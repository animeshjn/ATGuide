package edu.gmu.ttaconline.atcguide;

import java.util.ArrayList;
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
	 * 
	 * @param name
	 *            name of the area to be created
	 */
	public Area(String name) {
		/*
		 * Requires: Name of the area | Modifies: This Area | Effects: Creates a
		 * new Object of Area Type with given Name
		 */
		super();
		this.name = name;
		tasks = new ArrayList<Task>();
	}

	/**
	 * Set parentId or areaid to this
	 * 
	 * @param id
	 *            to be set
	 */
	public void setParentId(int id) {
		/* Requires: Id to be set as the parent area */
		/* Modifies: this area */
		/* Effects: Sets the parent id as the supplied id */
		parentId = id;
	}

	/**
	 * 
	 * @return parentId of this Area
	 */
	public int getParentId() {
		/* Requires: */
		/* Modifies: */
		/* Effects: Returns parent Id of this Area */
		return parentId;
	}

	/**
	 * @return list of Tasks in this area
	 */
	public ArrayList<Task> getTasks() {
		/* Requires: */
		/* Modifies: */
		/* Effects: returns all tasks in this Area */
		return tasks;
	}

	/**
	 * Set the list of tasks to this area
	 * 
	 * @param tasks
	 *            to be set to this area
	 */
	public void setTasks(ArrayList<Task> tasks) {
		/* Requires: List of tasks to be assigned */
		/* Modifies: This Area */
		/* Effects: Sets the Tasks in the Area */
		this.tasks = tasks;
	}

	/**
	 * General constructor to create an empty object
	 * */
	public Area() {
		/* Requires: */
		/* Modifies: this area */
		/* Effects: Calls Super Class Constructor */
		super();
	}

	/**
	 * Add task to the list of tasks of this area
	 * 
	 * @param task
	 *            to be added to list of tasks in this area
	 */
	public void addTask(Task task) {
		/* Requires: Task to be added */
		/* Modifies: Tasks in This Area */
		/* Effects: Adds the given task in the list of Tasks */
		tasks.add(task);
	}

	/**
	 * Set the name of the area to this
	 * 
	 * @param name
	 *            of the area to be set to this
	 */
	public void setAreaName(String name) {
		/* Requires: Name to be set in the Area */
		/* Modifies: this area */
		/* Effects: Sets the name as the supplied Name */
		this.name = name;
	}

	/**
	 * 
	 * @return name of this area
	 */
	public String getAreaName() {
		/* Requires: */
		/* Modifies: */
		/* Effects: returns the name of the Area */
		return name;
	}

	/**
	 * Returns the task object based on the given task id
	 * 
	 * @param id
	 *            of the task to be found
	 * @return Task matching the given id
	 */
	public Task getTaskById(int id) {
		/* Requires: Id of the Task to be found */
		/* Modifies: */
		/* Effects: returns the task matching given id in this Area */
		for (Task task : tasks) {
			if (task.taskid == id)
				return task;
		}
		return null;
	}
	
	public Task getTaskByName(String taskname) {
		/* Requires: Exact name of the Task to be found */
		/* Modifies: */
		/* Effects: returns the task matching given id in this Area */
		for (Task task : tasks) {
			if (task.taskname.equals(taskname))
				return task;
		}
		return null;
	}

	/**
	 * Logger method to log all tasks in this area
	 */
	public void logTasks() {
		/* Requires: */
		/* Modifies: */
		/* Effects: Logs all the tasks in the Area */
		for (Task task : tasks) {
			Log.d("ATGUIDE", " taskid: " + task.taskid);
			for (String key : task.strategies.keySet()) {
				Log.d("ATGUIDE", " Strategy id " + key + "value "
						+ task.strategies.get(key));
			}

		}

	}

}
