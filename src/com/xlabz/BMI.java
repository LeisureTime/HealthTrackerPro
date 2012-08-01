package com.xlabz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BMI extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.bmi);
	
	Button homeBt = (Button) findViewById(R.id.homeBt);
	homeBt.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(BMI.this, HealthCalculator.class);
			startActivity(intent);
			
		}
	});
}
}
