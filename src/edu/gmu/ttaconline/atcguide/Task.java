package edu.gmu.ttaconline.atcguide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Task {
	String taskname;
	int taskid;
	String areaname;
	Map<String,String> strategies= new HashMap<String,String>();


	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
		
	}

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

//	public void addStrategy(String strategy) {
//		strategies.add(strategy);
//	}

}
