package com.xlabz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class HomeGrid extends Activity {

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.home_grid);

	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	           // Toast.makeText(HomeGrid.this, "" + position, Toast.LENGTH_SHORT).show();
	        	if(position==0){
	        		Intent intent = new Intent(HomeGrid.this, User_Profile_List.class);
	        		startActivity(intent);
	        	}
	        	if(position==1){
	        		Intent intent = new Intent(HomeGrid.this, HealthCalculator.class);
	        		startActivity(intent);
	        	}
	        	
	        }
	    });
	}
}
