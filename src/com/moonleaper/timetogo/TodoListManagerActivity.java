package com.moonleaper.timetogo;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.moonleaper.timetogo.alarm.AlarmRing;
import com.moonleaper.timetogo.db.TodoDAL;

import java.util.Calendar;
import java.util.Date;

public class TodoListManagerActivity extends Activity {
	
	private static final int ADD_ITEM = 1;
	//private static final String CALL_TASK_PREFIX = "Call ";
	private ToDolistCursorAdapter todoListCursorAdapter;
	private ListView tasksList;
	private TodoDAL todoDALhelper;
    String if_repeat;
    Date date;

    public int priority;

    WeatherAnalysis weather = null;
    int property;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
        //System.out.println("HELLO");
		
		//allTasks = new ArrayList<Task>();
		todoDALhelper = new TodoDAL(this);
		tasksList = (ListView)findViewById(R.id.lstTodoItems);
		
		String[] from = {"title", "due"};
		int[] to = {R.id.txtTodoTitle, R.id.txtTodoDueDate};
		//Adapter between DB and ListView
		todoListCursorAdapter = new ToDolistCursorAdapter(this, R.layout.task_row, todoDALhelper.getCursor(), from, to);
		//bind DB and ListView
		tasksList.setAdapter(todoListCursorAdapter);
		registerForContextMenu(tasksList);

        //TODO
        //Intent weatherIntent = new Intent(TodoListManagerActivity.this,WeatherAnalysis.class);
        //startService(weatherIntent);
        /*ry{
        Intent getIntent = getIntent();
        Bundle bundle = getIntent.getExtras();
        String str = bundle.getString(WeatherAnalysis.PROPERTY);
        property = Integer.parseInt(str);
        System.out.println("Property in TodoM: "+property);
        }catch (Exception e){
            e.printStackTrace();
        } */



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menuItemAdd:
	        	Intent addIntent = new Intent(this, AddNewTodoItemActivity.class);
	        	startActivityForResult(addIntent, ADD_ITEM);
	        	break;
	    }
	    return true;
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, tasksList, null);
		getMenuInflater().inflate(R.menu.task_context, menu);
		String curTask = todoDALhelper.getCursor().getString(1);
		//set context menu header as the task title
		menu.setHeaderTitle(curTask);
		
	}
	
	public boolean onContextItemSelected(MenuItem item) {
		String selectedTask = todoDALhelper.getCursor().getString(1);
		switch (item.getItemId()){
			case R.id.menuItemDelete:
				//delete from DB and from Parse
				todoDALhelper.delete(new Task(selectedTask, new Date(),todoDALhelper.getCursor().getInt(3)));
				return true;
			
			}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		case ADD_ITEM:
			if(resultCode == RESULT_OK){
				Task toAdd = new Task((String)data.getSerializableExtra(AddNewTodoItemActivity.FIRST_CONTENT), 
						(Date)data.getSerializableExtra(AddNewTodoItemActivity.SECOND_CONTENT)
                        ,Integer.parseInt((String)data.getSerializableExtra(AddNewTodoItemActivity.THIRD_CONTENT)));
				//Insert Task to DB and Parse
                if_repeat =(String) data.getSerializableExtra(AddNewTodoItemActivity.FORTH_CONTENT);
				todoDALhelper.insert(toAdd);

                priority =  Integer.parseInt((String)data.getSerializableExtra(AddNewTodoItemActivity.THIRD_CONTENT));

                date = (Date)data.getSerializableExtra(AddNewTodoItemActivity.SECOND_CONTENT);
                if(date != null){
                    AlarmManager aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);

                    /*try {
                        Intent getIntent = getIntent();
                        Bundle bundle = getIntent.getExtras();
                        String str = bundle.getString(WeatherAnalysis.PROPERTY);
                        property = Integer.parseInt(str);
                        System.out.println("Property in TodoM: "+property);

                    }  catch (Exception e){
                        e.printStackTrace();
                    } */


                    //AlarmManager aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
                   // if(property == 0){
                    if(if_repeat == "true" ){

                        Intent intent = new Intent(TodoListManagerActivity.this, AlarmRing.class);

                        PendingIntent pi = PendingIntent.getActivity(TodoListManagerActivity.this, 0, intent, 0);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(System.currentTimeMillis());
                        cal.set(Calendar.HOUR_OF_DAY,date.getHours());
                        cal.set(Calendar.MINUTE,date.getMinutes());
                        long times =24 *3600*1000;
                        aManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), times, pi);

                    }
                    else
                    {
                        Intent intent = new Intent(TodoListManagerActivity.this, AlarmRing.class);

                        PendingIntent pi = PendingIntent.getActivity(TodoListManagerActivity.this, 0, intent, 0);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(System.currentTimeMillis());
                        cal.setTime(date);
                        aManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pi);
                    }
                    AlarmManager bManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
                    Intent weatherIntent = new Intent(TodoListManagerActivity.this,WeatherAnalysis.class);
                    final PendingIntent pi1 = PendingIntent.getService(TodoListManagerActivity.this,
                            0, weatherIntent, 0);


                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTimeInMillis(System.currentTimeMillis());
                    //cal1.set(Calendar.HOUR_OF_DAY,date.getHours());
                    //cal1.set(Calendar.MINUTE,date.getMinutes());
                    cal1.setTime(date);
                    bManager.set(AlarmManager.RTC_WAKEUP,cal1.getTimeInMillis()-3600*1000,pi1);

                    }

                    //priority == 1 means advancing
                    /*else if(property == 1 && priority ==1){
                        if(if_repeat == "true" ){

                            Intent intent = new Intent(TodoListManagerActivity.this, AlarmRing.class);

                            PendingIntent pi = PendingIntent.getActivity(TodoListManagerActivity.this, 0, intent, 0);
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(System.currentTimeMillis());
                            cal.set(Calendar.HOUR_OF_DAY,date.getHours());
                            cal.set(Calendar.MINUTE,date.getMinutes());
                            long times =24 *3600*1000;
                            aManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()-25*60*1000, times, pi);

                        }
                        else
                        {
                            Intent intent = new Intent(TodoListManagerActivity.this, AlarmRing.class);

                            PendingIntent pi = PendingIntent.getActivity(TodoListManagerActivity.this, 0, intent, 0);
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(System.currentTimeMillis());
                            cal.setTime(date);
                            aManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis()-25*60*1000,pi);
                        }
                    }
                    else if(property ==1 && priority == 2 ) {
                        if(if_repeat == "true" ){

                            Intent intent = new Intent(TodoListManagerActivity.this, AlarmRing.class);

                            PendingIntent pi = PendingIntent.getActivity(TodoListManagerActivity.this, 0, intent, 0);
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(System.currentTimeMillis());
                            cal.set(Calendar.HOUR_OF_DAY,date.getHours());
                            cal.set(Calendar.MINUTE,date.getMinutes());
                            long times =24 *3600*1000;
                            aManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()+25*60*1000, times, pi);

                        }
                        else
                        {
                            Intent intent = new Intent(TodoListManagerActivity.this, AlarmRing.class);

                            PendingIntent pi = PendingIntent.getActivity(TodoListManagerActivity.this, 0, intent, 0);
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(System.currentTimeMillis());
                            cal.setTime(date);
                            aManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis()+25*60*1000,pi);
                        }
                    }
                    else if(property == 2 && priority ==1){
                        if(if_repeat == "true" ){

                            Intent intent = new Intent(TodoListManagerActivity.this, AlarmRing.class);

                            PendingIntent pi = PendingIntent.getActivity(TodoListManagerActivity.this, 0, intent, 0);
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(System.currentTimeMillis());
                            cal.set(Calendar.HOUR_OF_DAY,date.getHours());
                            cal.set(Calendar.MINUTE,date.getMinutes());
                            long times =24 *3600*1000;
                            aManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()-50*60*1000, times, pi);

                        }
                        else
                        {
                            Intent intent = new Intent(TodoListManagerActivity.this, AlarmRing.class);

                            PendingIntent pi = PendingIntent.getActivity(TodoListManagerActivity.this, 0, intent, 0);
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(System.currentTimeMillis());
                            cal.setTime(date);
                            aManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis()-50*60*1000,pi);
                        }
                    }
                    else {
                        if(if_repeat == "true" ){

                            Intent intent = new Intent(TodoListManagerActivity.this, AlarmRing.class);

                            PendingIntent pi = PendingIntent.getActivity(TodoListManagerActivity.this, 0, intent, 0);
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(System.currentTimeMillis());
                            cal.set(Calendar.HOUR_OF_DAY,date.getHours());
                            cal.set(Calendar.MINUTE,date.getMinutes());
                            long times =24 *3600*1000;
                            aManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()+50*60*1000, times, pi);

                        }
                        else
                        {
                            Intent intent = new Intent(TodoListManagerActivity.this, AlarmRing.class);

                            PendingIntent pi = PendingIntent.getActivity(TodoListManagerActivity.this, 0, intent, 0);
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(System.currentTimeMillis());
                            cal.setTime(date);
                            aManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis()+50*60*1000,pi);
                        }
                    }
                } */
			}
			break;
		}
	}

   /* public void BinderService(){

        Intent intent = new Intent(this, WeatherAnalysis.class);
        bindService(intent , new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                 weather = ((WeatherAnalysis.LocalBinder)iBinder).getService();
                 property = weather.property;

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                  weather = null;
            }
        }, Context.BIND_AUTO_CREATE) ;
    }
     */

}
	

