package edu.gmu.ttaconline.atcguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Form_Summary extends Activity {
	Intent currentIntent;
	Context context;
	Activity activity=this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form__summary);
		currentIntent =getIntent();
		context= getApplicationContext();
		setIntentFromId(PersistenceBean.getCurrentId(context));
		
		setNextListener();
		//Check the last activity name 
		//Change the display texts
		//Change PDF listener// not required 
		// Set Next Listener
	}
	/**
	 * @param givenClassName the name of the class from which this is called
	 */
	public void setDisplayTexts(String givenClassName){
		/**Requires: Class name of the last caller class*/
		/**Modifies: Current view*/
		/**Effects: Sets the text in current view according to the caller class*/
		switch (givenClassName){
		
		case "FirstTrial":
			((TextView)findViewById(R.id.summary_first)).setText(getResources().getString(R.string.firstTrialSummary1));
			((TextView)findViewById(R.id.summary_body)).setText(getResources().getString(R.string.firstTrialSummary2));
			((TextView)findViewById(R.id.summary_body2)).setText(getResources().getString(R.string.firstTrialSummary3));
			((TextView)findViewById(R.id.summary_end)).setText(getResources().getString(R.string.firstTrialSummary4));
			break;
		case "SecondTrial":
			((TextView)findViewById(R.id.summary_first)).setText(getResources().getString(R.string.secondTrialSummary1));
			((TextView)findViewById(R.id.summary_body)).setText(getResources().getString(R.string.secondTrialSummary2));
			((TextView)findViewById(R.id.summary_body2)).setText(getResources().getString(R.string.secondTrialSummary3));
			((TextView)findViewById(R.id.summary_end)).setText(getResources().getString(R.string.secondTrialSummary4));
			break;
			
		case "RevisitFirstTrial":
		default:
		case "RevisitSecondTrial":
			((TextView)findViewById(R.id.summary_first)).setText(getResources().getString(R.string.firstTrialSummary1));
			((TextView)findViewById(R.id.summary_body)).setText(getResources().getString(R.string.firstTrialSummary2));
			((TextView)findViewById(R.id.summary_body2)).setText(getResources().getString(R.string.firstTrialSummary3));
			((TextView)findViewById(R.id.summary_end)).setText(getResources().getString(R.string.firstTrialSummary4));
		
			
		}
	}
	
	
	
	/**
	 * @param currentID Current Student ID being used
	 */
	public void setIntentFromId(String currentID){
		
		currentIntent= PersistenceBean.getExistingIntent(PersistenceBean.getCurrentId(context), context);
		String activityName =currentIntent.getStringExtra("activity_name");
		
		if(null == activityName){
		activityName=this.getLocalClassName();
		Log.d("ATGUIDE","Class Name Set:"+activityName);
		}
		
		setDisplayTexts(activityName);
				
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.form__summary, menu);
		return true;
	}

	private void setNextListener() {
		/**
		 * Modifies: Next button in the view Effects: Sets listener to the next
		 * button
		 * */

		currentIntent = PersistenceBean.getExistingIntent(
				PersistenceBean.getCurrentId(context), context);
		Button nextButton = (Button) findViewById(R.id.nextbutton);
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				PersistenceBean.persistAreaObject(areaList, "trial1"
//						+ PersistenceBean.getCurrentId(context), context);
				PDFLogic.activity = activity;
				currentIntent.setClass(context, PDFLogic.class);
				currentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				currentIntent
						.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				// Intent pdfService= new
				currentIntent.putExtra("trial1", true);
				PersistenceBean.persistIntent(
						PersistenceBean.getCurrentId(context), currentIntent,
						context);
				// Intent(getApplicationContext(),PDFLogic.class);
				android.widget.ProgressBar bar = new android.widget.ProgressBar(
						getApplicationContext());
				bar.setIndeterminate(true);
				bar.bringToFront();
				Log.d("ATGUIDE", "" + currentIntent.toString());
				// startService(currentIntent);
				Thread pdfThread = new Thread(new PDFLogic());
				pdfThread.start();
				Toast.makeText(context, "Please Wait", Toast.LENGTH_SHORT).show();
				
			}
		});

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
