package edu.neu.madcourse.mayankranjandayal.two_player_word_game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import edu.neu.madcourse.mayankranjandayal.R;
import edu.neu.madcourse.mayankranjandayal.two_player_word_game.two_player.DataHolder;
import edu.neu.madcourse.mayankranjandayal.two_player_word_game.two_player.TwoPlayerUserSelection;

public class twoPlayerWordGameHome extends Activity {

    private AlertDialog mDialog;
    MediaPlayer mMediaPlayer;

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comm_activity_communication);

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {

                handleShakeEvent(count);
            }
        });
    }

    public void handleShakeEvent(int cnt)
    {
        //Toast.makeText(this.getApplicationContext(), "Phone shaked !!", Toast.LENGTH_SHORT).show();
        DataHolder.getInstance().setContinueButtonClicked(false);
        Intent intent = new Intent(this , TwoPlayerUserSelection.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    public void startTwoPlayer(View view)
    {
        Intent intent = new Intent(this, edu.neu.madcourse.mayankranjandayal.two_player_word_game.two_player.TwoPlayerHome.class);
        this.startActivity(intent);
    }

    public void startSinglePlayer(View view)
    {
        Intent intent = new Intent(this, edu.neu.madcourse.mayankranjandayal.tobedeleted.single_player.single_player_home.class);
        this.startActivity(intent);
    }

    public void aboutGame(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.scraggle_about_text);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok_label,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // nothing
                    }
                });
        mDialog = builder.show();
    }

    public void displayAck(View view)
    {
        Intent intent = new Intent(this, edu.neu.madcourse.mayankranjandayal.Acknowledgement.class);
        this.startActivity(intent);
    }

    public void quitScraggle(View view)
    {
        finish();

    }

}
