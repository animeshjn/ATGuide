package edu.gmu.ttaconline.atcguide;

import java.net.URISyntaxException;
import java.util.ArrayList;

import edu.gmu.ttaconline.atcguide.FeedReaderContract.FeedEntry;
import edu.gmu.ttaconline.atcguide.FeedReaderContract.IntentStore;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
			rows = db.update(FeedEntry.STUDENT, values, ""+ FeedEntry.STUDENT_ID + "='" + studentid + "'", null);

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
			ArrayList<String> selectedInstructionalAreas, Context context) {

	}

	public static boolean persistIntent(String studentId, Intent intent,
			Context context) {
		
		boolean result = false;
		try{
		String intentDescription = null;
		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		try {
			intentDescription = intent.toUri(0);
		} catch (Exception unknown) {
			Log.e("ATGUIDE", "Error persisting intent: "+unknown.getMessage());
		}
		ContentValues values = new ContentValues();
		values.put(IntentStore.COLUMN_NAME_ID, studentId);
		values.put(IntentStore.COLUMN_NAME_INTENT, intentDescription);
		long rows = 0;
		try {
			rows = db.insertWithOnConflict(IntentStore.TABLE_NAME, null,
					values, SQLiteDatabase.CONFLICT_REPLACE);
			result = true;
		} catch (SQLException e) {
			Log.e("ATGUIDE", "SQLException: " + e.getMessage());
			result = false;
		} catch (Exception e) {
			Log.e("ATGUIDE", "Other Exception: " + e.getMessage());
			result = false;
		}
		db.close();
		if (rows > 0) {
			Toast.makeText(context, "" + rows + " inserted", Toast.LENGTH_SHORT)
					.show();
		}}catch(Exception e){
			Log.e("ATGUIDE", "Error in persisting intent "+e.getStackTrace());
			
		}
		
		return result;
	}

	public static Intent getExistingIntent(String studentId, Context context){
		Intent requiredIntent = null;
		Log.e("ATGUIDE","Searching for persisted intent with student id :"+studentId);
		int records = 0;
		Log.d("ATGUIDE", "before Db Helper");
		
		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
		Log.d("ATGUIDE", "before getting database");
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		// ContentValues values = new ContentValues();
		String intentDescription = null;
		Cursor cursor;
		
		try{
			Log.d("ATGUIDE", "Trying to get cursor");
			cursor = db.query(IntentStore.TABLE_NAME,
			new String[] { IntentStore.COLUMN_NAME_INTENT },
			IntentStore.COLUMN_NAME_ID + " = " + "'" + studentId + "'",
				null, null, null, null);
		
		}
		catch(Exception e){
			Log.e("ATUGUIDE","Error GETTING CURSOR "+e.getMessage());
			return null;}
		if(cursor==null){
			Log.e("ATGUIDE", "Cursor is null ");
		}
		int i=0;
		Log.d("ATGUIDE", "before cursor while ");
		cursor.moveToFirst();
		
			Log.d("ATGUIDE", "While started: "+(i++));
			intentDescription = cursor.getString(0);
			Log.d("ATGUIDE", "INTENT DESCRIPTION "+intentDescription);
			records++;
		try {
			requiredIntent = Intent.parseUri(intentDescription, 0);
		} catch (URISyntaxException e) {
			Log.e("ATGUIDE", "Error parsing intent: "+e.getStackTrace());
		}
		
		Toast.makeText(context,"Retrieved: "+records+" records" ,Toast.LENGTH_SHORT).show();
		if(requiredIntent == null){
			Log.e("ATGUIDE", "Required intent is null ");
		}
		
		return requiredIntent;
	}
	
	public static boolean isExistingRecord(String studentId,Context context)
	{
		FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context.getApplicationContext());
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor cursor = db.query(IntentStore.TABLE_NAME,
				new String[] { IntentStore.COLUMN_NAME_INTENT },
				IntentStore.COLUMN_NAME_ID + " like " + "'" + studentId + "'",
				null, null, null, null);
		if(cursor.getCount()>0)
		return true;
		else 
		return false;
	}
	public static void setCurrentId()
	{
		
	}

}
