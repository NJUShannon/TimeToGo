package com.moonleaper.timetogo;


import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/*
 * Adapts data between tasks DB to the listView
 */
public class ToDolistCursorAdapter extends SimpleCursorAdapter{

	private Cursor _myCursor;
	
	@SuppressWarnings("deprecation")
	public ToDolistCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		_myCursor = c;
	}
	
	//returns the appropriate view
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		//TextView's of each row in the ListView
		TextView taskTitle = (TextView)view.findViewById(R.id.txtTodoTitle);
		TextView taskDate = (TextView)view.findViewById(R.id.txtTodoDueDate);
		
		Task curTask;
		
		//checks whether the due date is null
		if(_myCursor.isNull(2)){
			curTask = new Task("", null,0);
			taskTitle.setTextColor(Color.BLACK);	
			taskDate.setTextColor(Color.BLACK);
		}
		else{
			curTask = new Task("", new Date(_myCursor.getLong(2)),_myCursor.getInt(3));
		}
		
		//sets the due date in the text view.
		taskDate.setText(curTask.getStrDate());
		//If due date isn't null and also passed display text in red
		if (curTask.getDueDate() != null){			
			if(curTask.getDueDate().before(new Date())){
				taskTitle.setTextColor(Color.RED);	
				taskDate.setTextColor(Color.RED);
			}
			else{
				taskTitle.setTextColor(Color.BLACK);	
				taskDate.setTextColor(Color.BLACK);
			}
		}
		return view;
	}
	
	public Task getItem(){
		return new Task(_myCursor.getString(1), new Date(),_myCursor.getInt(3));
	}
}
