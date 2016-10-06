package edu.neu.madcourse.mayankranjandayal.scraggle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import edu.neu.madcourse.mayankranjandayal.R;

public class ScraggleHome extends Activity {

    private AlertDialog mDialog;
    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scraggle_activity_home);

        mMediaPlayer = MediaPlayer.create(this, R.raw.track1);
        mMediaPlayer.setVolume(0.5f, 0.5f);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();*/
    }

    public void continueGame(View view){

       if(DataHolder.getInstance().isContinueFlagStage1())
        {
            DataHolder.getInstance().setContinueFlagStage1(false);
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            DataHolder.getInstance().setContinueButtonClicked(true);
            Intent intent = new Intent(this , Stage1.class);
            startActivity(intent);
            finish();
        }
       else if (DataHolder.getInstance().isContinueFlagStage2())
       {
           DataHolder.getInstance().setContinueFlagStage2(false);
           mMediaPlayer.stop();
           mMediaPlayer.reset();
           mMediaPlayer.release();
           DataHolder.getInstance().setContinueButtonClicked(true);
           Intent intent = new Intent(this , Stage2.class);
           startActivity(intent);
           finish();
       }

    }

    public void newGame(View view){

        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        DataHolder.getInstance().setContinueButtonClicked(false);
        Intent intent = new Intent(this , Stage1.class);
        startActivity(intent);
        finish();

    }

    public void aboutGame(View view){

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

    public void quitScraggle(View view){

        finish();
    }

    public void displayAck(View view){

        Intent intent = new Intent(this, edu.neu.madcourse.mayankranjandayal.Acknowledgement.class);
        this.startActivity(intent);
    }


}