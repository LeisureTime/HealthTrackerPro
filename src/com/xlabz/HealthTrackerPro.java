package com.xlabz;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class HealthTrackerPro extends Activity {
	private static final int PICK_IMAGE = 1;
	private Bitmap bitmap;
	private ImageView imgView;
	private ProgressDialog dialog;
  
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        
        Spinner spinner1 = (Spinner) findViewById(R.id.genderTxt);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.genderlist, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        
        Spinner spinner2 = (Spinner) findViewById(R.id.bloodTxt);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.bloodgrouplist, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        Button backBt = (Button) findViewById(R.id.backBt);
		backBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HealthTrackerPro.this, User_Profile_List.class);
				startActivity(intent);
				
			}
		});
		
        imgView = (ImageView) findViewById(R.id.ImageView);
        
        //UPLOAD IMAGE
        Button uploadBt = (Button) findViewById(R.id.uploadBt);
        uploadBt.setOnClickListener(new OnClickListener() {        	
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(
							Intent.createChooser(intent, "Select Picture"),
							PICK_IMAGE);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							e.getMessage(),
							Toast.LENGTH_LONG).show();
					Log.e(e.getClass().getName(), e.getMessage(), e);
				}
			}
        });

        //SUBMIT USER PROFILE REGISTRATION
        Button saveBt = (Button) findViewById(R.id.button1);
        saveBt.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (bitmap == null) {
					Toast.makeText(getApplicationContext(),
							"Please select image", Toast.LENGTH_SHORT).show();
				} else {
					dialog = ProgressDialog.show(HealthTrackerPro.this, "Uploading",
							"Please wait...", true);
					new ImageUploadTask().execute();
					
				}
			}
		});
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICK_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImageUri = data.getData();
				String filePath = null;

				try {
					// OI FILE Manager
					String filemanagerstring = selectedImageUri.getPath();

					// MEDIA GALLERY
					String selectedImagePath = getPath(selectedImageUri);

					if (selectedImagePath != null) {
						filePath = selectedImagePath;
					} else if (filemanagerstring != null) {
						filePath = filemanagerstring;
					} else {
						Toast.makeText(getApplicationContext(), "Unknown path",
								Toast.LENGTH_LONG).show();
						Log.e("Bitmap", "Unknown path");
					}

					if (filePath != null) {
						decodeFile(filePath);
					} else {
						bitmap = null;
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Internal error",
							Toast.LENGTH_LONG).show();
					Log.e(e.getClass().getName(), e.getMessage(), e);
				}
			}
			break;
		default:
		}
	}

	class ImageUploadTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... unsued) {
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost(ServerUrl.url);

				MultipartEntity entity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 100, bos);
				byte[] data = bos.toByteArray();				
				entity.addPart("photo", new ByteArrayBody(data,"newImage.png"));
				
				EditText nameText = (EditText)findViewById(R.id.nameTxt);
				EditText ageText = (EditText)findViewById(R.id.ageTxt);
				Spinner genderText = (Spinner)findViewById(R.id.genderTxt);
				Spinner bloodText = (Spinner)findViewById(R.id.bloodTxt);
				EditText heightText = (EditText)findViewById(R.id.htTxt);
				EditText weightText = (EditText)findViewById(R.id.wtTxt);
				EditText pulseText = (EditText)findViewById(R.id.pulseTxt);
				EditText bpText = (EditText)findViewById(R.id.bpTxt);
				EditText chlText = (EditText)findViewById(R.id.chlTxt);
				// Add your data
	 
				entity.addPart("name", new StringBody(nameText.getText().toString()));
				entity.addPart("age", new StringBody(ageText.getText().toString()));
				entity.addPart("gender", new StringBody(genderText.getSelectedItem().toString()));
				entity.addPart("bloodgroup", new StringBody(bloodText.getSelectedItem().toString()));
				entity.addPart("height", new StringBody(heightText.getText().toString()));
				entity.addPart("weight", new StringBody(weightText.getText().toString()));
				entity.addPart("pulse", new StringBody(pulseText.getText().toString()));
				entity.addPart("bp",new StringBody(bpText.getText().toString()));
				entity.addPart("cholesterol", new StringBody(chlText.getText().toString()));
				
				httpPost.setEntity(entity);
				HttpResponse response = httpClient.execute(httpPost,localContext);
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

				String sResponse = reader.readLine();
				Log.d("RESPONSE", sResponse);
				
				return sResponse;
			} catch (Exception e) {
				if (dialog.isShowing())
					dialog.dismiss();
				Toast.makeText(HealthTrackerPro.this,
						e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
				return null;
			}
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {
		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				if (dialog.isShowing())
					dialog.dismiss();

				if (sResponse != null) {					
					  JSONObject predictions = new JSONObject(sResponse);            
			            JSONArray ja = new JSONArray(predictions.getString("results"));

			            for (int i = 0; i < ja.length(); i++) {
		                    JSONObject jo = (JSONObject) ja.get(i);
		                   // Log.d("message-->",jo.getString("message"));
		                }
			            Intent intent = new Intent(HealthTrackerPro.this, User_Profile_List.class);
						startActivity(intent);
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	public void decodeFile(String filePath) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 1024;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);

		imgView.setImageBitmap(bitmap);

	}
    
}