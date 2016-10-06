package edu.neu.madcourse.mayankranjandayal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.telephony.TelephonyManager;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayAboutDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_about_details);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        TelephonyManager telMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String imei=telMgr.getDeviceId();
        TextView textViewIMEI = (TextView) findViewById(R.id.IMEI_number);
        textViewIMEI.setText("Phone's Unique ID:"+imei);
        ImageView imgView= (ImageView) findViewById(R.id.imageView);
        imgView.setImageResource(R.drawable.my_image);
    }

}
