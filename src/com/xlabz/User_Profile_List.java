package com.xlabz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.xlabz.pojo.UserProfile;

public class User_Profile_List extends ListActivity {
	private ProgressDialog dialog;
	public UserList_Adapter adpt;
	ArrayList<UserProfile> userList = new ArrayList<UserProfile>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile_list);

		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();

		HttpGet httpGet = new HttpGet(ServerUrl.CI_REST_GETALL_URL);
		try {
			HttpResponse response = httpClient.execute(httpGet, localContext);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));

			String sResponse = reader.readLine();
			Log.d("SRESPONSE", sResponse);

			if (sResponse != null) {
				// JSONObject predictions = new JSONObject(sResponse);
				JSONArray ja = new JSONArray(sResponse);

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					UserProfile up = new UserProfile();
					up.setName(jo.getString("name"));
					up.setId(jo.getInt("id"));
					userList.add(up);
					// Log.d("name-->",jo.getString("name"));
				}
				adpt = new UserList_Adapter(this, userList);
				setListAdapter(adpt);
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ADD USER
		Button addBt = (Button) findViewById(R.id.addBt);
		addBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(User_Profile_List.this,
						UserActivity.class);
				startActivity(intent);

			}
		});

		// BACK
		Button backBt = (Button) findViewById(R.id.backBt);
		backBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(User_Profile_List.this,
						HomeGrid.class);
				startActivity(intent);

			}
		});
	}

	// SHOW ALERT BOX BY CLICKING ON LIST ITEM
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// String item = (String) userList.get(position).getName();
		// Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
		Show_Alert_box(v.getContext(), "Please select action.", position);
	}

	// USER ASYNCRONUS TASK
	class UserHttpRequest extends AsyncTask<URL, Integer, String> {
		protected String doInBackground(URL... url) {
			try {
				Log.d("Response-122222->", url.toString());
				HttpURLConnection httpCon = (HttpURLConnection) url[0]
						.openConnection();
				httpCon.setDoOutput(true);
				httpCon.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				httpCon.setRequestMethod("DELETE");
				httpCon.connect();
				return httpCon.getResponseMessage();
			} catch (Exception e) {

				Toast.makeText(User_Profile_List.this, e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
				return null;
			}
		}

		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				Log.d("Response-122222->", sResponse);
				Intent intent = new Intent(User_Profile_List.this,
						User_Profile_List.class);
				startActivity(intent);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}
	}

	// ALERT BOX FOR DELETE A USER
	public void Show_Alert_box(Context context, String message, int position) {
		final int pos = position;

		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.create();
		alertDialog.setTitle("User Delete ?");
		alertDialog.setButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				URL url = null;
				try {
					url = new URL(ServerUrl.CI_REST_DELETE_URL
							+ userList.get(pos).getId());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				new UserHttpRequest().execute(url);
				adpt.notifyDataSetChanged();

			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});

		alertDialog.setMessage(message);
		alertDialog.show();
	}
}
