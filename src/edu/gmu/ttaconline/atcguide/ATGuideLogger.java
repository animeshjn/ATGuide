package edu.gmu.ttaconline.atcguide;

import android.util.Log;

public class ATGuideLogger {

	public static final String LOGTAG="ATGUIDE";
	public static void LogIt(Area area){
		
		Log.d(LOGTAG,""+area.getAreaName());
		Log.d(LOGTAG,""+area.getParentId());
		Log.d(LOGTAG,"log area complete");
		
		
		
	}
	
	
}
