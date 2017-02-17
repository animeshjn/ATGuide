package edu.gmu.ttaconline.atcguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class IEPReading extends Activity {
	Intent currentIntent;
	Context context;
	String IEPReading="";
	String IEPAlt="";
	RadioGroup read;
	RadioGroup iepAlternative;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_iepreading);
		context=getApplicationContext();
		setCurrentIntent();
		
		setNextListener();
		
		
	}

	
	private void setNextListener() {
		Button next=(Button)findViewById(R.id.nextbutton);
		next.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
					setRadioValues();
					currentIntent.putExtra("iepreading",IEPReading);
					currentIntent.putExtra("iepalt",IEPAlt);
					currentIntent.setClass(context, TaskForm.class);
					startActivity(currentIntent);
			}
		});
		
	}

	protected void setRadioValues() {
		 read = (RadioGroup) findViewById(R.id.read);
		 int selectedId= read.getCheckedRadioButtonId();
			RadioButton radio = (RadioButton) findViewById(selectedId);
			IEPReading=(String) radio.getText();
		iepAlternative=(RadioGroup) findViewById(R.id.iepalternative);
		RadioButton iepaltradio = (RadioButton) findViewById(selectedId);
		IEPAlt=(String) iepaltradio.getText();
	
	}


	private void setCurrentIntent() {
		currentIntent=getIntent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.iepreading, menu);
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
