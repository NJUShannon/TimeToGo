package com.moonleaper.timetogo.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.moonleaper.timetogo.R;

public class AlarmActivity extends Activity {
	public static final String ALARM_DATE = "alarmDate";
	public static final String ALARM_TIME = "alarmTime";
	public static final String IF_REPEAT = "if_repeat";
	
	
	
	DatePicker alarmDate;
	TimePicker alarmTime;
	
	CheckBox repeatCheck;
	Button btnOk;
	Button btnCancel;
	
	String dateStr;
	String timeStr;
    String repeatChkStr;
	
	//Calendar cal;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		
		alarmDate = (DatePicker)findViewById(R.id.alarmDatePicker);
		alarmTime = (TimePicker)findViewById(R.id.alarmTimePicker);
		
		repeatCheck = (CheckBox)findViewById(R.id.if_repeat);
		btnOk = (Button)findViewById(R.id.alarmOk);
		btnCancel = (Button)findViewById(R.id.alarmCancel);
		
		repeatCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					
					
					
				}
				
			}
			
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		btnOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dateStr = alarmDate.getYear() +"-"+(alarmDate.getMonth()+1)+"-"+alarmDate.getDayOfMonth();
				timeStr = alarmTime.getCurrentHour()+":"+alarmTime.getCurrentMinute()+":"+"00";
				
				alarmTime.setIs24HourView(true);
                if(repeatCheck.isChecked()){
                    repeatChkStr = "true";
                }
                else
                {
                    repeatChkStr = "false";
                }
				
				Intent alarmIntent = new Intent();
				alarmIntent.putExtra(ALARM_DATE, dateStr);
				alarmIntent.putExtra(ALARM_TIME, timeStr);
				alarmIntent.putExtra(IF_REPEAT, repeatChkStr);

                setResult(RESULT_OK,alarmIntent);
                finish();
				
			}
		});
			
		
		
		
	}
	
	
	

}
