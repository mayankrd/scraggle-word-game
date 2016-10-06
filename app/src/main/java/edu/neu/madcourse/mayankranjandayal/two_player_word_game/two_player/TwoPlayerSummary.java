package edu.neu.madcourse.mayankranjandayal.two_player_word_game.two_player;

import android.os.Bundle;
import android.app.Activity;

import edu.neu.madcourse.mayankranjandayal.R;

public class TwoPlayerSummary extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_player_activity_summary);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
