/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.mayankranjandayal.scraggle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import edu.neu.madcourse.mayankranjandayal.R;

public class GameActivity extends Activity implements CompoundButton.OnCheckedChangeListener{
   public static final String KEY_RESTORE = "key_restore";
   public static final String PREF_RESTORE = "pref_restore";
   //private MediaPlayer mMediaPlayer;
   private Handler mHandler = new Handler();
   private GameFragment mGameFragment;
   MediaPlayer mMediaPlayer;
   ToggleButton t;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.scraggle_activity_game);
      mGameFragment = (GameFragment) getFragmentManager()
            .findFragmentById(R.id.fragment_game);

      mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.track2);
      mMediaPlayer.start();
      boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
      final Activity obj = this;
/*      if (restore) {
         String gameData = getPreferences(MODE_PRIVATE)
               .getString(PREF_RESTORE, null);
         if (gameData != null) {
            mGameFragment.putState(gameData);
         }
      }*/
      t = (ToggleButton) findViewById(R.id.sound);
      t.setOnCheckedChangeListener(this);

      Log.d("UT3", "restore = " + restore);
      final TextView timer;
      timer=(TextView) findViewById(R.id.timer);
      new CountDownTimer(90000, 1000) {

         public void onTick(long millisUntilFinished) {
            timer.setText("Seconds Left: " + millisUntilFinished / 1000);
         }

         public void onFinish() {

            timer.setText("Time Over !");
            Intent intent = new Intent(obj, edu.neu.madcourse.mayankranjandayal.scraggle.Stage2.class);
            startActivity(intent);
            finish();
         }
      }.start();
   }

   public void restartGame() {
      mGameFragment.restartGame();
   }

   public void reportWinner(final Tile.Owner winner) {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
         mMediaPlayer.stop();
         mMediaPlayer.reset();
         mMediaPlayer.release();
      }
      builder.setMessage(getString(R.string.declare_winner, winner));
      builder.setCancelable(false);
      builder.setPositiveButton(R.string.ok_label,
            new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                  finish();
               }
            });
      final Dialog dialog = builder.create();
      mHandler.postDelayed(new Runnable() {
         @Override
         public void run() {
            mMediaPlayer = MediaPlayer.create(GameActivity.this,
                    winner == Tile.Owner.X ? R.raw.oldedgar_winner
                            : winner == Tile.Owner.O ? R.raw.notr_loser
                            : R.raw.department64_draw
            );
            mMediaPlayer.start();
            dialog.show();
         }
      }, 500);

      // Reset the board to the initial position
      mGameFragment.initGame();
   }

   public void startThinking() {
      View thinkView = findViewById(R.id.thinking);
      thinkView.setVisibility(View.VISIBLE);
   }

   public void stopThinking() {
      View thinkView = findViewById(R.id.thinking);
      thinkView.setVisibility(View.GONE);
   }

   @Override
   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      if (isChecked) {
         mMediaPlayer.stop();

      }
      else {
         mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.track2);
         mMediaPlayer.setLooping(true);
         mMediaPlayer.start();
      }
   }

   /*@Override
   protected void onResume() {
      super.onResume();
      mMediaPlayer = MediaPlayer.create(this, R.raw.track2);
      mMediaPlayer.setLooping(true);
      mMediaPlayer.start();
   }

   @Override
   protected void onPause() {
      super.onPause();
      mHandler.removeCallbacks(null);
      mMediaPlayer.stop();
      mMediaPlayer.reset();
      mMediaPlayer.release();
      String gameData = mGameFragment.getState();
      getPreferences(MODE_PRIVATE).edit()
            .putString(PREF_RESTORE, gameData)
            .commit();
      Log.d("UT3", "state = " + gameData);
   }*/
}
