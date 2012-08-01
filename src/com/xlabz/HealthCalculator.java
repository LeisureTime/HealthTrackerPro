package com.xlabz;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class HealthCalculator extends ListActivity {
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.health_calculator);
	 ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.health_calculator_list, android.R.layout.simple_list_item_1);
     setListAdapter(adapter);
     
     Button backBt = (Button) findViewById(R.id.backBt);
		backBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HealthCalculator.this, HomeGrid.class);
				startActivity(intent);
				
			}
		});
		
		final ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
	        	Toast.makeText(HealthCalculator.this, "position "+position, 100).show();
	        	if(position == 0){
	        		Intent intent = new Intent(HealthCalculator.this, BMI.class);
					startActivity(intent);
	        	}
	        }});
	        	
}

}
