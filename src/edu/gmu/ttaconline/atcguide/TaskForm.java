package edu.gmu.ttaconline.atcguide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import com.commonsware.cwac.merge.MergeAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TaskForm extends Activity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TAG = "ATGUIDE";
	Intent currentIntent;
	Intent persisted;
	Context context;
	int id = 3000;
	int strategyRowid = 99000;
	ArrayList<Integer> areaIds = new ArrayList<Integer>();
	int clickedId = id;
	LayoutInflater inflater;
	ArrayList<CharSequence> selectedInstructional;
	ArrayList<String> selectedList;
	ArrayList<Area> areasList = new ArrayList<Area>();
	MergeAdapter merge = new MergeAdapter();
	TextWatcher taskWatcher;
	Area currentSelection = null;
	TextView currentText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_form);
		currentIntent = getIntent();
		context = getApplicationContext();
		inflater = getLayoutInflater();
		placeArea();
		LinearLayout first = (LinearLayout) merge.getItem(0);
		if (first.getChildAt(0) != null) {
			first.getChildAt(0).callOnClick();
		} else
			first.callOnClick();
		// Place task
		// Generate task form
		// Fill task form
		addStrategyListener(R.id.strategy0);// to first strategy
		addPlusButtonListener();
		setNextListener();
	}

	private void setNextListener() {
		Button next=(Button)findViewById(R.id.nextbutton);
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

					for (Area area : areasList) {
						Log.d(TAG, "New Area and tasks: "+area.getAreaName());
						area.logTasks();
					}
				
			}
		});
		
	}

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
		final TextView taskview = (TextView) v;
		Button deletetask = (Button) findViewById(R.id.deletetask);
		deletetask.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// Toast.makeText(context, "delete "+clickedId,
				// Toast.LENGTH_SHORT).show();
				LinearLayout area = ((LinearLayout) findViewById(clickedId)
						.getParent());

				if (area.getChildCount() > 2) {
					area.removeView((View) area.findViewById(clickedId));
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

				((LinearLayout) findViewById(R.id.strategylayout)).addView(row);
				ImageButton delete = (ImageButton) row
						.findViewById(R.id.deletethisstrategy);
				delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						((LinearLayout) findViewById(R.id.strategylayout))
								.removeView(row.findViewById(row.getId()));
					}
				});
			}
		});
	}

	private void addStrategyListener(int id) {
		EditText strategy = (EditText) findViewById(id);
		strategy.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				Toast.makeText(context, "Strategy added", Toast.LENGTH_SHORT)
						.show();
			}
		});

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
		selectedInstructional = currentIntent
				.getCharSequenceArrayListExtra("selectedareas");
		selectedList = new ArrayList<String>();
		for (CharSequence selected : selectedInstructional) {
			selectedList.add((String) selected);
			Log.d(TAG, " " + selected);
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
		if(areaname!=null)
		for (Area area : areasList) {
			if (area != null && area.getAreaName().trim().equalsIgnoreCase(areaname.toString().trim())) {
				return area;
			}
		}
		return null;
	}

	private void placeArea() {
		getData();
		LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ListView instructional = (ListView) findViewById(R.id.instructionalAreasList);
		for (CharSequence areaText : selectedInstructional) {
			LinearLayout v = (LinearLayout) inflater.inflate(
					R.layout.areataskrow, null);
			v.setId(id++);
			 TextView area = (TextView) v.findViewById(R.id.areatextview);
			// area.setBackground(getResources().getDrawable(R.drawable.textviewback));
			 area.setText(areaText);
			// area.setSelected(true);
			 area.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_START);
			 area.setLayoutParams(textViewParams);
			 area.setId(id++);
			// ((View) area.getParent()).setId(id++);
			// LinearLayout visible= (LinearLayout)v;
			// visible.addView(new TextView(context));
			 
			 Area areaObject = new Area((String) areaText);
			// areaObject.setAreaName((String) areaText);
			 areaObject.setParentId(area.getId());
			 	
			 TextView task = new TextView(context);
			 task.setText("");
			// TODO: Enter task name is in intent

			task.setLayoutParams(textViewParams);
			task.setId(id++);
			 	//Bean Storage
				Task taskObject=new Task();
			 	taskObject.setAreaname(areaObject.getAreaName());
			 	taskObject.taskid=(task.getId());
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

					 Area areaobj= getAreaById(area.getId());
					 final Task t= areaobj.getTaskById(v.getId());
					 t.setTaskname(taskviewText.toString());

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
								t.setTaskname(s.toString());
								// Toast.makeText(context, "Editable text:"+s,
								// Toast.LENGTH_SHORT).show();
								
							} catch (Exception e) {
								Log.e(TAG, "ex: " + e.getMessage());
							}
						}
					});

				}
			});

			v.addView(task);
			area.setOnClickListener(new View.OnClickListener() {
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
						final int parentid = area.getId();
						area.getChildAt(1).callOnClick();
						// EditText title = (EditText)
						// findViewById(R.id.taskname);
						// title.addTextChangedListener(taskWatcher);
						Button addTask = (Button) findViewById(R.id.addnewtask);
						addTask.setOnClickListener(new View.OnClickListener() {

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

	public void clickThisArea(int currentId) {
		View v = findViewById(currentId);
		v.callOnClick();
	}

	public void highlightThis(View tv) {

		ListView lv = (ListView) findViewById(R.id.instructionalAreasList);
		MergeAdapter m = (MergeAdapter) lv.getAdapter();
		m.getCount();
		//
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
		TextView currentClicked = (TextView) findViewById(clickedId);
		// FIND PARENT
		LinearLayout parent = (LinearLayout) currentClicked.getParent();
		//get area id
		//get area object
		
		// Create empty View
		// Current Task
		TextView area = (TextView) parent.getChildAt(0);
		CharSequence areaText = area.getText();
		((TextView) findViewById(R.id.tasktitle)).setText(areaText);
		
		
		
		TextView tv = new TextView(context);
		tv.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tv.setId(id++);
		tv.setTextColor(Color.BLACK);
		tv.setPadding(15, 0, 0, 0);
		Area areaObject= getAreaByName(areaText);
		
		final Task t= new Task();
		t.setTaskname(tv.getText().toString());
		t.taskid=tv.getId();
		areaObject.addTask(t);
		
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView taskview = (TextView) v;
				CharSequence taskviewText = taskview.getText();
				clickedId = v.getId();
				highlightThis(v);
				EditText taskname = (EditText) findViewById(R.id.taskname);
				taskname.setText(taskviewText);
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
							t.setTaskname(s.toString());
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
		// Thats it
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
