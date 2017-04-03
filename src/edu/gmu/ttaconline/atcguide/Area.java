package edu.gmu.ttaconline.atcguide;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;
/**
 * Stores data of a particular area
 * */
public class Area {
	private String name;
	ArrayList<Task> tasks;
	int parentId;

	int taskCount = 0;
	boolean taskempty = true;
	
	/**
	 * Constructor to create area with given name
	 * @param name name of the area to be created
	 */
	public Area(String name) {
		super();
		this.name = name;
		tasks = new ArrayList<Task>();
	}

	/**
	 * Set parentId or areaid to this
	 * @param id to be set 
	 */
	public void setParentId(int id) {
		parentId = id;
	}

	/**
	 * 
	 * @return parentId set to this Area
	 */
	public int getParentId() {

		return parentId;
	}

	/**
	 * @return list of Tasks in this area
	 */
	public ArrayList<Task> getTasks() {
		return tasks;
	}

	/**
	 * Set the list of tasks to this area
	 * @param tasks to be set to this area
	 */
	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}
	/**
	 * General constructor to create an empty object
	 * */
	public Area() {
		super();
	}

	/**
	 * Add task to the list of tasks of this area
	 * @param task to be added to list of tasks in this area
	 */
	public void addTask(Task task) {
		tasks.add(task);
	}

	/**
	 * Set the name of the area to this
	 * @param name of the area to be set to this
	 */
	public void setAreaName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return name of this area
	 */
	public String getAreaName() {
		return name;
	}

	/**
	 * Returns the task object based on the given task id
	 * @param id of the task to be found
	 * @return Task matching the given id
	 */
	public Task getTaskById(int id) {
		for (Task task : tasks) {
			if (task.taskid == id)
				return task;
		}
		return null;
	}

	/**
	 * Logger method to log all tasks in this area
	 */
	public void logTasks() {
		for (Task task : tasks) {
			Log.d("ATGUIDE", " taskid: " + task.taskid);
			for(String key:task.strategies.keySet())
			{
				Log.d("ATGUIDE", " Strategy id "+key+"value " + task.strategies.get(key));
			}
//			for (String  strategy : task.strategyList) {
//				Log.d("ATGUIDE", " Strategy id value " + strategy);
//			}
			
			
		}

	}

}
