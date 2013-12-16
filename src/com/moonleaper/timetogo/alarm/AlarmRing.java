package com.moonleaper.timetogo.alarm;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import com.moonleaper.timetogo.R;

/**
 * Created with IntelliJ IDEA.
 * User: Shannon
 * Date: 13-12-15
 * Time: 下午6:40
 * To change this template use File | Settings | File Templates.
 */
public class AlarmRing extends Activity {

    MediaPlayer alarmMusic;
    String activityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.

        alarmMusic = MediaPlayer.create(this, R.raw.alarm);
        alarmMusic.setLooping(true);

        alarmMusic.start();

        new AlertDialog.Builder(AlarmRing.this)
                .setTitle("Your Task!")
                //TODO
                .setMessage("Task: "+activityName)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //To change body of implemented methods use File | Settings | File Templates.
                                alarmMusic.stop();
                                AlarmRing.this.finish();
                            }
                        })
                .show();

    }

    public void setActivityName(String name){
        activityName = name;

    }
}
