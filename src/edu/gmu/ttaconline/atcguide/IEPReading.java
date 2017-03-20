package edu.gmu.ttaconline.atcguide;

import java.io.UnsupportedEncodingException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
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
	RadioGroup iepAlternative;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_iepreading);
		Log.d("ATGUIDE","on create");
		context = getApplicationContext();
		currentIntent = getIntent();
		checkUri();
		// setCurrentIntent();
		setNextListener();
	}

	private void checkUri() {
		// TODO Auto-generated method stub
		Uri uri = currentIntent.getData();
		if (uri != null) {
			String msgFromUrl = currentIntent.getDataString();
			try {
				msgFromUrl = java.net.URLDecoder.decode(msgFromUrl, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		Toast.makeText(context,"Got Uri",Toast.LENGTH_SHORT).show();	
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (currentIntent.getData() != null
				&& currentIntent.getData().toString()
						.contains("http://iepreading.atguide.com")) {
			Toast.makeText(context, "Welcome Back !", Toast.LENGTH_SHORT)
					.show();
		}
		// Toast.makeText(context,"Restored State", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		if (currentIntent.getData() != null
				&& currentIntent.getData().toString()
						.contains("http://iepreading.atguide.com")) {
			Toast.makeText(context, "Welcome Back ! (Restore State)",
					Toast.LENGTH_SHORT).show();
		}
		
	}

	// public void readData()
	// {
	// RadioGroup read=(RadioGroup)findViewById(R.id.read);
	// }

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
												.setFlags(Intent.URI_INTENT_SCHEME);
										aimIntent
												.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										aimIntent.putExtras(currentIntent);
										// aimIntent.addCategory(Intent.CATEGORY_LAUNCHER);
										aimIntent
												.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
										aimIntent
												.setDataAndNormalize(aimEligible);
										PackageManager packageManager = getPackageManager();
										List<ResolveInfo> activities = packageManager
												.queryIntentActivities(
														aimIntent, 0);
										boolean isIntentSafe = activities
												.size() > 0;
										// for (ResolveInfo resolveInfo :
										// activities) {
										// String currentHomePackage =
										// resolveInfo.activityInfo.packageName;
										// if(currentHomePackage.contains("aim")&&resolveInfo.activityInfo.applicationInfo.className.contains("Main")){
										// {Intent intent = new
										// Intent(Intent.ACTION_VIEW,aimEligible);
										// intent.setData(aimEligible);
										// intent.putExtras(currentIntent);
										// intent.setClassName(currentHomePackage,
										// resolveInfo.activityInfo.applicationInfo.className);
										// startActivity(intent);
										// }
										// }
										//
										// //startActivity(new
										// Intent(context,(resolveInfo.activityInfo.applicationInfo.className+".class")));
										// }
										// if(isIntentSafe)

										startActivity(aimIntent);
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
									// TODO Auto-generated method stub
									// Say No
									// Forward to next without verifying
									// eligibility
									currentIntent.setClass(context,
											TaskForm.class);
									startActivity(currentIntent);
								}
							});
					builder.setNeutralButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									// Nothing
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

	private void setCurrentIntent() {
		currentIntent = getIntent();
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
