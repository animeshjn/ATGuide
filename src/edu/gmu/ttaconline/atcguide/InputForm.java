package edu.gmu.ttaconline.atcguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * InputForm Activity to begin the fill data transaction for this student
 * 
 * @author Animesh Jain
 * 
 */
public class InputForm extends Activity {

	Context context;
	Intent currentIntent;
	String studentid;
	String studentgrade;
	String studentschool;
	String studentparticipant;
	int day;
	int month;
	int year;
	boolean isSample;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		setContentView(R.layout.activity_input_form);
		currentIntent = getIntent();
		isSample = currentIntent.getBooleanExtra("sample", false);
		// case open : or if exists PersistedIntent(currentStudentId)
		// PersistenceBean.getExistingIntent(currentIntent.getStringExtra("studentid"),
		// context);
		// fill form
		if (isSample)
			fillSampleData();
		if(currentIntent.getBooleanExtra("open", false))
		{	currentIntent =PersistenceBean.getExistingIntent(currentIntent.getStringExtra("studentid"), context);
			PersistenceBean.persistCurrentId(currentIntent.getStringExtra("studentid"), context);
		    currentIntent.putExtra("open",true);
		     fillDataFromIntent();
		}
		// else blank form
		// case Preview : fillPDF(Intent)
		Button b = (Button) findViewById(R.id.nextbutton);
		b.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveData(v);
			}
		});
	}

	private void fillDataFromIntent() {
		
		studentid =currentIntent.getStringExtra("studentid");
		studentgrade=currentIntent.getStringExtra("studentgrade");
		studentschool = currentIntent.getStringExtra("studentschool");
		studentparticipant = currentIntent.getStringExtra("studentparticipant");
		day = currentIntent.getIntExtra("day",31);
		month = currentIntent.getIntExtra("month",1);
		year = currentIntent.getIntExtra("year",2017);
		setFormData();
	}

	/**
	 * Set form data from the businessLogic to the view.
	 * @param year
	 * @param month
	 * @param day
	 * @param studentparticipant
	 * @param studentschool
	 * @param studentid
	 */
	private void setFormData() {
		EditText sid = ((EditText) findViewById(R.id.studentid));
		sid.setText(studentid);
		Spinner studentgrade = ((Spinner) (findViewById(R.id.studentgrade)));
		int i=0;
		for(String grades:getResources().getStringArray(R.array.gradearray)){
				if(grades.equalsIgnoreCase(this.studentgrade))
					break;
			i++;
		}
		studentgrade.setSelection(i);
		EditText school = ((EditText) (findViewById(R.id.studentschool)));
		school.setText(studentschool);
		EditText participant = ((EditText) (findViewById(R.id.participants)));
		participant.setText(studentparticipant);
		DatePicker datePicker = (DatePicker) findViewById(R.id.date);
		datePicker.updateDate(year, month, day);
		datePicker.refreshDrawableState();
	}

	/**
	 * Fills the sample data to this form view
	 */
	private void fillSampleData() {
		// TODO Auto-generated method stub
		studentid = "AA123";
		studentschool = "Port Bell Elementary School";
		studentparticipant = "Mr.Johns, Parent; Miss Sheets, GeneralEducationTeacher; Mrs. Monterey, Title I Teacher, Mrs. Carl,"
				+ "SPED Teacher; Mr. Dean, Administrator";
		day = 31;
		month = 0;
		year = 2017;
		Toast.makeText(context, "Loading Sample Data", Toast.LENGTH_SHORT)
				.show();
		setFormData();
	}

	
	/**
	 *Save Data from the view v
	 * @param view of current form
	 */
	private void saveData(View v) {
		// TODO save Data
		Intent intent;
		 //PersistenceBean.persistInputFormData((ViewGroup)v, context);
		// v.getRootView(),context);
		v = v.getRootView();
		String studentid = ((EditText) (v.findViewById(R.id.studentid)))
				.getText().toString();
		String studentgrade = ((Spinner) (v.findViewById(R.id.studentgrade)))
				.getSelectedItem().toString();
		String studentschool = ((EditText) (v.findViewById(R.id.studentschool)))
				.getText().toString();
		String studentparticipant = ((EditText) (v
				.findViewById(R.id.participants))).getText().toString();
		DatePicker datePicker = (DatePicker) v.findViewById(R.id.date);
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth() + 1;
		int year = datePicker.getYear();
		// Date date = new java.util.Date(year, month, day);
		String date = month + "-" + day + "-" + year;
		intent = currentIntent;
		intent.putExtra("studentid", studentid);
		intent.putExtra("studentgrade", studentgrade);
		intent.putExtra("studentparticipant", studentparticipant);
		intent.putExtra("studentschool", studentschool);
		intent.putExtra("date", date.toString());
		intent.putExtra("day", day);
		intent.putExtra("month", month);
		intent.putExtra("year", year);
		Log.d(PDFLogic.LOG_TAG, "Date: " + date);
		intent.setClass(context, IEPGoals.class);
		if(!isSample){
		PersistenceBean.persistIntent(intent.getStringExtra("studentid"),
				intent, context);
		PersistenceBean.persistCurrentId(intent.getStringExtra("studentid"), context);
		}
		startActivity(intent);
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
