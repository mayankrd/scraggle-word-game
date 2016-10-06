package edu.neu.madcourse.mayankranjandayal.tobedeleted1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import edu.neu.madcourse.mayankranjandayal.R;

public class BurpeeGetSet extends Activity implements CompoundButton.OnCheckedChangeListener {

    Switch s1;
    MediaPlayer mMediaPlayer;
    private String TAG = "*TapToContScreen";
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String SOUNDSTATUS = "Sound";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burpee_activity_getset);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(! sharedPreferences.getString(SOUNDSTATUS,"off").equalsIgnoreCase("off"))
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SOUNDSTATUS, "on");
            editor.commit();
        }

        s1 = (Switch) findViewById(R.id.switch2);
        setSwitch();
        playPauseSound();
        s1.setOnCheckedChangeListener(this);
    }

    public void setSwitch(){
        if (sharedPreferences.getString(SOUNDSTATUS, "off").equalsIgnoreCase("on")) {
            s1.setChecked(true);
        }
        else
            s1.setChecked(false);
    }

    public void toggleSoundStatus() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getString(SOUNDSTATUS, "off").equalsIgnoreCase("on")) {
            editor.putString(SOUNDSTATUS, "off");
            editor.commit();
            Toast.makeText(this.getApplicationContext(), "No more voice prompts across the app !", Toast.LENGTH_SHORT).show();
        }
        else{
            editor.putString(SOUNDSTATUS, "on");
            editor.commit();
            Toast.makeText(this.getApplicationContext(), "Voice prompts ON across the app !", Toast.LENGTH_SHORT).show();
        }
    }

    public void playPauseSound(){
        if (sharedPreferences.getString(SOUNDSTATUS, "off").equalsIgnoreCase("on")) {
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tapanywhere);
            //mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
        }
        else{
            stopSound();
        }
    }

    public void stopSound(){
        try{
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();

        }catch(Exception e)
        {
            Log.d(TAG, "no sound to stop");
        }
    }

    public void taptocontinue(View view) {

        Intent intent = new Intent(this, BurpeeCount.class);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            // Sound is enabled
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.erkanozan_miss);
            mMediaPlayer.start();
            s1.setSoundEffectsEnabled(true);
            toggleSoundStatus();

        } else {
            // Sound  is disabled
            stopSound();
            s1.setSoundEffectsEnabled(false);
            setSoundStatusOff();
        }
    }

    public void setSoundStatusOff(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SOUNDSTATUS, "off");
        editor.commit();
        Toast.makeText(this.getApplicationContext(), "No more voice prompts across the app !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        finish();
        stopSound();
    }
}