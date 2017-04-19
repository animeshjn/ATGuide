package edu.gmu.ttaconline.atcguide;

import java.util.ArrayList;

import com.commonsware.cwac.merge.MergeAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * Activity for first trial of the AT Inputs the additional data for the first
 * trial
 * 
 * @author animesh jain
 * 
 */
public class FirstTrial extends Activity {
	/*
	 * Overview: Type inheriting from activity for the first trial of the AT
	 * trials
	 */
	/* Instance variables */
	ArrayList<Area> areaList;// Store the lists of all areas
	ArrayList<String> trial1Texts = new ArrayList<String>();// store text only

	MergeAdapter merge = new MergeAdapter();
	Context context;// store current context
	LayoutInflater inflater;// store the layout inflator to inflate rows
	int id = 1001;// default id
	static int clickedId = 9999;// current clicked id
	private ArrayList<CharSequence> selectedInstructional; // list of selected
															// areas
	/* Methods */
	// Control start point
	/**
	 *@see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_trial);
		context = getApplicationContext();
		areaList = PersistenceBean.getPersistedAreaObjects("trial1"
				+ PersistenceBean.getCurrentId(context), context);
		inflater = getLayoutInflater();
		placeArea();
	}
	/**
	 * Place all areas in the view
	 */
	@SuppressLint("InflateParams")
	private void placeArea() {
		// Modifies: this view
		/*Effects: places the area, task and AT on the left panels & sets their
		 * respective listeners
		 */
		try {
			// Initializing data
			getData();
			//Set params
			LayoutParams textViewParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			// The list view on left side
			ListView instructional = (ListView) findViewById(R.id.instructionalAreasList);
			// The list of area
			for (Area area : areaList) {
				// For each area get a Row
				LinearLayout areaRow = (LinearLayout) inflater.inflate(
						R.layout.areataskrow, null);
				areaRow.setId(id += 2);
				// Add the area name
				TextView areaTextView = (TextView) areaRow
						.findViewById(R.id.areatextview);
				// area.setBackground(getResources().getDrawable(R.drawable.textviewback));
				areaTextView.setText(area.getAreaName());
				areaTextView
						.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_START);
				areaTextView.setLayoutParams(textViewParams);
				areaTextView.setId(area.parentId);
				
				// Set onClick listener to the area name
				// append current task for 1st trial
				for (Task task : area.tasks) {
					//If solutions aren't working 
					if (!task.solutions) {
						LinearLayout taskLayout = new LinearLayout(context);
						taskLayout.setOrientation(LinearLayout.VERTICAL);
						taskLayout.setLayoutParams(textViewParams);
						TextView tasktextView = new TextView(context);
						tasktextView.setText(task.getTaskname());
						taskLayout.setTag(area.getAreaName());
						tasktextView.setLayoutParams(textViewParams);
						tasktextView.setId(task.taskid);
						tasktextView.setTextColor(Color.DKGRAY);
						tasktextView.setPadding(5, 0,0,0);
						TextView assistiveTech=new TextView(context);
						assistiveTech.setPadding(10, 0, 0, 0);
						assistiveTech.setText("Choose AT");
						assistiveTech.setTextColor(Color.DKGRAY);
						//assistiveTech.setTextAlignment();
						taskLayout.addView(tasktextView);
						taskLayout.addView(assistiveTech);
						areaRow.addView(taskLayout);
												
					}
				}

				// Linear layout task
				// append Assistive technology to this task
				
				merge.addView(areaRow);
			}
			instructional.setAdapter(merge);
		} catch (Exception e) {
			Log.e("ATGUIDE",
					"Error retrieving data  FirstTrial->placeArea() \n" + e);
		}
	}

	private void getData() {
		try {
			// selectedInstructional = currentIntent
			// .getCharSequenceArrayListExtra("selectedAreas");
			selectedInstructional = PersistenceBean.getPersistedAreaList(
					"trial1" + PersistenceBean.getCurrentId(context), context);
			for (CharSequence cs : selectedInstructional) {
				trial1Texts.add(cs.toString());
			}
		} catch (Exception e) {
			Log.e("ATGUIDE", "Error retrieving data  firsttrial->getData() "
					+ e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first_trial, menu);
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
