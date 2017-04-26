package edu.gmu.ttaconline.atcguide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.commonsware.cwac.merge.MergeAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

/**
 * Activity for first trial of the AT Inputs the additional data for the first
 * trial
 * 
 * @author animesh jain
 * 
 */
public class FirstTrial extends FragmentActivity {
	/*
	 * Overview: Type inheriting from activity for the first trial of the AT
	 * trials
	 */
	/* Instance variables */
	ArrayList<Area> areaList;// Store the lists of all areas
	ArrayList<String> trial1Texts = new ArrayList<String>();// store text only
	Calendar myCalendar = Calendar.getInstance();
	EditText datePick;
	MergeAdapter merge = new MergeAdapter();
	Context context;// store current context
	LayoutInflater inflater;// store the layout inflator to inflate rows
	int id = 1001;// default id
	static int clickedId = 9999;// current clicked id
	private ArrayList<CharSequence> selectedInstructional; // list of selected
	TextWatcher _ATNameWatcher, participantsWatcher, trialDateWatcher;
	android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
	Activity activity;

	// areas
	/* Methods */
	// Control start point
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
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
		activity = this;
		datePick = (EditText) findViewById(R.id.date);
		setATListener();
		setDatePickListener();
		clickFirstItem();
		
	}

	private void clickFirstItem() {
		ListView lv = (ListView) findViewById(R.id.instructionalAreasList);
		MergeAdapter m = (MergeAdapter) lv.getAdapter();
		m.getCount();
		View first = (View) m.getItem(0);
		if (first != null && first instanceof LinearLayout){
			((LinearLayout)first).getChildAt(1).callOnClick();
		} 
	}

	/**
	 * Set Listener for Assistive technology. Select the listener
	 */
	private void setATListener() {
		ImageButton add = (ImageButton) findViewById(R.id.addat);
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AT_List atlist = new AT_List();
				atlist.setFirstTrail(activity);
				atlist.show(fragmentManager, "AT List");
			}
		});

	}

	/**
	 * Place all areas in the view
	 */
	@SuppressLint("InflateParams")
	private void placeArea() {

		// Modifies: this view
		/*
		 * Effects: places the area, task and AT on the left panels & sets their
		 * respective listeners
		 */
		try {
			// Initializing data
			getData();
			// Set params
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
					// If solutions aren't working
					if (!task.solutions) {
						LinearLayout taskLayout = new LinearLayout(context);
						taskLayout.setOrientation(LinearLayout.VERTICAL);
						taskLayout.setLayoutParams(textViewParams);
						TextView tasktextView = new TextView(context);
						tasktextView.setText(task.getTaskname());
						taskLayout.setTag(area.getAreaName());
						tasktextView.setLayoutParams(textViewParams);
						tasktextView.setId(task.taskid);
						tasktextView.setTextColor(Color.BLACK);
						tasktextView.setTypeface(null, Typeface.BOLD);
						tasktextView.setPadding(15, 0, 0, 0);
						TextView assistiveTech = new TextView(context);
						assistiveTech.setPadding(25, 0, 0, 0);
						assistiveTech.setText("Choose AT");
						assistiveTech.setTextColor(Color.BLACK);
						assistiveTech.setId(id++);
						assistiveTech.setOnClickListener(getATListener());
						// assistiveTech.setTextAlignment();
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

	/**
	 * Get Data from intent and the persistence layer.
	 */
	private void getData() {
		try {
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

	/**
	 *Allows a pop-up calendar on click of EditTextView of the Date 
	 */
	public void setDatePickListener() {
		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateLabel();
			}

		};

		datePick.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePickerDialog dialog = new DatePickerDialog(FirstTrial.this,
						date, myCalendar.get(Calendar.YEAR), myCalendar
								.get(Calendar.MONTH), myCalendar
								.get(Calendar.DAY_OF_MONTH));
				dialog.getDatePicker().setSpinnersShown(true);
				dialog.getDatePicker().setCalendarViewShown(false);
				dialog.show();
			}
		});
	}

	/**
	 * Update the date label to the calendar object
	 */
	private void updateLabel() {
		String myFormat = "MM/dd/yy"; // In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		datePick.setText(sdf.format(myCalendar.getTime()));
	}

	/**
	 * 
	 * @return OnClickListener for the AT 
	 */
	public OnClickListener getATListener() {

		OnClickListener atL = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Remove Previous Listeners if set
				
				EditText atname = (EditText) findViewById(R.id.at);
				atname.removeTextChangedListener(_ATNameWatcher);
				EditText participantView = (EditText) findViewById(R.id.participants);
				participantView.removeTextChangedListener(participantsWatcher);
				EditText dateView = (EditText) findViewById(R.id.date);
				dateView.removeTextChangedListener(trialDateWatcher);
				// AT View
				TextView aTView = (TextView) v;
				CharSequence atName = aTView.getText();
				clickedId = v.getId();
				//Parent
				LinearLayout parent = (LinearLayout) v.getParent().getParent();
				TextView area = (TextView) parent.getChildAt(0);
				CharSequence areaText = area.getText();
				TextView taskView = (TextView) ((LinearLayout) v.getParent()).getChildAt(0);
				Area areaobj = getAreaByName(areaText);
				Task t = areaobj.getTaskById(taskView.getId());
				if (t.ats.size() == 0) {
					AT at = new AT();
					at.task = t.taskname;
					at.ATName = atName.toString();
					at.instructionalArea = area.getText().toString();
					at.id = clickedId;
					setAtToView(at, v);
					t.ats.add(at);
				} else {
					setAtToView(t.getATById(clickedId), v);
				}
				highlightThis(v);
				setAddATListener(v);
				setDeleteATListener(v);
			}
		};
		return atL;
	}

	/**
	 * 
	 * Sets the given AT to the view.
	 * 
	 * @param at
	 *            assitive technology object to be set to view
	 * @param currentClicked
	 *            view selected on left panel
	 */
	public void setAtToView(AT at, View currentClicked) {
		/*
		 * Requires : the AT Listener to the change in participants clicked
		 * view. Modifies: Current view and this. Effects: Sets the given AT to
		 * the view.
		 */
		((TextView) findViewById(R.id.areatitle)).setText(at
				.getInstructionalArea());
		((TextView) findViewById(R.id.taskname)).setText(at.task);
		((EditText) findViewById(R.id.at)).setText(at.getATName());
		((EditText) findViewById(R.id.participants)).setText(at
				.getParticipants());
		((EditText) findViewById(R.id.date)).setText(at.getFirstTrialDate());
		setATNameListener(at, currentClicked);
		setPartcipantListener(at);
		setCompletionDateListener(at);

	}

	/**
	 * Listener to the change in AT Name
	 * 
	 * @param at
	 *            Assistive Technology type instance
	 */
	public void setATNameListener(final AT at, final View currentClicked) {
		EditText atname = (EditText) findViewById(R.id.at);
		atname.removeTextChangedListener(_ATNameWatcher);
		_ATNameWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				at.setATName(s.toString());
				((TextView) currentClicked).setText(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};

		atname.addTextChangedListener(_ATNameWatcher);
	}

	/**
	 * Listener to the change in participants
	 * 
	 * @param at
	 *            Assistive Technology type instance
	 * 
	 */
	public void setPartcipantListener(final AT at) {
		/*
		 * Requires: AT to be binded to the participants. Modifies: this,
		 * current view and AT. Effects: Listener to the change in participants
		 */
		EditText participantView = (EditText) findViewById(R.id.participants);
		participantView.removeTextChangedListener(participantsWatcher);
		participantsWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// Text Changed , bind to AT
				at.setParticipants(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};
		participantView.addTextChangedListener(participantsWatcher);

	}

	/**
	 * Listener to the change in completion date
	 * 
	 * @param at
	 *            Assistive Technology type instance
	 */
	public void setCompletionDateListener(final AT at) {
		/* Requires: AT to be binded to the participants. */
		/* Modifies: this, current view and AT */
		/* Effects: Listener to the change in dateview */
		EditText dateView = (EditText) findViewById(R.id.date);
		dateView.removeTextChangedListener(trialDateWatcher);
		trialDateWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// Text Changed , bind to AT
				at.setFirstTrialDate(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};
		dateView.addTextChangedListener(trialDateWatcher);

	}
	
	/**
	 *Sets the Listener to the ADD New AT Button 
	 */
	public void setAddATListener(final View op) {
		try {
			// TODO: ADD Listener
			OnClickListener addCLick = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					EditText atname = (EditText) findViewById(R.id.at);
					atname.removeTextChangedListener(_ATNameWatcher);
					EditText participantView = (EditText) findViewById(R.id.participants);
					participantView.removeTextChangedListener(participantsWatcher);
					EditText dateView = (EditText) findViewById(R.id.date);
					dateView.removeTextChangedListener(trialDateWatcher);
					LinearLayout taskLayout = (LinearLayout) op.getParent();
					TextView assistiveTech = new TextView(context);
					assistiveTech.setPadding(25, 0, 0, 0);
					assistiveTech.setText("Choose AT");
					assistiveTech.setTextColor(Color.BLACK);
					assistiveTech.setId(id++);
					LinearLayout areaLayout= (LinearLayout)taskLayout.getParent();
					String areaText=((TextView)areaLayout.getChildAt(0)).getText().toString();
					Area areaobj = getAreaByName(areaText);
					Task t = areaobj.getTaskById(taskLayout.getChildAt(0).getId());
						AT at = new AT();
						at.task = t.taskname;
						at.ATName = "Choose AT";
						at.instructionalArea = areaText;
						at.id = assistiveTech.getId();
						
						t.ats.add(at);
						setAtToView(at, v);
					assistiveTech.setOnClickListener(getATListener());
					Log.d("ATGuide","Area Text: "+areaText);
					Log.d("ATGuide","Area Text: "+areaText);
					taskLayout.addView(assistiveTech);
					assistiveTech.callOnClick();
				}
			};
			((Button)findViewById(R.id.addnewat)).setOnClickListener(addCLick);
		} catch (ClassCastException e) {
			Log.e("ATGuide", "" + e);
		} catch (Exception e) {
			Log.e("ATGuide", "" + e);
		}
	}

	
	/**
	 * Deletes the AT from logic and View
	 * @param op
	 */
	public void setDeleteATListener(final View op){
		//Requires: current selected AT
		// Modifies: this, current view, area and task
		//Effects:  Deletes the AT from logic and View
		try {
			OnClickListener addCLick = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Remove All listeners
					EditText atname = (EditText) findViewById(R.id.at);
					atname.removeTextChangedListener(_ATNameWatcher);
					EditText participantView = (EditText) findViewById(R.id.participants);
					participantView.removeTextChangedListener(participantsWatcher);
					EditText dateView = (EditText) findViewById(R.id.date);
					dateView.removeTextChangedListener(trialDateWatcher);
					//Get Layouts
					LinearLayout taskLayout = (LinearLayout) op.getParent();
					TextView assistiveTech = (TextView)op;
					LinearLayout areaLayout= (LinearLayout)taskLayout.getParent();
					String areaText=((TextView)areaLayout.getChildAt(0)).getText().toString();
					//If deletion invalid
					if(taskLayout.getChildCount()<=2){
						Toast.makeText(context, "At least one AT is required for a task", Toast.LENGTH_SHORT).show();
					}	
					else{//getObjects
					Area areaobj = getAreaByName(areaText);
					Task t = areaobj.getTaskById(taskLayout.getChildAt(0).getId());
					//Delete from Logics
					t.ats.remove(t.getATById(assistiveTech.getId()));
					//Delete from view
					taskLayout.removeView(assistiveTech);
					//Select other AT
					taskLayout.getChildAt(1).callOnClick();}
				}
			};
			((Button)findViewById(R.id.deleteat)).setOnClickListener(addCLick);
		} catch (ClassCastException e) {
			Log.e("ATGuide", "" + e);
		} catch (Exception e) {
			Log.e("ATGuide", "" + e);
		}
		
	}
	
	

	/**
	 * Highlights the given view (un-highlights all the other views )
	 * 
	 * @param tv
	 */
	public void highlightThis(View tv) {
		ListView lv = (ListView) findViewById(R.id.instructionalAreasList);
		MergeAdapter m = (MergeAdapter) lv.getAdapter();
		m.getCount();
		//
		for (int c = 0; c <= m.getCount(); c++) {
			View tt = (View) m.getItem(c);
			if (tt != null)
				tt.setBackgroundResource(0);
			if (tt != null && tt instanceof LinearLayout) {
				LinearLayout ll = (LinearLayout) tt;
				ll.setBackgroundResource(0);
				for (int i = 0; i < ll.getChildCount(); i++) {
					View tc = (View) ll.getChildAt(i);
					tc.setBackgroundResource(0);
					if (tc instanceof LinearLayout) {
						LinearLayout lnk = (LinearLayout) tc;
						for (int j = 0; j < lnk.getChildCount(); j++)
							lnk.getChildAt(j).setBackgroundResource(0);
					}
				}
			}
		}

		tv.setBackground(getResources().getDrawable(R.drawable.highlighted));
		if (tv.getParent() instanceof LinearLayout)
			((LinearLayout) tv.getParent()).getChildAt(0).setBackgroundColor(
					Color.CYAN);
	}

	/**
	 * To get area object by name from the list
	 * @param areaname
	 * @return
	 */
	public Area getAreaByName(CharSequence areaname) {
		if (areaname != null)
			for (Area area : areaList) {
				if (area != null
						&& area.getAreaName().trim()
								.equalsIgnoreCase(areaname.toString().trim())) {
					return area;
				}
			}
		return null;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first_trial, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
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
