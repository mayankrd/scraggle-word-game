package edu.neu.madcourse.mayankranjandayal.tobedeleted1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import edu.neu.madcourse.mayankranjandayal.R;

public class BurpeeCount extends Activity implements SensorEventListener, CompoundButton.OnCheckedChangeListener{

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private static final int STEP1_THRESHOLD_MIN = 12;
    private static final int STEP1_THRESHOLD_MAX = 17;
    private static final int STEP2_THRESHOLD_MIN = -2;
    private static final int STEP2_THRESHOLD_MAX = 2;
    private static final int STEP3_THRESHOLD_MIN = -14;
    private static final int STEP3_THRESHOLD_MAX = -9;
    private int counter;
    private int burpeeCount;
    private boolean step1_flag;
    private boolean step2_flag;
    private boolean step3_flag;
    private TextView v;
    MediaPlayer mMediaPlayer;
    private String TAG = "*BurpeeCntScreen";
    Switch s1;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String currentBurpeeCount = "currBurCnt";
    public static final String lifetimeBurpeeCount = "lifeBurCnt";
    public static final String SOUNDSTATUS = "Sound";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burpee_activity_count);

        v = (TextView) findViewById(R.id.speed);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        s1 = (Switch) findViewById(R.id.switch2);
        setSwitch();
        s1.setOnCheckedChangeListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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

    public void setSwitch(){
        if (sharedPreferences.getString(SOUNDSTATUS, "off").equalsIgnoreCase("on")) {
            s1.setChecked(true);
        }
        else
            s1.setChecked(false);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        v.setText(String.valueOf(burpeeCount));
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long currTime = System.currentTimeMillis();

            if((currTime - lastUpdate) > 200)
            {
                long diffTime = (currTime - lastUpdate);
                lastUpdate = currTime;

               /*  if(step2_flag == true)
                {
                    if(counter < 15) {
                        if (y <= -9) {
                            step3_flag = true;
                            burpeeCount++;
                            v.setText(String.valueOf(burpeeCount));
                            step3.setText("Passed");
                            reSetFlags();
                        }
                        else
                            counter++;
                    }
                    else
                    {
                        //reSetFlags();
                        step1_flag = false;
                        step2_flag = false;
                        step1.setText("");
                        step2.setText("");
                    }

                }*/

                if(step1_flag == true)
                {
                    if(counter < 15) {
                        if (y >= STEP2_THRESHOLD_MIN && y <= STEP2_THRESHOLD_MAX) {
                            step2_flag = true;
                            counter = 0;
                            burpeeCount++;
                            v.setText(String.valueOf(burpeeCount));
                            playCountSound(burpeeCount);
                            reSetFlags();
                        }
                        else
                            counter++;
                    }
                    else
                    {
                        reSetFlags();
                      /*  step1_flag = false;
                        step1.setText("");*/
                    }

                }

                else
                {
                        if (y >= STEP1_THRESHOLD_MIN && y <= STEP1_THRESHOLD_MAX) {
                            step1_flag = true;

                        }
                }
            }

        }
    }

    public void reSetFlags()
    {
        step1_flag = false;
        step2_flag = false;
        step3_flag = false;
        counter = 0;
    }

    public void resetFlags(View v)
    {
        reSetFlags();
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause()
    {
        super.onPause();
        senSensorManager.unregisterListener(this);
        stopSound();
    }

    protected void onResume()
    {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopSound();
        finish();
    }

    public void gotoSummary(View v){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int lifeCnt = burpeeCount + Integer.parseInt(sharedPreferences.getString(lifetimeBurpeeCount, Integer.toString(0)));
        editor.putString(currentBurpeeCount, Integer.toString(burpeeCount));
        editor.putString(lifetimeBurpeeCount, Integer.toString(lifeCnt));
        editor.commit();
        Intent intent = new Intent(this, BurpeeSummary.class);
        startActivity(intent);
    }


    public void playCountSound(int count){

        if (sharedPreferences.getString(SOUNDSTATUS, "off").equalsIgnoreCase("on")) {
            String fileName = "count_" + count;
            int rId = getResources().getIdentifier(fileName, "raw", getPackageName());
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), rId);
            mMediaPlayer.start();
        }
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

    public void setSoundStatusOff(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SOUNDSTATUS, "off");
        editor.commit();
        Toast.makeText(this.getApplicationContext(), "No more voice prompts across the app !", Toast.LENGTH_SHORT).show();
    }



}
