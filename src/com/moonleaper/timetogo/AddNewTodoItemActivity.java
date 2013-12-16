package com.moonleaper.timetogo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import com.moonleaper.timetogo.alarm.AlarmActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNewTodoItemActivity extends Activity {
	public static final String FIRST_CONTENT = "title";
	public static final String SECOND_CONTENT = "dueDate";
	public static final String THIRD_CONTENT = "priority";
    public static final String FORTH_CONTENT = "if_repeat";
	
	private static final int ALARM_SET = 1;
	


	String dateStr;
	String timeStr;
	
	String if_repeat;
	

	
	
	
	
	
	Calendar calendar;
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_todo_item);
		
		final Button cancelButton = (Button)findViewById(R.id.btnCancel);
		final Button okButton = (Button)findViewById(R.id.btnOK);
		final Button setAlarmBtn = (Button)findViewById(R.id.setAlarmBtn);
		final CheckBox useAlarm = (CheckBox)findViewById(R.id.useAlarmCheck);
		//dueDate1 = (DatePicker)findViewById(R.id.datePicker);
		//dueTime = (TimePicker)findViewById(R.id.timePicker);
		setAlarmBtn.setClickable(false);
		
		
		
		
		useAlarm.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					setAlarmBtn.setEnabled(true);
					//aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
					
				}
				
			}
			
		});
		setAlarmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				calendar = Calendar.getInstance();
				/*DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener()
				{

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						calendar.set(Calendar.YEAR, year);
						calendar.set(Calendar.MONTH, monthOfYear);
						calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						
					}
					
				};
				TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener(){

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
						calendar.set(Calendar.MINUTE, minute);
					}
					
				};*/
				
				Intent setIntent = new Intent(AddNewTodoItemActivity.this, AlarmActivity.class);
				startActivityForResult(setIntent, ALARM_SET);
				
				
				
				
				
			}
			
		});
		
		
		
		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				EditText taskTitle = (EditText)findViewById(R.id.edtNewItem);
				EditText taskPriority = (EditText)findViewById(R.id.edit_priority);
				calendar = Calendar.getInstance();
				

				try{

                    calendar.setTime(df.parse(dateStr+" "+timeStr));
                }catch (Exception e){
                   
                	e.printStackTrace();
                }

				
				if(useAlarm.isChecked()){
				Intent taskIntent = new Intent();
				taskIntent.putExtra(FIRST_CONTENT, taskTitle.getText().toString());
				taskIntent.putExtra(SECOND_CONTENT, calendar.getTime());
				taskIntent.putExtra(THIRD_CONTENT, taskPriority.getText().toString());
                taskIntent.putExtra(FORTH_CONTENT,if_repeat);
				setResult(RESULT_OK, taskIntent);
				finish();
                }
                else
                {
                    Intent taskIntent = new Intent();
                    taskIntent.putExtra(FIRST_CONTENT, taskTitle.getText().toString());
                    //taskIntent.putExtra(SECOND_CONTENT,new Date());
                    taskIntent.putExtra(THIRD_CONTENT, taskPriority.getText().toString());
                    taskIntent.putExtra(FORTH_CONTENT, "false");
                    setResult(RESULT_OK, taskIntent);
                    finish();
                }
			}
		});
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ALARM_SET:
			if(resultCode == RESULT_OK){
				dateStr = (String)data.getSerializableExtra(AlarmActivity.ALARM_DATE);
				timeStr = (String)data.getSerializableExtra(AlarmActivity.ALARM_TIME);
				if_repeat = (String)data.getSerializableExtra(AlarmActivity.IF_REPEAT);
			}
			break;

		default:
			break;
		}
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_todo_item, menu);
		return true;
	}
	
	

}
