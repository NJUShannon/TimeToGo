package com.moonleaper.timetogo;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements ITodoItem{
	
	private static final String NO_DUE_DATE = "No due date";
	private String _theTask;
	private Date _dueDate;
	private String _strDate;
	private int _priority;
	
	public Task(String theTask, Date dueDate,int priority) {
		_theTask = theTask;
		_priority = priority;
		if(dueDate != null){
			_dueDate = new Date(dueDate.getTime());
			_strDate = dateAsString(_dueDate);
		}
		else{
			_dueDate = null;
			_strDate = NO_DUE_DATE;
		}
	}

	public String getTask() {
		return _theTask;
	}

	public Date getDueDate() {
		return _dueDate;
	}
	
	public int getPriority(){
		return _priority;
	}

	public String getStrDate() {
		return _strDate;
	}
	
	@SuppressLint("SimpleDateFormat")
	public String dateAsString(Date taskDate){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = df.format(taskDate);
		return dateStr;
	}

	public String getTitle() {
		return _theTask;
	}
}
