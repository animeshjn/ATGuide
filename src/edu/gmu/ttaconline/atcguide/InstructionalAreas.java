package edu.gmu.ttaconline.atcguide;

import java.util.ArrayList;

import com.commonsware.cwac.merge.MergeAdapter;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Toast;



public class InstructionalAreas extends Activity {
	Intent currentIntent;
	Context context;
	ArrayList<String> selectedInstructionalAreas= new ArrayList<String>();
	CheckBoxBean instructionalCheck;
	String otherText="";
	boolean isSample=false;
	boolean otherSelected=false;
	MergeAdapter merge = new MergeAdapter();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		setContentView(R.layout.activity_instructional_areas);
		setCurrentIntent();		  //Retrieve current intent
		instructionalCheck=new CheckBoxBean();
		setInstuctionalAreas();  //set the view of instructional areas
		setNextListener();		 //Persist selection and go to next activity

	}

	
	private void setNextListener() {
		
		Button next= (Button) findViewById(R.id.nextbutton);
		next.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//if(isSomethingSelected){
				if(otherSelected){
					selectedInstructionalAreas.add(""+otherText);
				}
				
				PersistenceBean.persistInstructionalAreas(currentIntent.getStringExtra("studentid"),selectedInstructionalAreas, context);
				currentIntent.putStringArrayListExtra("selectedareas",selectedInstructionalAreas);
				currentIntent.putExtra("otherSelected", otherSelected);
				currentIntent.putExtra("othertext", otherText);
				currentIntent.setClass(context, IEPReading.class);
				PersistenceBean.persistIntent(currentIntent.getStringExtra("studentid"),currentIntent, context);
				startActivity(currentIntent);
				
				//}
				//else
				//errorDialog()
			}
		});
		
		//ArrayList<Integer> checkedIDList=instructionalCheck.getAllCheckedIds();
		
		
		
	}

	/**
	 * 
	 * @param instructionalAreas List of areas to be checked
	 */
	public void CheckValuesFromPersistence(ArrayList<String> instructionalAreas){
		
		
	}
	private void setInstuctionalAreas() {
		Log.d("ATGUIDE", "Instructional areas");
		
		LinearLayout list1 = null;
		LinearLayout list2 = null;
		
		try {
			list1 = (LinearLayout) this.findViewById(R.id.list1);
			list2 = (LinearLayout) this.findViewById(R.id.list2);
			String firstList[] = getResources().getStringArray(R.array.list1);
			String secondList[] = getResources().getStringArray(R.array.list2);
			addListToView(firstList, list1);
			addListToView(secondList, list2);
			addOtherArea();
		} catch (Exception e) {
			Log.e("ATGUIDE", "Other Exception: " + e.getMessage());
		}

	}

	private void addOtherArea(){
		try{
		LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
				//layoutparams.weight=50;
		final LinearLayout other = (LinearLayout) this.findViewById(R.id.otherlayout);
			other.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_LTR);
		CheckBox otherCheck = new CheckBox(context);
		otherCheck.setLayoutParams(layoutparams);
		otherCheck.setText("Other");
		otherCheck.setTextColor(Color.DKGRAY);
		otherCheck.setButtonDrawable(getResources().getDrawable(
				R.drawable.custom_checkbox));
		
		
		otherCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				View v=(View) buttonView.getParent();
				EditText other=(EditText)v.findViewById(102456);
				otherSelected=isChecked;
				//other.setBackgroundColor(0);
				if(isChecked){
					Toast.makeText(context,"Please enter the name for other instructional area",Toast.LENGTH_SHORT).show();
					other.setEnabled(true);
					other.requestFocus();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(other, InputMethodManager.SHOW_FORCED);
					other.setFocusable(true);
					other.setFocusableInTouchMode(true);
					other.setCursorVisible(true);
					other.addTextChangedListener(new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							// TODO Auto-generated method stub
						}
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count,
								int after) {
							// TODO Auto-generated method stub
						}
						@Override
						public void afterTextChanged(Editable s) {
							// TODO Auto-generated method stub
							otherText=new String(s+"");
						}
					});
				}
				else{
					other.setEnabled(false);
					other.setFocusable(false);
					other.setText("");
					}
			}
		});
		EditText specify= new EditText(context);
		specify.setLayoutParams(layoutparams);
		specify.setEnabled(false);
		specify.setId(102456);
		specify.setFocusable(false);
		specify.clearFocus();
		specify.setBackground(getResources().getDrawable(R.drawable.textviewback));
		specify.setTextColor(Color.DKGRAY);
		specify.setMaxLines(1);
		
		other.addView(otherCheck);
	
		specify.setHint("Please specify");
		
		other.addView(specify);
		other.clearFocus();
		}catch(Exception e){
			Log.e("ATGUIDE",e.getMessage());
		}
		
	}

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
			
			currentCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					if(isChecked){
						selectedInstructionalAreas.add((String)buttonView.getText());
						//instructionalCheck.addChecked(buttonView.getId()); 	 	
					}
					else
						selectedInstructionalAreas.remove((String)buttonView.getText());
						//instructionalCheck.removeChecked(buttonView.getId());
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

	private void setCurrentIntent() {
		currentIntent = getIntent();
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
