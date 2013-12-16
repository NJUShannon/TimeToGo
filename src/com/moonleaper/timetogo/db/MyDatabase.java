package com.moonleaper.timetogo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabase extends SQLiteOpenHelper {
	
	public static final String DB_NAME = "todo_db";
	public static final String TABLE_NAME = "todo";
	public static final String COLUMN1 = "title";
	public static final String COLUMN2 = "due";
	public static final String COLUMN3 = "priority";
	
	public MyDatabase(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME + " ( _id integer primary key autoincrement, "
				+ COLUMN1 + " text, " + COLUMN2 + " integer ,"+ COLUMN3 + " integer );");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Nothing to do.
	}

}
