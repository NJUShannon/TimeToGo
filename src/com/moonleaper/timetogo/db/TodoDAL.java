package com.moonleaper.timetogo.db;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.moonleaper.timetogo.ITodoItem;
import com.moonleaper.timetogo.R;
import com.moonleaper.timetogo.Task;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TodoDAL {
	private SQLiteDatabase todo_db;
	private Cursor allDataCursor;
	ParseObject taskParse; 
	
	public TodoDAL(Context context) {
		//Creates DBHelper Object
		MyDatabase dbHelper = new MyDatabase(context);
		todo_db = dbHelper.getWritableDatabase();
		//get Cursor for the "todo" table with all data
		allDataCursor = todo_db.query(MyDatabase.TABLE_NAME, 
				new String[] {"_id", MyDatabase.COLUMN1, MyDatabase.COLUMN2 ,MyDatabase.COLUMN3},
				null, null, null, null, null);
		allDataCursor.moveToFirst();
		//Parse initializing with keys
		Parse.initialize(context, context.getResources().getString(R.string.parseApplication), 
				context.getResources().getString(R.string.clientKey));
		ParseUser.enableAutomaticUser();

	}
	@SuppressWarnings("deprecation")
	public boolean insert(ITodoItem todoItem) {
		//insert to PARSE:
		//Create Parse object and fill with the appropriate values 
		taskParse = new ParseObject(MyDatabase.TABLE_NAME);
		taskParse.put(MyDatabase.COLUMN1, todoItem.getTitle());
		taskParse.put(MyDatabase.COLUMN3, todoItem.getPriority());
		Date date = todoItem.getDueDate();
		if (date != null){
			taskParse.put(MyDatabase.COLUMN2, todoItem.getDueDate().getTime());			
		}
		else{
			taskParse.put(MyDatabase.COLUMN2, JSONObject.NULL);
		}
		//commit insertion
		taskParse.saveInBackground(); 
		
		//local DB:
		//creates ContentValue object and fill with the appropriate values
		ContentValues task = new ContentValues();
		task.put(MyDatabase.COLUMN1, todoItem.getTitle());
		task.put(MyDatabase.COLUMN3, todoItem.getTitle());
		if (date != null){
			task.put(MyDatabase.COLUMN2, todoItem.getDueDate().getTime());			
		}
		else{
			task.putNull(MyDatabase.COLUMN2);
		}
		//insert to DB ("todo" table)
		long returnVal = todo_db.insert(MyDatabase.TABLE_NAME, null, task);
		//update cursor --> ListView
		allDataCursor.requery();
		if (returnVal == -1) {
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public boolean update(ITodoItem todoItem) {
		//new Date to update
		final Date date = todoItem.getDueDate();
		//PARSE part
		//ask Parse DB for this ITodoItem in order to update it's due.
		//create ParseQuery object and ask for tuples that their "title" field is like
		//the title of the updated ITodoItem because only due is valid to update.
		ParseQuery query = new ParseQuery(MyDatabase.TABLE_NAME);
		query.whereEqualTo(MyDatabase.COLUMN1, todoItem.getTitle());
		//run query
		query.findInBackground(new FindCallback() 
			{
				//callback function when query returns
				public void done(List<ParseObject> matches, ParseException e) {
					if(e == null){
						if(!matches.isEmpty()){
							if(date != null){
								matches.get(0).put(MyDatabase.COLUMN2, date.getTime());															
							}
							else{
								matches.get(0).put(MyDatabase.COLUMN2, JSONObject.NULL);
							}
							matches.get(0).saveInBackground();
						}
					}
					else
						return;	
				}
			}); 
		
		//local DB
		ContentValues task = new ContentValues();
		if (date != null){
			task.put(MyDatabase.COLUMN2, date.getTime());			
		}
		else{
			task.putNull(MyDatabase.COLUMN2);
		}
		//update DB (the tuple his "title" column equal to the given ITodoItem title.
		int returnVal = todo_db.update(MyDatabase.TABLE_NAME, task, 
				MyDatabase.COLUMN1 + " = ?", new String[]{todoItem.getTitle()}); 
		//update cursor --> ListView
		allDataCursor.requery();
		if (returnVal != 1) {
			return false;
		}
		return true;
		
	}
	@SuppressWarnings("deprecation")
	public boolean delete(ITodoItem todoItem) {
		//DELETE from PARSE
		ParseQuery query = new ParseQuery(MyDatabase.TABLE_NAME);
		query.whereEqualTo(MyDatabase.COLUMN1, todoItem.getTitle());
		query.findInBackground(new FindCallback() 
			{
				public void done(List<ParseObject> matches, ParseException e) {
					if(e != null){
						return;
					}
					else{
						if(!matches.isEmpty()){
							matches.get(0).deleteInBackground();						
						}
					}
				}
			}); 
		
		//DELETE from DB
		int returnVal = todo_db.delete(MyDatabase.TABLE_NAME, 
				MyDatabase.COLUMN1 + " = ?", new String[] {todoItem.getTitle() });
		//update cursor --> ListView
		allDataCursor.requery();
		if (returnVal != 1) {
			return false;
		}
		return true;
	}
	
	
	
	public Cursor getCursor(){
		return allDataCursor;
	}
	
	
}
