package edu.gmu.ttaconline.atcguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Waiting extends Activity{
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_waiting);
	PDFLogic.activity=this;
	android.widget.ProgressBar bar = new android.widget.ProgressBar(getApplicationContext());
	bar.setIndeterminate(true);
	//setContentView(layoutResID);
	Intent pdfService= new Intent(getApplicationContext(),PDFLogic.class);
	startService(pdfService);
}
}
