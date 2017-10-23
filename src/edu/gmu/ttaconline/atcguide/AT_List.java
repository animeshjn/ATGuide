package edu.gmu.ttaconline.atcguide;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;

public class AT_List extends DialogFragment {
	ATListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	View group;
	Activity firstTrail;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getDialog().setTitle("Choose AT");
		View rootView = inflater.inflate(R.layout.activity_at_list, container,
				false);
		try {
			expListView = (ExpandableListView) rootView
					.findViewById(R.id.lvExp);
			prepareListData();
			listAdapter = new ATListAdapter(getActivity()
					.getApplicationContext(), listDataHeader, listDataChild);
			// setting list adapter
			expListView.setAdapter(listAdapter);
			group = container;
			expListView
					.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

						@Override
						public boolean onChildClick(ExpandableListView parent,
								View v, int groupPosition, int childPosition,
								long id) {
							String s;
							s = listDataChild.get(
									listDataHeader.get(groupPosition)).get(
									childPosition);
							((EditText) firstTrail.findViewById(R.id.at))
									.setText(s);
							// ((TextView)group.findViewById(R.id.at)).setText(listDataChild.get(
							// listDataHeader.get(groupPosition)).get(
							// childPosition));
							dismiss();
							return false;
						}
					});
		} catch (Exception e) {
			Log.e("DEMO", "Exception " + e);
		}
		return rootView;
	}

	public void setFirstTrail(Activity firstTrail) {
		this.firstTrail = firstTrail;
	}

	private void prepareListData() {
		try {
			listDataHeader = new ArrayList<String>();
			listDataChild = new HashMap<String, List<String>>();
			String headers[] = getResources().getStringArray(R.array.atareas);
			// Adding child data
			for (String header : headers) {
				listDataHeader.add(header);
			}
			// Adding child data
			List<String> read = new ArrayList<String>();
			String readAt[] = getResources().getStringArray(
					R.array.readSolutions);
			for (String solution : readAt) {
				read.add(solution);
			}
			List<String> writing = new ArrayList<String>();
			String writingSol[] = getResources().getStringArray(
					R.array.writeSolutions);
			for (String sol : writingSol) {
				writing.add(sol);
			}
			List<String> math = new ArrayList<String>();
			String mathSol[] = getResources().getStringArray(
					R.array.mathSolutions);
			for (String sol : mathSol) {
				math.add(sol);
			}
			List<String> listen = new ArrayList<String>();
			listen.add("No data");
			/* Complete this for listening */

			listDataChild.put(listDataHeader.get(0), read); // Header, Child
															// data
			listDataChild.put(listDataHeader.get(1), writing);
			listDataChild.put(listDataHeader.get(2), math);
			listDataChild.put(listDataHeader.get(3), listen);

		} catch (Exception e) {
			Log.e("DEMO", "Exception in prepareData() " + e);
		}
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
