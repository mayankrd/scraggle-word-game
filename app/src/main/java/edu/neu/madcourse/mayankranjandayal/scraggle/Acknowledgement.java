package edu.neu.madcourse.mayankranjandayal.scraggle;

import android.app.Activity;
import android.os.Bundle;

import edu.neu.madcourse.mayankranjandayal.R;

public class Acknowledgement extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acknowledgement);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getIntent();
    }

}
