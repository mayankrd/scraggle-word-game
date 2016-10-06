package edu.neu.madcourse.mayankranjandayal;

import android.os.Bundle;
import android.app.Activity;

public class Acknowledgement extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acknowledgement);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getIntent();
    }

}
