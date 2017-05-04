package edu.gmu.ttaconline.atcguide;

import java.io.UnsupportedEncodingException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class IEPReading extends Activity {
	Intent currentIntent;
	Context context;
	String IEPReading = "";
	String IEPAlt = "";
	RadioGroup read;
	String eligibility = "na";
	RadioGroup iepAlternative;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_iepreading);
			Log.d("ATGUIDE", "on create");
			context = getApplicationContext();
			setIntentFromDB();
			checkUri();
			setValuesFromIntent();
			setRadioToView();
			setNextListener();
			
		} catch (Exception e) {
			Log.e("ATGUIDE", "Exception in IEP READING: " + e);
		}
	}

	/**
	 * Set Intent From DB
	 */
	private void setIntentFromDB() {
		/*
		 * Effects: initialize intent from DB
		 */
		currentIntent = getIntent();
		currentIntent = PersistenceBean.getExistingIntent(
				PersistenceBean.getCurrentId(context), context);
	}

	/**
	 * Check URI for redirection from another application
	 */
	private void checkUri() {
		Uri uri = currentIntent.getData();
		if (uri != null) {
			String msgFromUrl = currentIntent.getDataString();
			try {
				msgFromUrl = java.net.URLDecoder.decode(msgFromUrl, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (currentIntent.getData() != null
				&& currentIntent.getData().toString()
						.contains("http://iepreading.atguide.com")) {
			if (currentIntent.getData().toString()
					.equalsIgnoreCase("http://iepreading.atguide.com/yes")) {
				Toast.makeText(context, "Eligible", Toast.LENGTH_SHORT).show();
				eligibility = "yes";
				currentIntent.setData(null);
			} else if (currentIntent.getData().toString()
					.equalsIgnoreCase("http://iepreading.atguide.com/no")) {
				eligibility = "no";
				Toast.makeText(context, "Not Eligible", Toast.LENGTH_SHORT)
						.show();
				currentIntent.setData(null);
			} else {
				eligibility = "na";
				Toast.makeText(context, "Not Eligible", Toast.LENGTH_SHORT)
						.show();
				currentIntent.setData(null);
			}
			currentIntent.putExtra("eligibility", eligibility);
			currentIntent.setData(null);
			currentIntent.setClass(context, TaskForm.class);
			startActivity(currentIntent);
		}

		currentIntent = getIntent();
		// Toast.makeText(context, "Welcome Back !"+currentIntent.getData(),
		// Toast.LENGTH_SHORT)
		// .show();
		// Toast.makeText(context,"Restored State", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		if (currentIntent.getData() != null
				&& currentIntent.getData().toString()
						.contains("http://iepreading.atguide.com")) {
		}
	}

	/**
	 * Set listener to forward the view to the next activity.
	 */
	private void setNextListener() {
		Button next = (Button) findViewById(R.id.nextbutton);
		next.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				setRadioValues();
				currentIntent.putExtra("iepreading", IEPReading);
				currentIntent.putExtra("iepalt", IEPAlt);
				Log.d("ATGUIDE", "Alt:" + IEPAlt);
				Log.d("ATGUIDE", "Reading:" + IEPReading);

				if (IEPAlt.trim().equalsIgnoreCase("NO")) {
					currentIntent.setClass(context, TaskForm.class);
					PersistenceBean.persistIntent(
							currentIntent.getStringExtra("studentid"),
							currentIntent, context);
					PersistenceBean.persistCurrentId(
							currentIntent.getStringExtra("studentid"), context);
					startActivity(currentIntent);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							IEPReading.this);
					builder.setCancelable(true);
					builder.setTitle(getResources()
							.getString(R.string.AIMTitle));
					builder.setMessage(getResources().getString(
							R.string.AIMCall));
					builder.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// go to the AIM VA Eligibility at
									// http://aimeligibility.com
									Uri aimEligible = Uri
											.parse("http://aimeligibility.com/");
									try {
										Intent aimIntent = new Intent(
												Intent.ACTION_VIEW, aimEligible);
										aimIntent
												.addFlags(Intent.URI_INTENT_SCHEME);
										aimIntent
												.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
										aimIntent
												.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										aimIntent.putExtras(currentIntent);
										aimIntent
												.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
										aimIntent
												.setDataAndNormalize(aimEligible);
										PackageManager packageManager = getPackageManager();
										List<ResolveInfo> activities = packageManager
												.queryIntentActivities(
														aimIntent, 0);
										boolean isIntentSafe = activities
												.size() > 0;
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

										PersistenceBean.persistIntent(aimIntent
												.getStringExtra("studentid"),
												currentIntent, context);
										PersistenceBean.persistCurrentId(
												aimIntent
														.getStringExtra("studentid"),
												context);
										if (installed)
											startActivity(aimIntent);
										else {
											Toast.makeText(
													context,
													"AIM eLigibility not installed",
													Toast.LENGTH_SHORT).show();
											aimIntent = new Intent(
													Intent.ACTION_VIEW,
													(Uri.parse("market://aimeligibility.com")));
											aimIntent.putExtras(currentIntent);
											startActivity(aimIntent);
										}
										finish();
									} catch (Exception e) {
										Log.e("ATGUIDE", " " + e.getMessage());
									}
								}
							});
					builder.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									try {
										currentIntent = getIntent();
										currentIntent.setClass(context,
												TaskForm.class);
										PersistenceBean.persistIntent(
												currentIntent
														.getStringExtra("studentid"),
												currentIntent, context);
										PersistenceBean.persistCurrentId(
												currentIntent
														.getStringExtra("studentid"),
												context);
										startActivity(currentIntent);
									} catch (Exception unknown) {
										Log.e("ATGUIDE",
												"EX: " + unknown.getMessage());
									}
								}
							});
					builder.setNeutralButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					builder.show();
				}
			}
		});
	}

	protected void setRadioValues() {
		read = (RadioGroup) findViewById(R.id.read);
		int selectedId = read.getCheckedRadioButtonId();
		RadioButton radio = (RadioButton) findViewById(selectedId);
		IEPReading = (String) radio.getText();
		iepAlternative = (RadioGroup) findViewById(R.id.iepalternative);
		RadioButton iepaltradio = (RadioButton) findViewById(iepAlternative
				.getCheckedRadioButtonId());
		IEPAlt = (String) iepaltradio.getText();
	}

	private void setValuesFromIntent() {
		IEPReading = "" + currentIntent.getStringExtra("iepreading");
		IEPAlt = "" + currentIntent.getStringExtra("iepalt");
	}

	/**
	 * Set radio to the view
	 * 
	 */
	private void setRadioToView() {

		read = (RadioGroup) findViewById(R.id.read);
	
		if (IEPReading != null && IEPReading.equalsIgnoreCase("Yes")) {
			
			RadioButton iepreader = (RadioButton) findViewById(R.id.iepreadyes);
			read.check(iepreader.getId());
		} else {
			RadioButton iepreader = (RadioButton) findViewById(R.id.iepreadno);
			read.check(iepreader.getId());
			iepreader.callOnClick();
		}

		iepAlternative = (RadioGroup) findViewById(R.id.iepalternative);
		
		if (IEPAlt != null && IEPAlt.equalsIgnoreCase("Yes")) {
			RadioButton iepaltr = (RadioButton) findViewById(R.id.iepaltyes);
			iepAlternative.check(iepaltr.getId());
		} else {
			RadioButton iepaltr = (RadioButton) findViewById(R.id.iepaltno);
			iepAlternative.check(iepaltr.getId());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.iepreading, menu);
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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		// outState.pu
		read = (RadioGroup) findViewById(R.id.read);
		int selectedId = read.getCheckedRadioButtonId();
		RadioButton radio = (RadioButton) findViewById(selectedId);
		read = (RadioGroup) findViewById(R.id.read);
		IEPReading = (String) radio.getText();
		iepAlternative = (RadioGroup) findViewById(R.id.iepalternative);
		RadioButton iepaltradio = (RadioButton) findViewById(iepAlternative
				.getCheckedRadioButtonId());
		IEPAlt = (String) iepaltradio.getText();
		IEPReading = (String) radio.getText();
		iepAlternative = (RadioGroup) findViewById(R.id.iepalternative);
		IEPAlt = (String) iepaltradio.getText();
		super.onSaveInstanceState(outState);

	}

}
