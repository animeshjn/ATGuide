package edu.gmu.ttaconline.atcguide;

import java.util.ArrayList;

import com.commonsware.cwac.merge.MergeAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

/**
 * For the selection of Instructional areas
 * 
 * @author ajain13
 * 
 */

public class InstructionalAreas extends Activity {
	/** current running intent */
	Intent currentIntent;
	/** This context */
	Context context;
	/** Selected instructional areas */
	ArrayList<String> selectedInstructionalAreas = new ArrayList<String>();
	/** @deprecated logic for checkBox (Deprecated: use intent data instead) */
	CheckBoxBean instructionalCheck;
	String otherText = "";
	boolean isSample = false;
	boolean otherSelected = false;
	MergeAdapter merge = new MergeAdapter();
	String firstList[];
	String secondList[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		setCustomActionBar();
		setContentView(R.layout.activity_instructional_areas);
		
		setCurrentIntent(); // Retrieve current intent
		instructionalCheck = new CheckBoxBean();
		setInstuctionalAreas(); // set the view of instructional areas
		checkAreaFromDB();// check areas from db
		setNextListener(); // Persist selection and go to next activity
	}

	/**
	 * Sets the Custom action bar to this view 
	 */
	protected void setCustomActionBar() {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		ActionBar action = getActionBar();
		getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
		action.setDisplayShowCustomEnabled(true);

		getActionBar().setHomeButtonEnabled(false);
		View v = getLayoutInflater().inflate(R.layout.action_main, null);
//		v.findViewById(R.id.newrecord).setOnClickListener(
//				new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						//actionNew(v);
//					}
//				});
		//v.findViewById(R.id.helpbutton).setOnClickListener(getHelpListener());
		v.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(v);
	}
	
	/**
	 * Click listener to the Next button
	 */
	private void setNextListener() {
		Button next = (Button) findViewById(R.id.nextbutton);
		next.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// if(isSomethingSelected){
				if (otherSelected) {
					selectedInstructionalAreas.add("" + otherText);
				}

				PersistenceBean.persistInstructionalAreas(
						currentIntent.getStringExtra("studentid"),
						selectedInstructionalAreas, context);
				currentIntent.putStringArrayListExtra("selectedareas",
						selectedInstructionalAreas);
				currentIntent.putExtra("otherSelected", otherSelected);
				currentIntent.putExtra("othertext", otherText);
				currentIntent.setClass(context, IEPReading.class);
				PersistenceBean.persistCurrentId(
						currentIntent.getStringExtra("studentid"), context);
				PersistenceBean.persistIntent(
						currentIntent.getStringExtra("studentid"),
						currentIntent, context);
				startActivity(currentIntent);

			}
		});

		// ArrayList<Integer>
		// checkedIDList=instructionalCheck.getAllCheckedIds();

	}

	/**
	 * 
	 * @param instructionalAreas
	 *            List of areas to be checked
	 */
	public void CheckValuesFromPersistence(ArrayList<String> instructionalAreas) {

	}

	/**
	 * Set the instructional areas.
	 * 
	 */
	private void setInstuctionalAreas() {

		LinearLayout list1 = null;
		LinearLayout list2 = null;

		try {
			list1 = (LinearLayout) this.findViewById(R.id.list1);
			list2 = (LinearLayout) this.findViewById(R.id.list2);
			firstList = getResources().getStringArray(R.array.list1);
			secondList = getResources().getStringArray(R.array.list2);
			addListToView(firstList, list1);
			addListToView(secondList, list2);
			addOtherArea();
		} catch (Exception e) {
			Log.e("ATGUIDE", "Other Exception: " + e.getMessage());
		}

	}

	/**
	 * Checkbox Area from the database.
	 */
	private void checkAreaFromDB() {
		try {
			ArrayList<CharSequence> persistedList = PersistenceBean
					.getPersistedAreaList(
							PersistenceBean.getCurrentId(context), context);
			if (persistedList != null)
				for (CharSequence areaName : persistedList) {
					LinearLayout list1 = (LinearLayout) findViewById(R.id.list1);
					for (int i = 0; i < list1.getChildCount(); i++) {
						if (list1.getChildAt(i) instanceof LinearLayout) {
							LinearLayout checkLayout = (LinearLayout) list1
									.getChildAt(i);
							CheckBox cb = (CheckBox) checkLayout.getChildAt(0);
							String checkText = (String) cb.getText();
							if (checkText.contains(areaName)) {
								cb.setChecked(true);
							}
						}
					}

					LinearLayout list2 = (LinearLayout) findViewById(R.id.list2);
					for (int i = 0; i < list2.getChildCount(); i++) {
						if (list1.getChildAt(i) instanceof LinearLayout) {
							LinearLayout checkLayout = (LinearLayout) list2
									.getChildAt(i);
							CheckBox cb = (CheckBox) checkLayout.getChildAt(0);
							String checkText = (String) cb.getText();
							if (checkText.contains(areaName)) {
								cb.setChecked(true);
							}
						}
					}
				}
		} catch (Exception e) {
			Log.e("ATGUIDE", "EX: " + e);
		}
	}

	/**
	 * Add the instructional area 'Other' and set the listeners
	 */
	private void addOtherArea() {
		try {
			LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			// layoutparams.weight=50;
			final LinearLayout other = (LinearLayout) this
					.findViewById(R.id.otherlayout);
			other.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_LTR);
			CheckBox otherCheck = new CheckBox(context);
			otherCheck.setLayoutParams(layoutparams);
			otherCheck.setText("Other");
			otherCheck.setTextColor(Color.DKGRAY);
			otherCheck.setButtonDrawable(getResources().getDrawable(
					R.drawable.custom_checkbox));

			otherCheck
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							View v = (View) buttonView.getParent();
							EditText other = (EditText) v.findViewById(102456);
							otherSelected = isChecked;
							// other.setBackgroundColor(0);
							if (isChecked) {
								Toast.makeText(
										context,
										"Please enter the name for other instructional area",
										Toast.LENGTH_SHORT).show();
								other.setEnabled(true);
								other.requestFocus();

								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.showSoftInput(other,
										InputMethodManager.SHOW_FORCED);

								other.setFocusable(true);
								other.setFocusableInTouchMode(true);
								other.setCursorVisible(true);

								other.addTextChangedListener(new TextWatcher() {
									@Override
									public void onTextChanged(CharSequence s,
											int start, int before, int count) {
										// TODO Auto-generated method stub
									}

									@Override
									public void beforeTextChanged(
											CharSequence s, int start,
											int count, int after) {
										// TODO Auto-generated method stub
									}

									@Override
									public void afterTextChanged(Editable s) {
										// TODO Auto-generated method stub
										otherText = new String(s + "");
									}
								});
							} else {
								other.setEnabled(false);
								other.setFocusable(false);
								other.setText("");
							}
						}
					});
			EditText specify = new EditText(context);
			specify.setLayoutParams(layoutparams);
			specify.setEnabled(false);
			specify.setId(102456);
			specify.setFocusable(false);
			specify.clearFocus();
			specify.setBackground(getResources().getDrawable(
					R.drawable.textviewback));
			specify.setTextColor(Color.DKGRAY);
			specify.setMaxLines(1);
			other.addView(otherCheck);
			specify.setHint("Please specify");
			other.addView(specify);
			other.clearFocus();
		} catch (Exception e) {
			Log.e("ATGUIDE", e.getMessage());
		}

	}

	/**
	 * Adds the given list of instructional areas to the view
	 * 
	 * @param listArray
	 * @param listLayout
	 */
	private void addListToView(String listArray[], LinearLayout listLayout) {

		LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < listArray.length; i++) {

			LinearLayout currentListItem = new LinearLayout(context);
			currentListItem.setLayoutParams(layoutparams);
			CheckBox currentCheck = new CheckBox(context);
			currentCheck.setLayoutParams(layoutparams);
			currentCheck.setText(listArray[i]);

			currentCheck.setTextColor(Color.BLACK);
			currentCheck.setId(instructionalCheck.getNextCheckID());

			currentCheck
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,

						boolean isChecked) {
							if (isChecked) {
								selectedInstructionalAreas
										.add((String) buttonView.getText());
								// instructionalCheck.addChecked(buttonView.getId());
							} else
								selectedInstructionalAreas
										.remove((String) buttonView.getText());
							// instructionalCheck.removeChecked(buttonView.getId());
						}
					});
			currentCheck.setButtonDrawable(getResources().getDrawable(
					R.drawable.custom_checkbox));
			currentListItem.addView(currentCheck);
			Space space = new Space(context);
			space.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, 10));
			try {
				listLayout.addView(currentListItem);
				listLayout.addView(space);
			} catch (Exception e) {
				Log.e("ATGUIDE", "Other Exception: " + e.getMessage());
			}
		}
	}

	/**
	 * Set the value of current intent from current running student id
	 */
	private void setCurrentIntent() {
		currentIntent = PersistenceBean.getExistingIntent(
				PersistenceBean.getCurrentId(context), context);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.instructional_areas, menu);
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
