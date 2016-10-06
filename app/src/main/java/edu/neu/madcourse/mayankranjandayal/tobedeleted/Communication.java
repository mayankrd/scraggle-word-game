package edu.neu.madcourse.mayankranjandayal.tobedeleted;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import edu.neu.madcourse.mayankranjandayal.R;

public class Communication extends Activity {

    private AlertDialog mDialog;
    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comm_activity_communication);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void startTwoPlayer(View view)
    {
        Intent intent = new Intent(this, edu.neu.madcourse.mayankranjandayal.tobedeleted.two_player.CommunicationMain.class);
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
