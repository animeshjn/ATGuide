package edu.gmu.ttaconline.atcguide;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;

public class Area {
	private String name;
	ArrayList<Task> tasks;
	int parentId;

	int taskCount = 0;
	boolean taskempty = true;

	public Area(String name) {
		super();
		this.name = name;
		tasks = new ArrayList<Task>();
	}

	public void setParentId(int id) {
		parentId = id;
	}

	public int getParentId() {

		return parentId;
	}

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	public Area() {
		super();
	}

	public void addTask(Task task) {
		tasks.add(task);
	}

	public void setAreaName(String name) {
		this.name = name;
	}

	public String getAreaName() {
		return name;
	}

	public Task getTaskById(int id) {
		for (Task task : tasks) {
			if (task.taskid == id)
				return task;
		}
		return null;
	}

	public void logTasks() {
		for (Task task : tasks) {
			Log.d("ATGUIDE", " taskid: " + task.taskid);

		}

	}

}
