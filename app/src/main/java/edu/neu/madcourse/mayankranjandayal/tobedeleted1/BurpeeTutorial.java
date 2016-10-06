package edu.neu.madcourse.mayankranjandayal.tobedeleted1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import edu.neu.madcourse.mayankranjandayal.R;

public class BurpeeTutorial extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burpee_activity_tutorial);

    }

    @Override
    public void onResume() {
        super.onResume();
        VideoView videoview = (VideoView) findViewById(R.id.video_view);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.burpeedemo);

        videoview.setVideoURI(uri);
        videoview.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void skip2content(View view) {

        Intent intent = new Intent(this, BurpeeGetSet.class);
        startActivity(intent);

    }
}
