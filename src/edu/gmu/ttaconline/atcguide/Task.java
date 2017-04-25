package edu.gmu.ttaconline.atcguide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Bean to store single task from a particular area
 * */
public class Task {
	//Overview: 
	//Bean to store single task from a particular area
	//Instances
	String taskname;
	int taskid;
	boolean solutions=true;
	String areaname;
	Map<String,String> strategies= new HashMap<String,String>();
	public List<String> strategyList=new ArrayList<String>();
	static int strategyId;
	public List<AT> ats=new ArrayList<AT>();
	//Methods
	/**
	 * @return TaskName of this task
	 */
	public String getTaskname() {
		return taskname;
	}
	/**
	 * set the given task name to this task
	 * @param taskname name of the task to be set to this
	 */
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	
	public void copyTask(Task task){
		this.taskid=task.taskid;
		this.solutions=task.solutions;
		this.areaname=task.areaname;
		this.strategies=task.strategies;
		this.strategyList=task.strategyList;
		this.strategyId=task.strategyId;
		
	}
	
	/**
	 * retrieve the area name of this task
	 * @return Area name to which this task belongs
	 */
	public String getAreaname() {
		return areaname;
	}

	/**
	 * Set the area to the task
	 * @param areaname the name of the area to which this task should set to belong
	 */
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

//	public void addStrategy(String strategy) {
//		strategies.add(strategy);
//	}

}
