package edu.neu.madcourse.mayankranjandayal.two_player_word_game.two_player;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import edu.neu.madcourse.mayankranjandayal.R;

public class TwoPlayerHome extends Activity {

    private AlertDialog mDialog;
    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_player_activity_home);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        mMediaPlayer = MediaPlayer.create(this, R.raw.track1);
        mMediaPlayer.setVolume(0.5f, 0.5f);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    public void continue2pGame(View view)
    {}

    public void findPlayers(View view)
    {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        DataHolder.getInstance().setContinueButtonClicked(false);
        Intent intent = new Intent(this , TwoPlayerUserSelection.class);
        startActivity(intent);
        finish();
    }

    public void aboutGame(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.scraggle_about_text_2p);
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

}
