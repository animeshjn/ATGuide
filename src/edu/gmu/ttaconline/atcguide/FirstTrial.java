package edu.gmu.ttaconline.atcguide;

import java.util.ArrayList;
import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class FirstTrial extends Activity {

	ArrayList<Area> areaList;
	ArrayList<String> trial1Texts;
	Context context;
	LayoutInflater inflater;
	int id=1001;
	static int clickedId=9999;
	private ArrayList<CharSequence> selectedInstructional;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_form);
		context=getApplicationContext();
		areaList=PersistenceBean.getPersistedAreaObjects("trial1"+PersistenceBean.getCurrentId(context), context);
		placeArea();
	}
	private void placeArea() {
		getData();
		LayoutParams textViewParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ListView instructional = (ListView) findViewById(R.id.instructionalAreasList);
		for (Area area: areaList) {
			LinearLayout areaRow = (LinearLayout) inflater.inflate(R.layout.areataskrow, null);
			areaRow.setId(id+=2);
			TextView areaTextView = (TextView) areaRow.findViewById(R.id.areatextview);
			// area.setBackground(getResources().getDrawable(R.drawable.textviewback));
			areaTextView.setText(area.getAreaName());
			areaTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_START);
			areaTextView.setLayoutParams(textViewParams);
			areaTextView.setId(area.parentId);
			
			
			
		}
		
	}
	private void getData() {
		try {
			// selectedInstructional = currentIntent
			// .getCharSequenceArrayListExtra("selectedareas");
			selectedInstructional = PersistenceBean.getPersistedAreaList("trial1"+PersistenceBean.getCurrentId(context),context);
			for (CharSequence cs : selectedInstructional) {
				trial1Texts.add(cs.toString());
			}
		} catch (Exception e) {
			
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
