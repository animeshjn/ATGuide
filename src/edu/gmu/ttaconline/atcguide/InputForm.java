package edu.gmu.ttaconline.atcguide;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.Toast;

public class InputForm extends Activity {
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context=getApplicationContext();
		
		
		setContentView(R.layout.activity_input_form);
		
		Button b=(Button)findViewById(R.id.nextbutton);
		b.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveData(v);
				//nextActivity();
				
			}

			
		});
		
	}

	private void saveData(View v) {
		//TODO save Data
		
		
		
		Intent i  = PersistenceBean.persistInputFormData((ViewGroup) v.getRootView(),context);
		Toast.makeText(context, "Data Saved", Toast.LENGTH_SHORT).show();
		i.setClass(context,IEPGoals.class);
		startActivity(i);
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.input_form, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
