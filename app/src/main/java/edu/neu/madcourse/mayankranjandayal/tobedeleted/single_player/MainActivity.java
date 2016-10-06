/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.mayankranjandayal.tobedeleted.single_player;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

import edu.neu.madcourse.mayankranjandayal.R;

public class MainActivity extends Activity {
   MediaPlayer mMediaPlayer;
   // ...

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.scraggle_activity_main);
   }

   @Override
   protected void onResume() {
      super.onResume();
      mMediaPlayer = MediaPlayer.create(this, R.raw.track1);
      mMediaPlayer.setVolume(0.5f, 0.5f);
      mMediaPlayer.setLooping(true);
      mMediaPlayer.start();
   }

   @Override
   protected void onPause() {
      super.onPause();
      mMediaPlayer.stop();
      mMediaPlayer.reset();
      mMediaPlayer.release();
   }
}
