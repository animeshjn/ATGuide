package edu.gmu.ttaconline.atcguide;

import java.io.Serializable;
import java.util.ArrayList;

import com.commonsware.cwac.merge.MergeAdapter;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TaskForm extends Activity implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Log Tag
	 */
	private static final String TAG = "ATGUIDE";
	Intent currentIntent;
	Intent persisted;
	String studentid = "";
	Context context;
	TextWatcher watcher[] = new TextWatcher[1000];
	boolean open = false;
	static int id = 3000;
	static int strategyRowid = 0;// to 20
	ArrayList<Integer> areaIds = new ArrayList<Integer>();

	static int clickedId = id;
	LayoutInflater inflater;
	boolean trial1 = false;
	ArrayList<CharSequence> selectedInstructional;
	ArrayList<String> selectedList;
	ArrayList<String> trial1TextList = new ArrayList<String>();
	ArrayList<Area> areasList = new ArrayList<Area>();
	ArrayList<Area> trial1List = new ArrayList<Area>();;
	MergeAdapter merge = new MergeAdapter();
	TextWatcher taskWatcher;
	Area currentSelection = null;
	TextView currentText;
	Activity activity;
	//private OnClickListener taskListener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);
		try {
			setCustomActionBar();
			setContentView(R.layout.activity_task_form);
			// get from db
			activity = this;
			context = getApplicationContext();
			
			try {
				currentIntent = getIntentFromDb();// getIntent();//getIntentFromDb();}
			} catch (Exception e) {
				Log.d("ATGuide", "Exception getting Intent from Db" + e);
			}

			studentid = currentIntent.getStringExtra("studentid");
			currentIntent.setData(null);
			inflater = getLayoutInflater();
			@SuppressWarnings("unused")
			int areaListSize = 0;
			try {
				areaListSize = PersistenceBean.getPersistedAreaList(
						PersistenceBean.getCurrentId(context), context).size();
			} catch (Exception e) {
				Log.d("ATGuide", "Exception in AreaList size()");
			}
			// trial for any case ! using current intent
			getData();
			if (!currentIntent.getBooleanExtra("open", false))
				try {
					placeArea();
				} catch (Exception e) {
					Log.e("ATGUIDE",
							"Exception in place Area Task form"
									+ e.getMessage());
				}
			// if it is new
			else {
			 try {
			 placeAreaFromDB();
			 } catch (Exception e) {
			 Log.e("ATGUIDE",
			 "Exception in place Area from DataBase Task form"
			 + e.getMessage());
			 }
			 }
			// retrieveArea()if it is old
			LinearLayout first = (LinearLayout) merge.getItem(0);
			if (first.getChildAt(0) != null) {
				first.getChildAt(0).callOnClick();
			} else
				first.callOnClick();

			addPlusButtonListener();
			setNextListener();
			setLogListener();
			validateSolutions();
		} catch (Exception unknown) {
			Log.e("ATGUIDE",
					"Exception Unknown in Task Form" + unknown.getMessage());
			Toast.makeText(context, "Exception" + unknown, Toast.LENGTH_SHORT)
					.show();
		}
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
	 * Checks if first trial is required for any tasks, identifies the tasks for
	 * first trial
	 */
	private void validateSolutions() {
		// Abstract function:
		// Get all area
		// Get each task
		// Add the solution not working to separate list
		// save the list
		// For all selected / persisted Area
		trial1 = false;

		for (Area checkArea : areasList) {
			// For each task in this area
			for (Task task : checkArea.tasks) {
				// If any of the strategies in current task are not working
				if (!task.solutions) {
					trial1List.add(checkArea);
					trial1TextList.add("" + checkArea.getAreaName());
					trial1 = true;
				}
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// Save Data

		super.onBackPressed();
	}

	/**
	 * Intent From DB
	 * 
	 * @return Intent fetched from database
	 */
	private Intent getIntentFromDb() {
		return PersistenceBean.getExistingIntent(
				PersistenceBean.getCurrentId(getApplicationContext()),
				getApplicationContext());
	}

	/**
	 * Sets listener for the click of next button of this view
	 * */
	private void setNextListener() {
		Button next = (Button) findViewById(R.id.nextbutton);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			
				PersistenceBean
						.persistAreaObject(areasList, studentid, context);
				
				PersistenceBean.persistIntent(
						currentIntent.getStringExtra("studentid"),
						currentIntent, context);
				validateSolutions();
				if (trial1) {
					// Alert About trial 1
					AlertDialog.Builder info = new AlertDialog.Builder(activity);
					info.setMessage(getResources()
							.getString(R.string.trial1nav).toString());
					info.setCancelable(true);
					info.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// FWD to trial 1
									currentIntent.putExtra("trial1", trial1);

									currentIntent.setClass(context,
											FirstTrial.class);
									PersistenceBean.persistAreaObject(
											trial1List, "trial1" + studentid,
											context);
									Log.d("ATGUIDE",
											"student id on next persist"
													+ studentid);
									PersistenceBean.persistInstructionalAreas(
											"trial1" + studentid,
											trial1TextList, context);
									
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
									
									//startService(currentIntent);
									
									Thread t= new Thread(new PDFLogic());
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

	private void setLogListener() {

		Button log = (Button) findViewById(R.id.logbutton);
		log.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Data Logged on Console",
						Toast.LENGTH_SHORT).show();
				// Display DATA
				Log.d("ATGUIDE",
						"AREALIST SIZE before retrieving" + areasList.size());
				ArrayList<Area> persistedarea = PersistenceBean
						.getPersistedAreaObjects(studentid, context);
				Log.d("ATGUIDE",
						"starting looop areacount: " + persistedarea.size());
				for (Area area : persistedarea) {
					ATGuideLogger.LogIt(area);
					area.logTasks();
				}
			}
		});

	}

	/**
	 * Used to check if there is an empty strategy, used to avoid adding
	 * strategy in case of empty strategy
	 * 
	 * @param current
	 *            form view
	 * @return true if any of the strategy is empty
	 */
	public boolean isStrategyEmpty(View v) {
		LinearLayout main = (LinearLayout) findViewById(R.id.strategylayout);
		for (int i = 0; i < main.getChildCount(); i++) {
			View inside = main.getChildAt(i);
			if (inside instanceof LinearLayout) {
				View incepted = ((LinearLayout) inside).getChildAt(0);
				if (incepted instanceof EditText) {
					EditText strategy = (EditText) incepted;
					Editable s = strategy.getText();
					if (s == null) {
						return true;
					}
					if (s.toString().equals("")
							|| s.toString().trim().equals("")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void setDeleteTaskListener(View v) {

		// final TextView taskview = (TextView) v;
		Button deletetask = (Button) findViewById(R.id.deletetask);
		deletetask.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// Toast.makeText(context, "delete "+clickedId,
				// Toast.LENGTH_SHORT).show();
				LinearLayout area = ((LinearLayout) findViewById(clickedId)
						.getParent());

				if (area.getChildCount() > 2) {
					area.removeView(area.findViewById(clickedId));
					Area areaObj = getAreaById(((TextView) area.getChildAt(0))
							.getId());
					areaObj.tasks.remove(areaObj.getTaskById(clickedId));
					Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT)
							.show();

				} else
					Toast.makeText(context,
							"At least one task is required for an area",
							Toast.LENGTH_SHORT).show();
				area.getChildAt(0).callOnClick();
				// area.findViewById(clickedId);
				// merge.setActive(findViewById(clickedId), false);
				merge.notifyDataSetChanged();
			}
		});
	}

	private void addPlusButtonListener() {
		ImageButton plusButton = (ImageButton) findViewById(R.id.addstrategy);
		plusButton.setOnClickListener(new OnClickListener() {

			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				// Validate if current is not empty
				if (isStrategyEmpty(v)) {
					try {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								TaskForm.this);
						builder.setCancelable(true);
						builder.setTitle("Empty strategy");
						builder.setMessage("Please fill out the empty strategy first!");
						builder.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});

						builder.show();
					} catch (Exception unknown) {
						Log.e(TAG, "Ex !!" + unknown.getMessage());
					}
					return;
				}
				// Add new row
				final LinearLayout row = (LinearLayout) getLayoutInflater()
						.inflate(R.layout.strategyrow, null);
				row.setId(strategyRowid++);
				// TextView task = (TextView) findViewById(clickedId);
				TextView taskView = (TextView) findViewById(clickedId);
				TextView area = (TextView) (((LinearLayout) taskView
						.getParent()).getChildAt(0));
				Area localObj = getAreaByName(area.getText());
				final Task t = localObj.getTaskById(taskView.getId());
				EditText strategyText = (EditText) row
						.findViewById(R.id.strategyedittext);
				strategyText.setId(strategyRowid++);
				final int id = strategyText.getId();
				watcher[id] = new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						t.strategies.put("" + id, new String(s.toString()));
						Toast.makeText(context, "put:" + id, Toast.LENGTH_SHORT)
								.show();
					}
				};
				strategyText.addTextChangedListener(watcher[id]);
				((LinearLayout) findViewById(R.id.strategylayout)).addView(row);
				// -----------------------------------------------------------
				ImageButton delete = (ImageButton) row
						.findViewById(R.id.deletethisstrategy);
				delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						((LinearLayout) findViewById(R.id.strategylayout))
								.removeView(row.findViewById(row.getId()));
						t.strategies.remove(id + "");
					}
				});
			}
		});
	}

	void onDeleteFirstStrategy() {
		ImageButton deletefirststrategy = (ImageButton) findViewById(R.id.deletestrategy0);
		deletefirststrategy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "You cannot delete this strategy !",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void getData() {
		try {
			// selectedInstructional = currentIntent
			// .getCharSequenceArrayListExtra("selectedareas");

			selectedInstructional = PersistenceBean.getPersistedAreaList(
					PersistenceBean.getCurrentId(getApplicationContext()),
					getApplicationContext());
			areasList = PersistenceBean.getPersistedAreaObjects(
					PersistenceBean.getCurrentId(getApplicationContext()),
					getApplicationContext());
			Log.d("ATGUIDE", "areasListLength:" + areasList.size());
			selectedList = new ArrayList<String>();
			open = currentIntent.getBooleanExtra("open", false);

			for (CharSequence selected : selectedInstructional) {
				selectedList.add((String) selected);
				Log.d(TAG, " " + selected);
			}
		} catch (Exception e) {
		}
	}

	public Area getAreaById(int id) {
		for (Area area : areasList) {
			if (area != null && area.getParentId() == id) {
				return area;
			}
		}
		return null;
	}

	public Area getAreaByName(CharSequence areaname) {
		if (areaname != null)
			for (Area area : areasList) {
				if (area != null
						&& area.getAreaName().trim()
								.equalsIgnoreCase(areaname.toString().trim())) {
					return area;
				}
			}
		return null;
	}

	/**
	 * Places the existing area objects to the view
	 */
	@SuppressLint("InflateParams")
	private void placeExistingView() {

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
			for (Area area : areasList) {
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
						// assistiveTech.setOnClickListener(//getATListener());
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
					"Error retrieving data placeArea() \n" + e);
		}

	}

	/**
	 * Places area and task views dynamically. Sets all the changes and
	 * listeners. Sets the listener to the task views, and buttons. Implements
	 * the call for implementing new strategies and new tasks.
	 **/
	@SuppressLint("InflateParams")
	private void placeArea() {

		getData();
		LayoutParams textViewParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ListView instructional = (ListView) findViewById(R.id.instructionalAreasList);
		for (CharSequence areaText : selectedInstructional) {
			LinearLayout v = (LinearLayout) inflater.inflate(
					R.layout.areataskrow, null);
			v.setId(id++);
			TextView area = (TextView) v.findViewById(R.id.areatextview);
			// area.setBackground(getResources().getDrawable(R.drawable.textviewback));
			area.setText(areaText);
			area.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_START);
			area.setLayoutParams(textViewParams);
			area.setId(id++);
			// ((View) area.getParent()).setId(id++);
			// LinearLayout visible= (LinearLayout)v;
			// visible.addView(new TextView(context));

			Area areaObject;
			if (open)
				areaObject = getAreaByName(areaText);
			else {
				areaObject = new Area((String) areaText);
				// areaObject.setAreaName((String) areaText);
			}

			areaObject.setParentId(area.getId());
			TextView task;
			task = new TextView(context);
			task.setText("");
			if (!areaObject.tasks.isEmpty())

				// TODO: Enter task name is in intent
				task.setLayoutParams(textViewParams);
			task.setId(id++);
			// Bean Storage
			Task taskObject = new Task();
			taskObject.setAreaname(areaObject.getAreaName());
			taskObject.taskid = (task.getId());
			areaObject.tasks.add(taskObject);
			areasList.add(areaObject);
			task.setPadding(15, 0, 0, 0);
			task.setTextColor(Color.BLACK);
			task.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					TextView taskview = (TextView) v;
					CharSequence taskviewText = taskview.getText();
					clickedId = v.getId();
					setDeleteTaskListener(v);
					highlightThis(v);
					LinearLayout parent = (LinearLayout) v.getParent();
					TextView area = (TextView) parent.getChildAt(0);
					CharSequence areaText = area.getText();
					Area areaobj = getAreaById(area.getId());
					Task t = areaobj.getTaskById(v.getId());
					t.setTaskname(taskviewText.toString());
					// TODO: CODE HERE TO ADD STRATEGIES
					LinearLayout strategyLayout = (LinearLayout) findViewById(R.id.strategylayout);
					strategyLayout.removeAllViews();
					final LinearLayout row0 = (LinearLayout) getLayoutInflater()
							.inflate(R.layout.strategyrow, null);
					row0.setId(0);
					strategyLayout.addView(row0);
					EditText strategy = (EditText) row0
							.findViewById(R.id.strategyedittext);
					if (strategy == null)
						Toast.makeText(context, "Strategy is null",
								Toast.LENGTH_SHORT).show();
					// Insert strategies
					if (t.strategies.keySet() != null
							&& t.strategies.get("0") != null
							&& t.strategies.get("0") != "")
						strategy.setText(t.strategies.get("0"));
					else
						strategy.setText("");
					if (t.strategies.keySet().size() > 0) {// Loop through all
															// of them
						strategyLayout.removeAllViews(); // instead of finding
															// and removing text

						// change listeners, just start over for every click
						// improved efficiency O(n)

						int i = -1;
						// for each of the strategy in the task object
						for (final String key : t.strategies.keySet()) {
							// Toast.makeText(context,
							// t.strategies.keySet().toString() + " in loop " +
							// t.strategies.keySet().size(),
							// Toast.LENGTH_SHORT).show();
							i++;
							final int id = i;
							// get a new row
							final LinearLayout row = (LinearLayout) getLayoutInflater()
									.inflate(R.layout.strategyrow, null);
							// set id
							row.setId(id);
							EditText strategyText = (EditText) row
									.findViewById(R.id.strategyedittext);
							if (t.strategies.keySet() != null
									&& t.strategies.get(key) != null
									&& t.strategies.get(key) != "")
								strategyText.setText(t.strategies.get(key));
							else
								strategyText.setText("");
							watcher[id] = new TextWatcher() {
								@Override
								public void onTextChanged(CharSequence s,
										int start, int before, int count) {
								}

								@Override
								public void beforeTextChanged(CharSequence s,
										int start, int count, int after) {
								}

								@Override
								public void afterTextChanged(Editable s) {
									View v1 = (View) findViewById(clickedId);
									LinearLayout parent = (LinearLayout) v1
											.getParent();
									TextView area = (TextView) parent
											.getChildAt(0);
									Area areaobj = getAreaById(area.getId());
									Task t = areaobj.getTaskById(clickedId);
									t.strategies.remove(id + "");
									t.strategies.put(id + "",
											new String(s.toString()));
								}
							};
							strategyText.addTextChangedListener(watcher[id]);
							((LinearLayout) findViewById(R.id.strategylayout))
									.addView(row);
							ImageButton delete = (ImageButton) row
									.findViewById(R.id.deletethisstrategy);
							delete.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if (((LinearLayout) v.getParent()
											.getParent()).getChildCount() > 1) {
										((LinearLayout) findViewById(R.id.strategylayout))
												.removeView(row
														.findViewById(row
																.getId()));
										View v1 = (View) findViewById(clickedId);
										LinearLayout parent = (LinearLayout) v1
												.getParent();
										TextView area = (TextView) parent
												.getChildAt(0);
										Area areaobj = getAreaById(area.getId());
										Task t = areaobj.getTaskById(clickedId);
										t.strategies.remove(id + "");
										Toast.makeText(context,
												"Strategy deleted",
												Toast.LENGTH_SHORT).show();

									} else
										Toast.makeText(
												context,
												"At least one strategy is required",
												Toast.LENGTH_SHORT).show();
								}
							});
							// add accordingly
						}
					}

					watcher[0] = new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
						}

						@Override
						public void afterTextChanged(Editable s) {
							View v1 = (View) findViewById(clickedId);
							LinearLayout parent = (LinearLayout) v1.getParent();
							TextView area = (TextView) parent.getChildAt(0);
							Area areaobj = getAreaById(area.getId());
							Task t = areaobj.getTaskById(clickedId);
							t.strategies.remove("0");
							t.strategies.put("0", new String(s.toString()));
						}
					};
					strategy.addTextChangedListener(watcher[0]);
					((TextView) findViewById(R.id.tasktitle)).setText(areaText);
					EditText taskname = (EditText) findViewById(R.id.taskname);
					taskname.setText(taskviewText);
					// taskname.callOnClick();
					taskname.requestFocus();
					taskname.removeTextChangedListener(taskWatcher);
					taskWatcher = new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
							// TODO Auto-generated method stub
						}

						@Override
						public void afterTextChanged(Editable s) {
							// TODO Auto-generated method stub
							try {
								// Toast.makeText(context, "Text Changed",
								// Toast.LENGTH_SHORT).show();
								TextView task = (TextView) findViewById(clickedId);
								task.setText(s);
								View v1 = (View) findViewById(clickedId);
								LinearLayout parent = (LinearLayout) v1
										.getParent();
								TextView area = (TextView) parent.getChildAt(0);
								Area areaobj = getAreaById(area.getId());
								Task t = areaobj.getTaskById(clickedId);
								t.setTaskname(s.toString());
								// Toast.makeText(context, "Editable text:"+s,
								// Toast.LENGTH_SHORT).show();
							} catch (Exception e) {
								Log.e(TAG, "ex: " + e.getMessage());
							}
						}
					};
					taskname.addTextChangedListener(taskWatcher);
					RadioGroup solutions = (RadioGroup) findViewById(R.id.solutionradiogroup);
					if (t.solutions) {
						((RadioButton) solutions.findViewById(R.id.solutionyes))
								.setChecked(true);
					} else {
						((RadioButton) solutions.findViewById(R.id.solutionno))
								.setChecked(true);

					}
					solutions
							.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
								@Override
								public void onCheckedChanged(RadioGroup group,
										int checkedId) {
									RadioButton checkedSolution = (RadioButton) group
											.findViewById(checkedId);
									boolean isChecked = checkedSolution
											.isChecked();
									View v1 = (View) findViewById(clickedId);
									LinearLayout parent = (LinearLayout) v1
											.getParent();
									TextView area = (TextView) parent
											.getChildAt(0);
									Area areaobj = getAreaById(area.getId());
									Task t = areaobj.getTaskById(clickedId);
									if (isChecked
											&& (checkedId == R.id.solutionyes)) {
										t.solutions = true;
										Toast.makeText(context, "checked",
												Toast.LENGTH_SHORT).show();

									} else if (isChecked
											&& (checkedId == R.id.solutionno)) {
										t.solutions = false;
										Toast.makeText(context, "UN Checked",
												Toast.LENGTH_SHORT).show();
									}

								}
							});
				}
			});
			v.addView(task);
			area.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						TextView curr = (TextView) v;
						// currentText = curr;
						TextView areatitle = (TextView) findViewById(R.id.tasktitle);
						areatitle.setText(curr.getText());
						highlightThis((View) v.getParent());
						// set on edit listener
						LinearLayout area = (LinearLayout) v.getParent();
						// final int parentid = area.getId();
						area.getChildAt(1).callOnClick();
						// EditText title = (EditText)
						// findViewById(R.id.taskname);
						// title.addTextChangedListener(taskWatcher);
						Button addTask = (Button) findViewById(R.id.addnewtask);
						addTask.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Toast.makeText(context,
										"Add new task to this area",
										Toast.LENGTH_SHORT).show();
								onAddNewTask();
							}
						});
					} catch (Exception e) {
						Log.e("ATGUIDE", e.getMessage());
					}
				}
			});
			merge.addView(v);
			merge.setActive(v, true);
		}
		instructional.setAdapter(merge);
		// PersistenceBean.persistIntent(currentIntent.getStringExtra("studentid"),
		// currentIntent,context);
		// persisted=
		// PersistenceBean.getExistingIntent(currentIntent.getStringExtra("studentid"),
		// context);
		// Log.d("ATGUIDE","here: "+persisted.getStringExtra("studentid"));
	}

	@SuppressWarnings("unused")
	private OnClickListener getAreaOnclickListener() {

		return new OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					TextView curr = (TextView) v;
					Toast.makeText(context, "Area called" + clickedId,
							Toast.LENGTH_SHORT).show();
					// currentText = curr;
					TextView areatitle = (TextView) findViewById(R.id.tasktitle);
					areatitle.setText(curr.getText());
					highlightThis((View) v.getParent());
					// set on edit listener
					LinearLayout area = (LinearLayout) v.getParent();
					// final int parentid = area.getId();
					area.getChildAt(1).callOnClick();
					// EditText title = (EditText)
					// findViewById(R.id.taskname);
					// title.addTextChangedListener(taskWatcher);
					Button addTask = (Button) findViewById(R.id.addnewtask);
					addTask.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Toast.makeText(context,
									"Add new task to this area",
									Toast.LENGTH_SHORT).show();
							onAddNewTask();
						}
					});
				} catch (Exception e) {
					Log.e("ATGUIDE", e.getMessage());
				}
			}
		};

	}

	@SuppressLint("InflateParams")
	public void placeAreaFromDB() {

		//getData();
		
		LayoutParams textViewParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ListView instructional = (ListView) findViewById(R.id.instructionalAreasList);
		for (CharSequence areaText : selectedInstructional) {
			LinearLayout v = (LinearLayout) inflater.inflate(
					R.layout.areataskrow, null);
			v.setId(id++);
			TextView area = (TextView) v.findViewById(R.id.areatextview);
			// area.setBackground(getResources().getDrawable(R.drawable.textviewback));
			area.setText(areaText);
			area.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_START);
			area.setLayoutParams(textViewParams);
			area.setId(id++);
			// ((View) area.getParent()).setId(id++);
			// LinearLayout visible= (LinearLayout)v;
			// visible.addView(new TextView(context));
			Area areaObject;
			if (open) {
				areaObject = getAreaByName(areaText);
				area.setId(areaObject.getParentId());
			} else {
				areaObject = new Area((String) areaText);
				// areaObject.setAreaName((String) areaText);
				areaObject.setParentId(area.getId());
			}

			TextView task;
			task = new TextView(context);
			task.setText("");
			if (!areaObject.tasks.isEmpty()){
				for(Task t:areaObject.tasks){
					task.setLayoutParams(textViewParams);
					task = new TextView(context);
					task.setText(t.getTaskname());
					task.setId(t.taskid);
					task.setPadding(15, 0, 0, 0);
					task.setTextColor(Color.BLACK);
					task.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {


					TextView taskview = (TextView) v;
					CharSequence taskviewText = taskview.getText();
					clickedId = v.getId();
					setDeleteTaskListener(v);
					highlightThis(v);
					LinearLayout parent = (LinearLayout) v.getParent();
					TextView area = (TextView) parent.getChildAt(0);
					CharSequence areaText = area.getText();
					Area areaobj = getAreaById(area.getId());
					Task t = areaobj.getTaskById(v.getId());
					t.setTaskname(taskviewText.toString());
					// TODO: CODE HERE TO ADD STRATEGIES
					LinearLayout strategyLayout = (LinearLayout) findViewById(R.id.strategylayout);
					strategyLayout.removeAllViews();
					final LinearLayout row0 = (LinearLayout) getLayoutInflater()
							.inflate(R.layout.strategyrow, null);
					row0.setId(0);
					strategyLayout.addView(row0);
					EditText strategy = (EditText) row0
							.findViewById(R.id.strategyedittext);
					if (strategy == null)
						Toast.makeText(context, "Strategy is null",
								Toast.LENGTH_SHORT).show();
					// Insert strategies
					if (t.strategies.keySet() != null
							&& t.strategies.get("0") != null
							&& t.strategies.get("0") != "")
						strategy.setText(t.strategies.get("0"));
					else
						strategy.setText("");
					if (t.strategies.keySet().size() > 0) {// Loop through all
															// of them
						strategyLayout.removeAllViews(); // instead of finding
															// and removing text

						// change listeners, just start over for every click
						// improved efficiency O(n)

						int i = -1;
						// for each of the strategy in the task object
						for (final String key : t.strategies.keySet()) {
							// Toast.makeText(context,
							// t.strategies.keySet().toString() + " in loop " +
							// t.strategies.keySet().size(),
							// Toast.LENGTH_SHORT).show();
							i++;
							final int id = i;
							// get a new row
							final LinearLayout row = (LinearLayout) getLayoutInflater()
									.inflate(R.layout.strategyrow, null);
							// set id
							row.setId(id);
							EditText strategyText = (EditText) row
									.findViewById(R.id.strategyedittext);
							if (t.strategies.keySet() != null
									&& t.strategies.get(key) != null
									&& t.strategies.get(key) != "")
								strategyText.setText(t.strategies.get(key));
							else
								strategyText.setText("");
							watcher[id] = new TextWatcher() {
								@Override
								public void onTextChanged(CharSequence s,
										int start, int before, int count) {
								}

								@Override
								public void beforeTextChanged(CharSequence s,
										int start, int count, int after) {
								}

								@Override
								public void afterTextChanged(Editable s) {
									View v1 = (View) findViewById(clickedId);
									LinearLayout parent = (LinearLayout) v1
											.getParent();
									TextView area = (TextView) parent
											.getChildAt(0);
									Area areaobj = getAreaById(area.getId());
									Task t = areaobj.getTaskById(clickedId);
									t.strategies.remove(id + "");
									t.strategies.put(id + "",
											new String(s.toString()));
								}
							};
							strategyText.addTextChangedListener(watcher[id]);
							((LinearLayout) findViewById(R.id.strategylayout))
									.addView(row);
							ImageButton delete = (ImageButton) row
									.findViewById(R.id.deletethisstrategy);
							delete.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if (((LinearLayout) v.getParent()
											.getParent()).getChildCount() > 1) {
										((LinearLayout) findViewById(R.id.strategylayout))
												.removeView(row
														.findViewById(row
																.getId()));
										View v1 = (View) findViewById(clickedId);
										LinearLayout parent = (LinearLayout) v1
												.getParent();
										TextView area = (TextView) parent
												.getChildAt(0);
										Area areaobj = getAreaById(area.getId());
										Task t = areaobj.getTaskById(clickedId);
										t.strategies.remove(id + "");
										Toast.makeText(context,
												"Strategy deleted",
												Toast.LENGTH_SHORT).show();

									} else
										Toast.makeText(
												context,
												"At least one strategy is required",
												Toast.LENGTH_SHORT).show();
								}
							});
							// add accordingly
						}
					}

					watcher[0] = new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
						}

						@Override
						public void afterTextChanged(Editable s) {
							View v1 = (View) findViewById(clickedId);
							LinearLayout parent = (LinearLayout) v1.getParent();
							TextView area = (TextView) parent.getChildAt(0);
							Area areaobj = getAreaById(area.getId());
							Task t = areaobj.getTaskById(clickedId);
							t.strategies.remove("0");
							t.strategies.put("0", new String(s.toString()));
						}
					};
					strategy.addTextChangedListener(watcher[0]);
					((TextView) findViewById(R.id.tasktitle)).setText(areaText);
					EditText taskname = (EditText) findViewById(R.id.taskname);
					taskname.setText(taskviewText);
					// taskname.callOnClick();
					taskname.requestFocus();
					taskname.removeTextChangedListener(taskWatcher);
					taskWatcher = new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
							// TODO Auto-generated method stub
						}

						@Override
						public void afterTextChanged(Editable s) {
							// TODO Auto-generated method stub
							try {
								// Toast.makeText(context, "Text Changed",
								// Toast.LENGTH_SHORT).show();
								TextView task = (TextView) findViewById(clickedId);
								task.setText(s);
								View v1 = (View) findViewById(clickedId);
								LinearLayout parent = (LinearLayout) v1
										.getParent();
								TextView area = (TextView) parent.getChildAt(0);
								Area areaobj = getAreaById(area.getId());
								Task t = areaobj.getTaskById(clickedId);
								t.setTaskname(s.toString());
								// Toast.makeText(context, "Editable text:"+s,
								// Toast.LENGTH_SHORT).show();
							} catch (Exception e) {
								Log.e(TAG, "ex: " + e.getMessage());
							}
						}
					};
					taskname.addTextChangedListener(taskWatcher);
					RadioGroup solutions = (RadioGroup) findViewById(R.id.solutionradiogroup);
					if (t.solutions) {
						((RadioButton) solutions.findViewById(R.id.solutionyes))
								.setChecked(true);
					} else {
						((RadioButton) solutions.findViewById(R.id.solutionno))
								.setChecked(true);

					}
					solutions
							.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
								@Override
								public void onCheckedChanged(RadioGroup group,
										int checkedId) {
									RadioButton checkedSolution = (RadioButton) group
											.findViewById(checkedId);
									boolean isChecked = checkedSolution
											.isChecked();
									View v1 = (View) findViewById(clickedId);
									LinearLayout parent = (LinearLayout) v1
											.getParent();
									TextView area = (TextView) parent
											.getChildAt(0);
									Area areaobj = getAreaById(area.getId());
									Task t = areaobj.getTaskById(clickedId);
									if (isChecked
											&& (checkedId == R.id.solutionyes)) {
										t.solutions = true;
										Toast.makeText(context, "checked",
												Toast.LENGTH_SHORT).show();

									} else if (isChecked
											&& (checkedId == R.id.solutionno)) {
										t.solutions = false;
										Toast.makeText(context, "UN Checked",
												Toast.LENGTH_SHORT).show();
									}

								}
							});
				}
			});
					v.addView(task);
					
				}
				
			}
				// TODO: Enter task name is in intent
			//task.setLayoutParams(textViewParams);
			//task.setId(id++);
			// Bean Storage
			//Task taskObject = new Task();
			//taskObject.setAreaname(areaObject.getAreaName());
			//taskObject.taskid = (task.getId());
			//areaObject.tasks.add(taskObject);
			//areasList.add(areaObject);
//			task.setPadding(15, 0, 0, 0);
//			task.setTextColor(Color.BLACK);
//			task.setOnClickListener();
//			v.addView(task);
			area.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						TextView curr = (TextView) v;
						// currentText = curr;
						TextView areatitle = (TextView) findViewById(R.id.tasktitle);
						areatitle.setText(curr.getText());
						highlightThis((View) v.getParent());
						// set on edit listener
						LinearLayout area = (LinearLayout) v.getParent();
						// final int parentid = area.getId();
						area.getChildAt(1).callOnClick();
						// EditText title = (EditText)
						// findViewById(R.id.taskname);
						// title.addTextChangedListener(taskWatcher);
						Button addTask = (Button) findViewById(R.id.addnewtask);
						addTask.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Toast.makeText(context,
										"Add new task to this area",
										Toast.LENGTH_SHORT).show();
								onAddNewTask();
							}
						});
					} catch (Exception e) {
						Log.e("ATGUIDE", e.getMessage());
					}
				}
			});
			merge.addView(v);
			merge.setActive(v, true);
		}
		instructional.setAdapter(merge);

	}


	
	OnClickListener getTaskListener() {

		return new OnClickListener() {

			@SuppressLint("InflateParams") @Override
			public void onClick(View v) {
				Toast.makeText(context, "Task called " + clickedId,
						Toast.LENGTH_SHORT).show();

				TextView taskview = null;
				try {
					taskview = (TextView) v;
				} catch (Exception e) {

					Log.e("ATGUIDE", "Ex: " + e);
				}
				CharSequence taskviewText = taskview.getText();
				clickedId = v.getId();
				Toast.makeText(context, "" + clickedId, Toast.LENGTH_SHORT)
						.show();
				setDeleteTaskListener(v);
				highlightThis(v);
				LinearLayout parent = (LinearLayout) v.getParent();
				TextView area = (TextView) parent.getChildAt(0);
				CharSequence areaText = area.getText();
				Area areaobj = getAreaById(area.getId());
				Task t = areaobj.getTaskById(v.getId());
				t.setTaskname(taskviewText.toString());
				// TODO: CODE HERE TO ADD STRATEGIES
				LinearLayout strategyLayout = (LinearLayout) findViewById(R.id.strategylayout);
				strategyLayout.removeAllViews();
				final LinearLayout row0 = (LinearLayout) getLayoutInflater()
						.inflate(R.layout.strategyrow, null);
				row0.setId(0);
				strategyLayout.addView(row0);
				EditText strategy = (EditText) row0
						.findViewById(R.id.strategyedittext);
				if (strategy == null)
					Toast.makeText(context, "Strategy is null",
							Toast.LENGTH_SHORT).show();
				// Insert strategies
				if (t.strategies.keySet() != null
						&& t.strategies.get("0") != null
						&& t.strategies.get("0") != "")
					strategy.setText(t.strategies.get("0"));
				else
					strategy.setText("");
				if (t.strategies.keySet().size() > 0) {// Loop through all
														// of them
					strategyLayout.removeAllViews(); // instead of finding
														// and removing text

					// change listeners, just start over for every click
					// improved efficiency O(n)

					int i = -1;
					// for each of the strategy in the task object
					for (final String key : t.strategies.keySet()) {
						// Toast.makeText(context,
						// t.strategies.keySet().toString() + " in loop " +
						// t.strategies.keySet().size(),
						// Toast.LENGTH_SHORT).show();
						i++;
						final int id = i;
						// get a new row
						final LinearLayout row = (LinearLayout) getLayoutInflater()
								.inflate(R.layout.strategyrow, null);
						// set id
						row.setId(id);
						EditText strategyText = (EditText) row
								.findViewById(R.id.strategyedittext);
						if (t.strategies.keySet() != null
								&& t.strategies.get(key) != null
								&& t.strategies.get(key) != "")
							strategyText.setText(t.strategies.get(key));
						else
							strategyText.setText("");
						watcher[id] = new TextWatcher() {
							@Override
							public void onTextChanged(CharSequence s,
									int start, int before, int count) {
							}

							@Override
							public void beforeTextChanged(CharSequence s,
									int start, int count, int after) {
							}

							@Override
							public void afterTextChanged(Editable s) {
								View v1 = (View) findViewById(clickedId);
								LinearLayout parent = (LinearLayout) v1
										.getParent();
								TextView area = (TextView) parent.getChildAt(0);
								Area areaobj = getAreaById(area.getId());
								Task t = areaobj.getTaskById(clickedId);
								t.strategies.remove(id + "");
								t.strategies.put(id + "",
										new String(s.toString()));
							}
						};
						strategyText.addTextChangedListener(watcher[id]);
						((LinearLayout) findViewById(R.id.strategylayout))
								.addView(row);
						ImageButton delete = (ImageButton) row
								.findViewById(R.id.deletethisstrategy);
						delete.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (((LinearLayout) v.getParent().getParent())
										.getChildCount() > 1) {
									((LinearLayout) findViewById(R.id.strategylayout))
											.removeView(row.findViewById(row
													.getId()));
									View v1 = (View) findViewById(clickedId);
									LinearLayout parent = (LinearLayout) v1
											.getParent();
									TextView area = (TextView) parent
											.getChildAt(0);
									Area areaobj = getAreaById(area.getId());
									Task t = areaobj.getTaskById(clickedId);
									t.strategies.remove(id + "");
									Toast.makeText(context, "Strategy deleted",
											Toast.LENGTH_SHORT).show();

								} else
									Toast.makeText(
											context,
											"At least one strategy is required",
											Toast.LENGTH_SHORT).show();
							}
						});
						// add accordingly
					}
				}

				watcher[0] = new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						View v1 = (View) findViewById(clickedId);
						LinearLayout parent = (LinearLayout) v1.getParent();
						TextView area = (TextView) parent.getChildAt(0);
						Area areaobj = getAreaById(area.getId());
						Task t = areaobj.getTaskById(clickedId);
						t.strategies.remove("0");
						t.strategies.put("0", new String(s.toString()));
					}
				};
				strategy.addTextChangedListener(watcher[0]);
				((TextView) findViewById(R.id.tasktitle)).setText(areaText);
				EditText taskname = (EditText) findViewById(R.id.taskname);
				taskname.setText(taskviewText);
				// taskname.callOnClick();
				taskname.requestFocus();
				taskname.removeTextChangedListener(taskWatcher);
				taskWatcher = new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub
					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						try {
							// Toast.makeText(context, "Text Changed",
							// Toast.LENGTH_SHORT).show();
							TextView task = (TextView) findViewById(clickedId);
							task.setText(s);
							View v1 = (View) findViewById(clickedId);
							LinearLayout parent = (LinearLayout) v1.getParent();
							TextView area = (TextView) parent.getChildAt(0);
							Area areaobj = getAreaById(area.getId());
							Task t = areaobj.getTaskById(clickedId);
							t.setTaskname(s.toString());
							// Toast.makeText(context, "Editable text:"+s,
							// Toast.LENGTH_SHORT).show();

						} catch (Exception e) {
							Log.e(TAG, "ex: " + e.getMessage());
						}
					}
				};
				taskname.addTextChangedListener(taskWatcher);
				RadioGroup solutions = (RadioGroup) findViewById(R.id.solutionradiogroup);
				if (t.solutions) {
					((RadioButton) solutions.findViewById(R.id.solutionyes))
							.setChecked(true);
				} else {
					((RadioButton) solutions.findViewById(R.id.solutionno))
							.setChecked(true);

				}
				solutions
						.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(RadioGroup group,
									int checkedId) {
								RadioButton checkedSolution = (RadioButton) group
										.findViewById(checkedId);
								boolean isChecked = checkedSolution.isChecked();
								View v1 = (View) findViewById(clickedId);
								LinearLayout parent = (LinearLayout) v1
										.getParent();
								TextView area = (TextView) parent.getChildAt(0);
								Area areaobj = getAreaById(area.getId());
								Task t = areaobj.getTaskById(clickedId);
								if (isChecked
										&& (checkedId == R.id.solutionyes)) {
									t.solutions = true;
									Toast.makeText(context, "checked",
											Toast.LENGTH_SHORT).show();

								} else if (isChecked
										&& (checkedId == R.id.solutionno)) {
									t.solutions = false;
									Toast.makeText(context, "UN Checked",
											Toast.LENGTH_SHORT).show();
								}

							}
						});
			}
		};

	}

	/**
	 * Mark this Area as clicked now
	 * 
	 * @param currentId
	 *            to be clicked
	 */
	public void clickThisArea(int currentId) {
		View v = findViewById(currentId);

		v.callOnClick();
	}

	/**
	 * highlight this text view
	 * 
	 * @param tv
	 */
	public void highlightThis(View tv) {
		ListView lv = (ListView) findViewById(R.id.instructionalAreasList);
		MergeAdapter m = (MergeAdapter) lv.getAdapter();
		m.getCount();
		for (int c = 0; c <= m.getCount(); c++) {
			View tt = (View) m.getItem(c);
			if (tt != null && tt instanceof LinearLayout) {
				LinearLayout ll = (LinearLayout) tt;
				ll.setBackgroundResource(0);
				for (int i = 0; i < ll.getChildCount(); i++) {
					TextView tc = (TextView) ll.getChildAt(i);
					tc.setBackgroundResource(0);
				}
			}
		}
		tv.setBackground(getResources().getDrawable(R.drawable.highlighted));
		if (tv.getParent() instanceof LinearLayout)
			((LinearLayout) tv.getParent()).getChildAt(0).setBackgroundColor(
					Color.CYAN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_form, menu);
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

	public void onAddNewTask() {
		// AddNewTextView to current area
		// blank all text views
		// set ids++
		// set on click listener
		// store task to the object
		// FIND CURRENT CLICKED
		View currentClicked = (View) findViewById(clickedId);
		// FIND PARENT
		LinearLayout parent;
		parent = (LinearLayout) currentClicked.getParent();

		// get area id
		// get area object
		// Create empty View
		// Current Task
		TextView area = (TextView) parent.getChildAt(0);
		CharSequence areaText = area.getText();
		((TextView) findViewById(R.id.tasktitle)).setText(areaText);
		TextView tv = new TextView(context);
		tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		tv.setId(id++);
		tv.setTextColor(Color.BLACK);
		tv.setPadding(15, 0, 0, 0);
		Area areaObject = getAreaByName(areaText);
		final Task t = new Task();
		t.setTaskname(tv.getText().toString());
		t.taskid = tv.getId();
		RadioGroup solutions = (RadioGroup) findViewById(R.id.solutionradiogroup);
		if (t.solutions) {
			((RadioButton) solutions.findViewById(R.id.solutionyes))
					.setChecked(true);
		} else {
			((RadioButton) solutions.findViewById(R.id.solutionno))
					.setChecked(true);
		}
		solutions
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						RadioButton checkedSolution = (RadioButton) group
								.findViewById(checkedId);
						boolean isChecked = checkedSolution.isChecked();
						if (isChecked && (checkedId == R.id.solutionyes))
							t.solutions = true;
						else
							t.solutions = false;

					}
				});
		areaObject.addTask(t);
		// On click listener of the task
		tv.setOnClickListener(new OnClickListener() {
			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				TextView taskview = (TextView) v;
				CharSequence taskviewText = taskview.getText();
				clickedId = v.getId();
				setDeleteTaskListener(v);
				highlightThis(v);
				LinearLayout parent = (LinearLayout) v.getParent();
				TextView area = (TextView) parent.getChildAt(0);
				CharSequence areaText = area.getText();
				// Area Object is already created and is in the area list
				Area areaobj = getAreaById(area.getId());
				// Create task for this
				Task t = areaobj.getTaskById(v.getId());// ? what if task not
														// present
				Task t1 = new Task();
				t1.copyTask(t);
				t.setTaskname(taskviewText.toString());
				// TODO: CODE HERE TO ADD STRATEGIES
				LinearLayout strategyLayout = (LinearLayout) findViewById(R.id.strategylayout);
				// Remove All views
				strategyLayout.removeAllViews();
				final LinearLayout row0 = (LinearLayout) getLayoutInflater()
						.inflate(R.layout.strategyrow, null);
				row0.setId(0);
				strategyLayout.addView(row0);
				EditText strategy = (EditText) row0
						.findViewById(R.id.strategyedittext);

				if (strategy == null)
					Toast.makeText(context, "Strategy is null",
							Toast.LENGTH_SHORT).show();
				// try {
				// if (watcher[0] != null && strategy != null)
				// strategy.removeTextChangedListener(watcher[0]);
				// } catch (Exception e) {
				// Log.e(TAG, "Probable Null Pointer or index out of bound " +
				// e.getMessage());
				// }
				// Insert strategies
				if (t.strategies.keySet() != null
						&& t.strategies.get("0") != null
						&& t.strategies.get("0") != "")
					strategy.setText(t.strategies.get("0"));
				else
					strategy.setText("");
				if (t.strategies.keySet().size() > 0) {// Loop through all of
														// them
					strategyLayout.removeAllViews(); // instead of finding and
														// removing text
					// change listeners, just start over for every click
					// improved efficiency O(n)
					int i = -1;
					// for each of the strategy in the task object
					for (final String key : t.strategies.keySet()) {
						// Toast.makeText(context,
						// t.strategies.keySet().toString() + " in loop " +
						// t.strategies.keySet().size(),
						// Toast.LENGTH_SHORT).show();
						i++;
						final int id = i;
						// get a new row
						final LinearLayout row = (LinearLayout) getLayoutInflater()
								.inflate(R.layout.strategyrow, null);
						// set id
						row.setId(id);
						EditText strategyText = (EditText) row
								.findViewById(R.id.strategyedittext);
						if (t.strategies.keySet() != null
								&& t.strategies.get(key) != null
								&& t.strategies.get(key) != "")
							strategyText.setText(t.strategies.get(key));
						else
							strategyText.setText("");
						watcher[id] = new TextWatcher() {
							@Override
							public void onTextChanged(CharSequence s,
									int start, int before, int count) {
							}

							@Override
							public void beforeTextChanged(CharSequence s,
									int start, int count, int after) {
							}

							@Override
							public void afterTextChanged(Editable s) {
								View v = (View) findViewById(clickedId);
								LinearLayout parent = (LinearLayout) v
										.getParent();
								TextView area = (TextView) parent.getChildAt(0);
								Area areaObj = getAreaById(area.getId());
								Task t = areaObj.getTaskById(clickedId);

								t.strategies.remove(id + "");
								t.strategies.put(id + "",
										new String(s.toString()));
							}
						};
						// Strategy Text
						strategyText.addTextChangedListener(watcher[id]);
						((LinearLayout) findViewById(R.id.strategylayout))
								.addView(row);
						ImageButton delete = (ImageButton) row
								.findViewById(R.id.deletethisstrategy);
						delete.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (((LinearLayout) v.getParent().getParent())
										.getChildCount() > 1) {
									((LinearLayout) findViewById(R.id.strategylayout))
											.removeView(row.findViewById(row
													.getId()));
									View v1 = (View) findViewById(clickedId);
									LinearLayout parent = (LinearLayout) v1
											.getParent();
									TextView area = (TextView) parent
											.getChildAt(0);
									Area areaObj = getAreaById(area.getId());
									Task t = areaObj.getTaskById(clickedId);

									t.strategies.remove(id + "");
									Toast.makeText(context, "Strategy deleted",
											Toast.LENGTH_SHORT).show();
								} else
									Toast.makeText(
											context,
											"At least one strategy is required",
											Toast.LENGTH_SHORT).show();
							}
						});
						// add accordingly
					}
				}

				// First strategy
				watcher[0] = new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						View v1 = (View) findViewById(clickedId);
						LinearLayout parent = (LinearLayout) v1.getParent();
						TextView area = (TextView) parent.getChildAt(0);
						Area areaObj = getAreaById(area.getId());
						Task t = areaObj.getTaskById(clickedId);
						t.strategies.remove("0");
						t.strategies.put("0", new String(s.toString()));
					}
				};
				// Add listener to first strategy
				strategy.addTextChangedListener(watcher[0]);
				((TextView) findViewById(R.id.tasktitle)).setText(areaText);
				EditText taskname = (EditText) findViewById(R.id.taskname);
				taskname.setText(taskviewText);
				// taskname.callOnClick();
				taskname.requestFocus();
				taskname.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub
					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						try {
							// Toast.makeText(context, "Text Changed",
							// Toast.LENGTH_SHORT).show();
							TextView task = (TextView) findViewById(clickedId);
							task.setText(s);
							View v1 = (View) findViewById(clickedId);
							LinearLayout parent = (LinearLayout) v1.getParent();
							TextView area = (TextView) parent.getChildAt(0);
							Area areaObj = getAreaById(area.getId());
							Task t = areaObj.getTaskById(clickedId);
							t.setTaskname(task.getText().toString());
							// Toast.makeText(context, "Editable text:"+s,
							// Toast.LENGTH_SHORT).show();
						} catch (Exception e) {
							Log.e(TAG, "ex: " + e.getMessage());
						}
					}
				});

			}

		});

		// ADD EMPTY VIEW TO THIS
		parent.addView(tv);
		tv.callOnClick();
	}

	public class addTasklistener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// set text
			EditText et = (EditText) findViewById(R.id.taskname);
			et.setText(((TextView) v).getText());
			// strategies
			currentText = (TextView) v;
		}
	}
}
