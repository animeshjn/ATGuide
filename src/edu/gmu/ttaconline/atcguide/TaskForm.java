package edu.gmu.ttaconline.atcguide;

import java.util.ArrayList;
import java.util.Collections;

import com.commonsware.cwac.merge.MergeAdapter;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskForm extends Activity {

	private static final String TAG = "ATGUIDE";
	Intent currentIntent;
	Intent persisted;
	Context context;
	LayoutInflater inflater;
	ArrayList<CharSequence> selectedInstructional;
	ArrayList<String> selectedList;
	MergeAdapter merge= new MergeAdapter();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_form);
		currentIntent=getIntent();
		
		
		context=getApplicationContext();
		inflater=getLayoutInflater();
		placeArea();
		// place task 
		//generate task form
		//fill task form
		
	}

	
	
	private void getData() {
		selectedInstructional=currentIntent.getCharSequenceArrayListExtra("selectedareas");
		selectedList= new ArrayList<String>();
		for (CharSequence selected : selectedInstructional) {
			selectedList.add((String)selected);
			Log.d(TAG," "+selected);
		}
	}



	private void placeArea() {
		getData();
		LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ListView instructional=(ListView) findViewById(R.id.instructionalAreasList);
		
		//ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,
	      //      android.R.layout.simple_list_item_1,selectedList);
		for (CharSequence areaText :selectedInstructional) {
		
		View v= inflater.inflate(R.layout.areataskrow, null);
		
		TextView area = (TextView)v.findViewById(R.id.areatextview);
				//area.setBackground(getResources().getDrawable(R.drawable.textviewback));
				area.setText(areaText);
				//area.setSelected(true);
				area.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_START);
				area.setLayoutParams(textViewParams);
				area.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						TextView curr=(TextView)v;
						Toast.makeText(context, "Area clicked: "+curr.getText(),Toast.LENGTH_SHORT).show();
						try{
							PersistenceBean.persistIntent(currentIntent.getStringExtra("studentid"), currentIntent,context);
							persisted= PersistenceBean.getExistingIntent(currentIntent.getStringExtra("studentid"), context);
							Log.d("ATGUIDE","here: "+persisted.getStringExtra("studentid"));
							
						}
							catch(Exception e){Log.e("ATGUIDE",e.getMessage());}
						
					}
				});
		
				merge.addView(v);
				merge.setActive(v,true);
				
		
				
		}
	instructional.setAdapter(merge);
		
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
}
