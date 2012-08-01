package com.xlabz;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xlabz.pojo.UserProfile;

public class UserList_Adapter extends ArrayAdapter<UserProfile> {
	 final Activity context;
	 private List<UserProfile> objects = new ArrayList<UserProfile>();
	public UserList_Adapter(Activity context,List<UserProfile> objects) {
		super(context, R.layout.user_profile_item, objects);
		this.context = context;
		this.objects = objects;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.user_profile_item, null, true);
		TextView nameTextview = (TextView) rowView.findViewById(R.id.nameTextview);
		//ImageView photoimageView = (ImageView) rowView.findViewById(R.id.PhotoimageView);		
		//Log.d("IN VIEW", String.valueOf(objects.size()));
		nameTextview.setText(objects.get(position).getName());
		return rowView;
	}

}
