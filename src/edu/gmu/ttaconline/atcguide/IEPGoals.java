package edu.gmu.ttaconline.atcguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * The activity that determines if the student have IEP Goals that require Assistive Technology solutions in any instructional areas
 * @author Animesh Jain 
 *
 **/
public class IEPGoals extends Activity {
	
	Intent currentIntent;
	String iepGoal="";
	RadioGroup radioIepGroup;
	private Context context;
	boolean isSample=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Set current view
		setContentView(R.layout.activity_iepgoals);
		//set Context
		context=getApplicationContext();
		//Set current view
		setCurrentIntent();
		isSample=currentIntent.getBooleanExtra("sample",false);
		//set iep goal
		//setIepGoal();
		//set listener
		setNextListener();
		//Calls for next activity
		
	}

	/**
	 * Sets the currentIntent from the running intent
	 */
	private void setCurrentIntent() {
		//current intent
			currentIntent=getIntent();		
		//OR get Intent from database
		//if it is already set
		setSelected();
		
	}
	
	/**
	 * To set the selected data from the view to the controller layer
	 */
	private void setSelected()
	{
		if(currentIntent.hasExtra("iepGoal")){
			iepGoal=currentIntent.getStringExtra("iepgoal");
			if(iepGoal.equalsIgnoreCase("yes")){
				RadioButton r= (RadioButton)findViewById(R.id.iepyes);
				r.setSelected(true);
			}
			else
			{
				RadioButton r= (RadioButton)findViewById(R.id.iepno);
				r.setSelected(true);
			}
			
		}
	}
	
	/**
	 * Persists the given IEPGoal answer to the persistence layer as well as to the intent
	 *  
	 */
	private void persistIEPGoals() {
	//Persisting IEP Goals to current intent
		currentIntent.putExtra("iepgoal", iepGoal);	
		if(!isSample){
		PersistenceBean.persistIntent(currentIntent.getStringExtra("studentid"), currentIntent, context);
		}
		else
			Toast.makeText(context, "Warning: sample data will not be saved", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Set listener to the next button click in the view
	 */
	private void setNextListener() {
		
		radioIepGroup = (RadioGroup) findViewById(R.id.iep);
		Button next = (Button)findViewById(R.id.nextbutton);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				int selectedId= radioIepGroup.getCheckedRadioButtonId();
				RadioButton radio = (RadioButton) findViewById(selectedId);
				iepGoal=(String) radio.getText();
				persistIEPGoals();
				if(iepGoal.equalsIgnoreCase("Yes"))
				{
					currentIntent.setClass(context,InstructionalAreas.class);
					startActivity(currentIntent);
				}
				else{
				    //Display alert
				    //PDFBean.fillFormData(PersistenceBean.getPDFData(),this.getData())
					
				}
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.iepgoals, menu);
		
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