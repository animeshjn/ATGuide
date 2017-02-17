package edu.gmu.ttaconline.atcguide;

import java.util.ArrayList;

import edu.gmu.ttaconline.atcguide.FeedReaderContract.FeedEntry;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class PersistenceBean {

	public static Intent persistInputFormData(ViewGroup v, Context context) {
		boolean result = false;

		Log.d("ATGUIDE", "Data Persistence Started");

		String studentid = ((EditText) (v.findViewById(R.id.studentid)))
				.getText().toString();
		String studentgrade = ((Spinner) (v.findViewById(R.id.studentgrade)))
				.getSelectedItem().toString();
		String studentschool = ((EditText) (v.findViewById(R.id.studentschool)))
				.getText().toString();
		String studentparticipant = ((EditText) (v
				.findViewById(R.id.participants))).getText().toString();
		DatePicker datePicker = (DatePicker) v.findViewById(R.id.date);
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth() + 1;
		int year = datePicker.getYear();
		System.out.println(" " + " " + studentid + "  " + studentgrade
				+ studentschool + studentparticipant + day + month + year);

		// SQLiteDatabase
		// db=context.openOrCreateDatabase("student",context.MODE_PRIVATE,null);

		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
		@SuppressWarnings("deprecation")
		java.sql.Date date = new java.sql.Date(year, month, day);

		// Gets the data repository in write mode
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		mDbHelper.onUpgrade(db, 2, 3);
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.STUDENT_ID, studentid);
		values.put(FeedEntry.STUDENT_GRADE, studentgrade);
		values.put(FeedEntry.STUDENT_SCHOOL, studentschool);
		values.put(FeedEntry.STUDENT_PARTICIPANTS, studentparticipant);
		values.put(FeedEntry.STUDENT_DATE, date.toString());

		// values.put(FeedEntry.STUDENT_IEPGOALS, studentgrade);

		// Insert the new row, returning the primary key value of the new row
		try {
			db.insert(FeedEntry.STUDENT, null, values);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			Log.e("ATGUIDE", "Exception: " + e.getMessage());
		} finally {
			db.close();
		}

		Log.d("ATGUIDE", "Data Persistence ended with : " + result);
		Intent intent = new Intent();
		intent.putExtra("studentid", studentid);
		intent.putExtra("studentgrade", studentgrade);
		intent.putExtra("studentparticipant", studentparticipant);
		intent.putExtra("studentschool", studentschool);
		intent.putExtra("date", date.toString());

		return intent;
	}

	public static void persistIEGoals(String studentid, String iepGoal,
			Context context) {
		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
		ContentValues values = new ContentValues();
		int rows = 0;
		values.put(FeedEntry.STUDENT_IEPGOAL, iepGoal);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		try {
			rows = db.update(FeedEntry.STUDENT, values, ""
					+ FeedEntry.STUDENT_ID + "='" + studentid + "'", null);

		} catch (SQLException e) {
			Log.e("ATGUIDE", "SQLException: " + e.getMessage());
		} catch (Exception e) {
			Log.e("ATGUIDE", "Other Exception: " + e.getMessage());
		}
		db.close();
		if (rows > 0) {
			Toast.makeText(context, "" + rows + " record(s) affected",
					Toast.LENGTH_SHORT).show();
		}
		Log.d("ATGUIDE", "IEP Goals persisted, number of row(s): " + rows);
	}

	public static void persistInstructionalAreas(String studentId,
			ArrayList<String> selectedInstructionalAreas, Context context)
	{
		
		
	}
}
