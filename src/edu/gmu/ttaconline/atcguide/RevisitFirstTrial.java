package edu.gmu.ttaconline.atcguide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.commonsware.cwac.merge.MergeAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class RevisitFirstTrial extends FragmentActivity {
	/*
	 * Overview: Type inheriting from activity for the first trial of the AT
	 * trials
	 */
	/* Instance variables */
	ArrayList<Area> areaList;// Store the lists of all areas
	ArrayList<String> trial1Texts = new ArrayList<String>();// store text only
	ArrayList<Area> trial2List = new ArrayList<Area>();;
	ArrayList<String> trial2TextList = new ArrayList<String>();
	String studentid = "";
	Calendar myCalendar = Calendar.getInstance();
	EditText datePick;
	MergeAdapter merge = new MergeAdapter();
	Context context;// store current context
	LayoutInflater inflater;// store the layout inflator to inflate rows
	int id = 1001;// default id
	static int clickedId = 9999;// current clicked id
	private ArrayList<CharSequence> selectedInstructional; // list of selected
	TextWatcher _ATNameWatcher, participantsWatcher, trialDateWatcher,
			_ActionToWatcher;
	android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
	Activity activity;
	Intent currentIntent;
	OnClickListener addATCLick;
	boolean exploreVA = false;
	String exploringVA = "Exploring VA";
	boolean open = false;
	boolean trial2 = false;
	String URI__REVISIT_FIRST_TRIAL = "http://revisitTrial1.atguide.com";

	// areas
	/* Methods */
	// Control start point
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_revisit_first_trial);
		context = getApplicationContext();
		areaList = PersistenceBean.getPersistedAreaObjects("trial1"
				+ PersistenceBean.getCurrentId(context), context);
		inflater = getLayoutInflater();
		// Get existing useful data from intent and SQLite

		try {
			getData();
			checkUri();

			// toast("data string"+ getIntent().getDataString());
			// if not open

			placeAreaFromDB();
			studentid = getIntent().getStringExtra("studentid");
			activity = this;
			datePick = (EditText) findViewById(R.id.date);
			// setATListener();
			setDatePickListener();
			clickFirstItem();
			setNextListener();
		} catch (Exception e) {
			Log.e("AT GUIDE", "Exception in FirstTrial.onCreate 104: *" + e);
		}
	}

	private void checkUri() {

		Uri uri = getIntent().getData();
		if (uri != null) {
			try {
				exploringVA = ""
						+ getIntent().getStringExtra("dataFromAIMVANavigator");
				Area exploreArea = getAreaByName("Exploring VA");
				if (null != exploreArea && null != exploreArea.tasks
						&& exploreArea.tasks.size() != 0) {
					Task exploreTask = exploreArea.tasks.get(0);
					if (exploreTask != null && exploreTask.ats != null
							&& exploreTask.ats.size() != 0) {
						AT exploreAT = exploreTask.ats.get(0);
						exploreAT.setATName(exploringVA);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Set Listener for the Next Button
	 */
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
				// Alert About trial 2
				validateSolutions();
				if (trial2) {
					AlertDialog.Builder info = new AlertDialog.Builder(activity);
					info.setMessage(getResources()
							.getString(R.string.trial1nav).toString());
					info.setCancelable(true);
					info.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// FWD to trial 2
									currentIntent.putExtra("trial2", trial2);
									currentIntent.setClass(context,
											SecondTrial.class);
									PersistenceBean.persistAreaObject(
											trial2List, "trial2" + studentid,
											context);
									Log.d("ATGUIDE",
											"student id on next persist"
													+ studentid);
									PersistenceBean.persistInstructionalAreas(
											"trial2" + studentid,
											trial2TextList, context);
									startActivity(currentIntent);
								}
							});
					info.setNeutralButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
					AlertDialog infoAlert = info.create();
					infoAlert.setCanceledOnTouchOutside(false);
					infoAlert.setCancelable(false);
					infoAlert.show();

				} else {

					AlertDialog.Builder info = new AlertDialog.Builder(activity);
					info.setMessage(getResources().getString(
							R.string.adequatesolution).toString());
					info.setCancelable(true);
					info.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Toast.makeText(context, "Please Wait",
											Toast.LENGTH_SHORT).show();
									PDFLogic.activity = activity;
									// Intent pdfService= new
									// Intent(activity.getApplicationContext(),PDFLogic.class);
									currentIntent.setClass(context,
											PDFLogic.class);
									currentIntent
											.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									currentIntent
											.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
									// Intent pdfService= new
									// Intent(getApplicationContext(),PDFLogic.class);
									android.widget.ProgressBar bar = new android.widget.ProgressBar(
											getApplicationContext());
									bar.setIndeterminate(true);
									bar.bringToFront();
									// startService(currentIntent);

									Thread t = new Thread(new PDFLogic());
									t.start();

								}
							});
					info.setNeutralButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
					AlertDialog infoAlert = info.create();
					infoAlert.setCanceledOnTouchOutside(false);
					infoAlert.setCancelable(false);
					infoAlert.show();
					// setContentView(layoutResID);

				}

			}
		});

	}

	/**
	 * Click the first item in the Adapter
	 */
	private void clickFirstItem() {

		ListView lv = (ListView) findViewById(R.id.instructionalAreasList);
		MergeAdapter m = (MergeAdapter) lv.getAdapter();
		m.getCount();
		View first = (View) m.getItem(0); // Linear Layout
		if (first != null && first instanceof LinearLayout) {
			LinearLayout taskLay = (LinearLayout) ((LinearLayout) first)
					.getChildAt(1);
			taskLay.getChildAt(1).callOnClick();
		}
	}

	/**
	 * Set Listener for Assistive technology. Select the listener
	 */
	private void setATListener() {

		ImageButton add = (ImageButton) findViewById(R.id.addat);
		add.setImageResource(R.drawable.plusstrategy);
		// add.setLayoutParams(new LinearLayout.LayoutParams());
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
	 * Place the special listener to call navigator app
	 */
	private void setSpecialATListener() {

		/* Requires: */
		/* Modifies: */
		/* Effects: */
		ImageButton add = (ImageButton) findViewById(R.id.addat);
		add.setImageResource(R.drawable.navigator);
		add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Toast.makeText(context, "Call the navigator App",
						Toast.LENGTH_SHORT).show();
				// go to the AIM VA Eligibility at
				// http://aimeligibility.com
				Uri aimEligible = Uri.parse("http://aimnavigator.com/");
				try {
					Intent aimIntent = new Intent(Intent.ACTION_VIEW,
							aimEligible);
					// aimIntent.addFlags(Intent.URI_INTENT_SCHEME);
					// aimIntent
					// .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					// aimIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// aimIntent.putExtras(currentIntent);
					aimIntent.putExtra("studentid",
							"" + currentIntent.getStringExtra("studentid")+","+currentIntent.getStringExtra("studentgrade")+","+currentIntent.getStringExtra("studentparticipant")+","+URI__REVISIT_FIRST_TRIAL);
					aimIntent.putExtra(
							"studentparticipant",
							""
									);
					aimIntent.putExtra(
							"studentgrade",
							""
									);
					
					aimIntent
							.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					aimIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_SINGLE_TOP);
					// aimIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
					aimIntent.setDataAndNormalize(aimEligible);
					PackageManager packageManager = getPackageManager();
					List<ResolveInfo> activities = packageManager
							.queryIntentActivities(aimIntent, 0);
					boolean isIntentSafe = activities.size() > 0;
					boolean installed = false;
					if (isIntentSafe) {
						for (ResolveInfo resolveInfo : activities) {
							if (resolveInfo.activityInfo.packageName
									.contains("aim")) {
								installed = true;
								aimIntent
										.setPackage(resolveInfo.activityInfo.packageName);
							}
						}
					}

					//aimIntent.putExtra("open", true);
					PersistenceBean.persistIntent(
							currentIntent.getStringExtra("studentid"),
							currentIntent, context);
					// Log.d("ATGUIDE","intent "+aimIntent.getStringExtra("studentid")+aimIntent.getStringExtra("studentgrade"));
					PersistenceBean.persistCurrentId(
							currentIntent.getStringExtra("studentid"), context);
					if (installed)
						startActivity(aimIntent);
					else {
						
						aimIntent = new Intent(Intent.ACTION_VIEW, (Uri
								.parse("market://com.kihdapps.aimnavigator")));
						Toast.makeText(getApplicationContext(),
								"AIM Navigator not installed",
								Toast.LENGTH_SHORT).show();
//						aimIntent.putExtras(currentIntent);
//						aimIntent.putExtra("RedirectUri",
//								URI__REVISIT_FIRST_TRIAL);

						startActivity(aimIntent);
					}

				} catch (Exception e) {
					Log.e("ATGUIDE", " " + e.getMessage());
				}

			}
		});

	}

	@Override
	protected void onNewIntent(Intent intent) {

		// super.onNewIntent(intent);
		toast("On New Intent Called");

		// super.onNewIntent(intent);
		Toast.makeText(context, "New Intent Called", Toast.LENGTH_SHORT).show();
		EditText ATname = (EditText) findViewById(R.id.at);
		Uri uri = intent.getData();
		if (uri != null) {
			try {
				exploringVA = ""
						+ intent.getStringExtra("dataFromAIMVANavigator");
				log("Data from AIMVA Navigator:" + exploringVA);
				ATname.setText("" + exploringVA);
			} catch (Exception e) {
				Log.e("ATGUIDE", "Exception in checkUri caught at line 135: "
						+ e);

				log("Exception" + e);
			}
		}

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
			// Set params
			LayoutParams textViewParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			// The list view on left side
			ListView instructional = (ListView) findViewById(R.id.instructionalAreasList);
			// The list of area
			// int atCount = -1;
			String iepAlt = currentIntent.getStringExtra("iepalt");
			if (null != iepAlt && iepAlt.equalsIgnoreCase("Yes")) {
				// Add Aim Navigator to area and create special view
				// Add AreaName as Exploring AT
				// Add On click Listener: OnClick: Add extra button to call
				// application
				// add check to the data for incoming URL
				// Auto Fill from the data
				Area nav = new Area("Exploring VA");
				// nav.addTask();
				nav.parentId = id++;
				Task explorer = new Task();
				explorer.solutions = false;
				explorer.taskid = id++;
				explorer.setAreaname(nav.getAreaName());
				explorer.taskname = "Exploring VA";
				nav.addTask(explorer);
				areaList.add(nav);
			}

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
						// atCount++;
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
						if (area.getAreaName().equalsIgnoreCase("Exploring VA")) {

							assistiveTech
									.setOnClickListener(getExplorerATListener());

						} else {
							assistiveTech.setOnClickListener(getATListener());

						}
						AT at0 = new AT();
						at0.ATName = "";
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

	@Override
	protected void onRestart() {

		super.onRestart();
		Toast.makeText(context, "Restart called", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Place the elements: Area, Task and ATs from database
	 */
	@SuppressLint("InflateParams")
	private void placeAreaFromDB() {

		// Modifies: this view
		/*
		 * Effects: places the area, task and AT on the left panels & sets their
		 * respective listeners from the database
		 */
		try {
			// Initializing data
			// Set params
			LayoutParams textViewParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			// The list view on left side
			ListView instructional = (ListView) findViewById(R.id.instructionalAreasList);
			// The list of area
			// int atCount = -1;
			String iepAlt = currentIntent.getStringExtra("iepalt");
			if (null != iepAlt && iepAlt.equalsIgnoreCase("Yes")) {
				// Add Aim Navigator to area and create special view
				// Add AreaName as Exploring AT
				// Add OnClick Listener: OnClick: Add extra button to call
				// application
				// add check to the data for incoming URL
				// Auto Fill from the data
				Area nav;
				Area navOld = getAreaByName("Exploring VA");
				if (null != navOld) {
					nav = navOld;
				} else {
					nav = new Area("Exploring VA");
				}
				// nav.addTask();
				nav.parentId = id++;
				// nav.tasks.clear();
				AT exploreAT = null;
				Task t;
				if (null == nav.tasks || nav.tasks.size() == 0) {
					t = null;

				} else {
					t = nav.tasks.get(0);
				}

				if (null != t) {
					if (t.ats.size() == 0) {
						exploreAT = null;
					} else {
						exploreAT = t.ats.get(0);
					}
				}

				if (null == exploreAT) {
					exploreAT = new AT();
					exploreAT.ATName = exploringVA;
					exploreAT.participants = "";
					exploreAT.firstTrialDate = "";
					exploreAT.task = "Exploring VA";
				}
				Task explorer = null;

				if (null == t) {
					explorer = new Task();
					explorer.solutions = false;
					explorer.taskid = id++;
					explorer.setAreaname(nav.getAreaName());
					explorer.taskname = "Exploring VA";
				} else {
					explorer = t;
				}
				explorer.ats.clear();
				explorer.ats.add(exploreAT);
				nav.tasks.clear();
				nav.addTask(explorer);
				if (null == navOld)
					areaList.add(nav);
			}

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
						// atCount++;
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
						taskLayout.addView(tasktextView);
						TextView assistiveTech = new TextView(context);
						if (task.ats.size() > 0) {
							for (AT at : task.ats) {
								if (null != at) {
									assistiveTech = new TextView(context);
									assistiveTech.setPadding(25, 0, 0, 0);
									assistiveTech.setText(at.getATName());
									assistiveTech.setTextColor(Color.BLACK);
									assistiveTech.setId(at.id);
									if (area.getAreaName().equalsIgnoreCase(
											"Exploring VA")) {

										assistiveTech
												.setOnClickListener(getExplorerATListener());
									} else {
										assistiveTech
												.setOnClickListener(getATListener());
									}
									taskLayout.addView(assistiveTech);
								}
							}
						} else {
							assistiveTech.setTextColor(Color.BLACK);
							assistiveTech.setId(id++);
							assistiveTech.setOnClickListener(getATListener());
							taskLayout.addView(assistiveTech);
						}

						// }

						// AT at0= new AT();
						// at0.ATName= "";
						// assistiveTech.setTextAlignment();
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
					"Error retrieving data  FirstTrial->placeAreaFromDB() \n"
							+ e);
		}
	}

	/**
	 * Get Data from intent and the persistence layer.
	 */
	private void getData() {

		try {
			selectedInstructional = PersistenceBean.getPersistedAreaList(
					"trial1" + PersistenceBean.getCurrentId(context), context);
			open = getIntent().getBooleanExtra("open", false);
			currentIntent = PersistenceBean.getExistingIntent(
					PersistenceBean.getCurrentId(context), context);

			for (CharSequence cs : selectedInstructional) {
				trial1Texts.add(cs.toString());
			}
		} catch (Exception e) {
			Log.e("ATGUIDE", "Error retrieving data  firsttrial->getData() "
					+ e);
		}
	}

	/**
	 * Allows a pop-up calendar on click of EditTextView of the Date
	 */
	public void setDatePickListener() {

		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,

			int dayOfMonth) {

				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateLabel();
			}

		};

		datePick.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				DatePickerDialog dialog = new DatePickerDialog(
						RevisitFirstTrial.this, date, myCalendar
								.get(Calendar.YEAR), myCalendar
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
		String myFormat = "MM/dd/yy";
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		datePick.setText(sdf.format(myCalendar.getTime()));
	}

	/**
	 * Provide the onclick listener to the special AIM Navigator AT
	 * 
	 * @return Listener to special AT
	 */
	public OnClickListener getExplorerATListener() {

		OnClickListener atL = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Remove Previous Listeners if set
				EditText atname = (EditText) findViewById(R.id.atTriedEdit);
				atname.removeTextChangedListener(_ATNameWatcher);
				// atname.setText("Choose AT");//Default
				EditText participantView = (EditText) findViewById(R.id.participants);
				participantView.removeTextChangedListener(participantsWatcher);
				// participantView.setText("");//
				EditText dateView = (EditText) findViewById(R.id.date);
				dateView.removeTextChangedListener(trialDateWatcher);
				// AT View
				TextView aTView = (TextView) v;
				CharSequence atName = trimATName((String) aTView.getText());
				clickedId = v.getId();
				// Parent
				LinearLayout parent = (LinearLayout) v.getParent().getParent();
				TextView area = (TextView) parent.getChildAt(0);
				CharSequence areaText = area.getText();
				TextView taskView = (TextView) ((LinearLayout) v.getParent())
						.getChildAt(0);
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
				setSpecialATListener();
				// setAddATListener(v);
				// setDeleteATListener(v);
			}
		};
		return atL;

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

				EditText atname = (EditText) findViewById(R.id.atTriedEdit);
				atname.removeTextChangedListener(_ATNameWatcher);

				EditText ataction = (EditText) findViewById(R.id.at);
				ataction.removeTextChangedListener(_ActionToWatcher);
				// atname.setText("Choose AT");//Default
				EditText participantView = (EditText) findViewById(R.id.participants);
				participantView.removeTextChangedListener(participantsWatcher);
				// participantView.setText("");//
				EditText dateView = (EditText) findViewById(R.id.date);
				dateView.removeTextChangedListener(trialDateWatcher);

				// AT View
				TextView aTView = (TextView) v;
				CharSequence atName = trimATName((String) aTView.getText());
				clickedId = v.getId();
				// Parent
				LinearLayout parent = (LinearLayout) v.getParent().getParent();
				TextView area = (TextView) parent.getChildAt(0);
				CharSequence areaText = area.getText();
				TextView taskView = (TextView) ((LinearLayout) v.getParent())
						.getChildAt(0);
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
				setATListener();
				// setAddATListener(v);
				// setDeleteATListener(v);
			}
		};
		return atL;
	}

	/**
	 * Sets the given AT to the view.
	 * 
	 * @param at
	 *            assitive technology object to be set to view
	 * @param currentClicked
	 *            view selected on left panel
	 */
	public void setAtToView(final AT at, View currentClicked) {
		/*
		 * Requires : the AT Listener to the change in participants clicked
		 * view. Modifies: Current view and this. Effects: Sets the given AT to
		 * the view.
		 */

		setATNameListener(at, currentClicked);
		setPartcipantListener(at);
		setCompletionDateListener(at);
		setATActionListener(at, currentClicked);
		((TextView) findViewById(R.id.areatitle)).setText(at
				.getInstructionalArea());
		((TextView) findViewById(R.id.taskname)).setText(at.task);
		((EditText) findViewById(R.id.atTriedEdit)).setText(at.getATName());

		((EditText) findViewById(R.id.at)).setText(at.trial1Action);
		((EditText) findViewById(R.id.participants)).setText(at.trial1Persons);
		((EditText) findViewById(R.id.date)).setText(at.getFirstTrialDate());
		RadioGroup solutions = (RadioGroup) findViewById(R.id.solutionradiogroup);

		solutions
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						RadioButton checkedSolution = (RadioButton) group
								.findViewById(checkedId);
						boolean isChecked = checkedSolution.isChecked();

						if (isChecked && (checkedId == R.id.solutionyes)) {
							// t.solutions = true;
							View action = (View) findViewById(R.id.trialActionLayout);
							action.setVisibility(View.INVISIBLE);
							View personLayout = (View) findViewById(R.id.actionPersonLayout);
							personLayout.setVisibility(View.INVISIBLE);
							at.solutionWorking = true;
							View dateLayout = (View) findViewById(R.id.actionCompletionLayout);
							dateLayout.setVisibility(View.INVISIBLE);

						} else if (isChecked && (checkedId == R.id.solutionno)) {
							// t.solutions = false;
							// make next fields visible
							View action = (View) findViewById(R.id.trialActionLayout);
							action.setVisibility(View.VISIBLE);
							setChildrenVisibility((LinearLayout) action,
									View.VISIBLE);
							View personLayout = (View) findViewById(R.id.actionPersonLayout);
							personLayout.setVisibility(View.VISIBLE);
							setChildrenVisibility((LinearLayout) personLayout,
									View.VISIBLE);
							at.solutionWorking = false;
							View dateLayout = (View) findViewById(R.id.actionCompletionLayout);
							dateLayout.setVisibility(View.VISIBLE);
							setChildrenVisibility((LinearLayout) dateLayout,
									View.VISIBLE);
						}

					}
				});

		if (at.solutionWorking) {
			((RadioButton) solutions.findViewById(R.id.solutionyes))
					.setChecked(true);
			View action = (View) findViewById(R.id.trialActionLayout);
			action.setVisibility(View.INVISIBLE);
			View personLayout = (View) findViewById(R.id.actionPersonLayout);
			personLayout.setVisibility(View.INVISIBLE);

			View dateLayout = (View) findViewById(R.id.actionCompletionLayout);
			dateLayout.setVisibility(View.INVISIBLE);
		} else {
			((RadioButton) solutions.findViewById(R.id.solutionno))
					.setChecked(true);

		}

	}

	/**
	 * Recursive Definition to set Visibility to all children of Linear Layout
	 * 
	 * @param parent
	 * @param visibility
	 */
	public void setChildrenVisibility(LinearLayout parent, int visibility) {
		int totalChildren = parent.getChildCount();
		for (int childNum = 0; childNum < totalChildren; childNum++) {
			View currentView = parent.getChildAt(childNum);
			currentView.setVisibility(visibility);
			if (currentView instanceof LinearLayout) {
				setChildrenVisibility((LinearLayout) currentView, visibility);
			}
			currentView.setVisibility(visibility);

		}

	}

	/**
	 * Validate solutions for second trial
	 */
	private void validateSolutions() {
		// Abstract function:
		// Get all area
		// Get each task
		// Add the solution not working to separate list
		// save the list
		// For all selected / persisted Area
		trial2 = false;

		for (Area checkArea : areaList) {
			// For each task in this area
			for (Task task : checkArea.tasks) {
				// If any of the strategies in current task are not working
				if (!task.solutions) {

					for (AT at : task.ats) {
						if (!at.solutionWorking) {
							trial2List.add(checkArea);
							trial2TextList.add("" + checkArea.getAreaName());
							trial2 = true;
						}
					}

				}
			}

		}

	}

	/**
	 * Listener to the change in AT Name
	 * 
	 * @param at
	 *            Assistive Technology type instance
	 */
	public void setATNameListener(final AT at, final View currentClicked) {
		EditText atname = (EditText) findViewById(R.id.atTriedEdit);
		atname.removeTextChangedListener(_ATNameWatcher);
		atname.setEnabled(false);
		atname.setFocusable(false);

		_ATNameWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				at.setATName(trimATName(s.toString()));
				((TextView) currentClicked).setText(trimATName(s.toString()));
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
	 * Listener to the action to be taken after this trial
	 * 
	 * @param at
	 *            the current AT
	 * @param currentClicked
	 *            the selected View
	 */
	public void setATActionListener(final AT at, final View currentClicked) {
		EditText atname = (EditText) findViewById(R.id.at);
		atname.removeTextChangedListener(_ActionToWatcher);
		// atname.setEnabled(false);
		// atname.setFocusable(false);

		_ActionToWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				at.trial1Action = (trimATName(s.toString()));

				// ((TextView)
				// currentClicked).setText(trimATName(s.toString()));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};

		atname.addTextChangedListener(_ActionToWatcher);
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
				at.trial1Persons = (s.toString());
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
				at.trial1CompletionDate = (s.toString());
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
	 * Sets the Listener to the ADD New AT Button
	 */
	public void setAddATListener(final View op) {

		try {
			// ADD Listener
			OnClickListener addCLick = new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					EditText atname = (EditText) findViewById(R.id.at);
					atname.removeTextChangedListener(_ATNameWatcher);
					EditText participantView = (EditText) findViewById(R.id.participants);
					participantView
							.removeTextChangedListener(participantsWatcher);
					EditText dateView = (EditText) findViewById(R.id.date);
					dateView.removeTextChangedListener(trialDateWatcher);
					LinearLayout taskLayout = (LinearLayout) op.getParent();
					TextView assistiveTech = new TextView(context);
					assistiveTech.setPadding(25, 0, 0, 0);
					assistiveTech.setText("Choose AT");
					assistiveTech.setTextColor(Color.BLACK);
					assistiveTech.setId(id++);
					LinearLayout areaLayout = (LinearLayout) taskLayout
							.getParent();
					String areaText = ((TextView) areaLayout.getChildAt(0))
							.getText().toString();
					Area areaobj = getAreaByName(areaText);
					Task t = areaobj.getTaskById(taskLayout.getChildAt(0)
							.getId());
					AT at = new AT();
					at.task = t.taskname;
					at.ATName = "Choose AT";
					at.instructionalArea = areaText;
					at.id = assistiveTech.getId();
					t.ats.add(at);
					setAtToView(at, v);
					assistiveTech.setOnClickListener(getATListener());
					Log.d("ATGuide", "Area Text: " + areaText);
					Log.d("ATGuide", "Area Text: " + areaText);
					taskLayout.addView(assistiveTech);
					assistiveTech.callOnClick();
				}
			};
			((Button) findViewById(R.id.addnewat)).setOnClickListener(addCLick);
		} catch (ClassCastException e) {
			Log.e("ATGuide", "" + e);
		} catch (Exception e) {
			Log.e("ATGuide", "" + e);
		}
	}

	/**
	 * Deletes the AT from logic and View
	 * 
	 * @param op
	 */
	public void setDeleteATListener(final View op) {
		// Requires: current selected AT
		// Modifies: this, current view, area and task
		// Effects: Deletes the AT from logic and View
		try {
			OnClickListener addCLick = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Remove All listeners
					EditText atname = (EditText) findViewById(R.id.at);
					atname.removeTextChangedListener(_ATNameWatcher);
					EditText participantView = (EditText) findViewById(R.id.participants);
					participantView
							.removeTextChangedListener(participantsWatcher);
					EditText dateView = (EditText) findViewById(R.id.date);
					dateView.removeTextChangedListener(trialDateWatcher);
					// Get Layouts
					LinearLayout taskLayout = (LinearLayout) op.getParent();
					TextView assistiveTech = (TextView) op;
					LinearLayout areaLayout = (LinearLayout) taskLayout
							.getParent();
					String areaText = ((TextView) areaLayout.getChildAt(0))
							.getText().toString();
					// If deletion invalid
					if (taskLayout.getChildCount() <= 2) {
						Toast.makeText(context,
								"At least one AT is required for a task",
								Toast.LENGTH_SHORT).show();
					} else {// getObjects
						Area areaobj = getAreaByName(areaText);
						Task t = areaobj.getTaskById(taskLayout.getChildAt(0)
								.getId());
						// Delete from Logics
						t.ats.remove(t.getATById(assistiveTech.getId()));
						// Delete from view
						taskLayout.removeView(assistiveTech);
						// Select other AT
						taskLayout.getChildAt(1).callOnClick();
					}
				}
			};
			((Button) findViewById(R.id.deleteat)).setOnClickListener(addCLick);
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
	 * 
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

	public String trimATName(String atName) {
		if (atName.contains("(e.g.") || atName.contains("( e.g.")) {
			return atName.replaceFirst("[(][e][.][g][.](.+?)[)]", " ");
		}
		return atName;
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

	public void log(String string) {
		Log.d("ATGUIDE", string);
	}

	public void toast(String string) {
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT)
				.show();
	}

}
