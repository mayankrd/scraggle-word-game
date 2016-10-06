package edu.neu.madcourse.mayankranjandayal.tobedeleted1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.mayankranjandayal.R;

public class BurpeeScreenZero extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burpee_activity_screenzero);

        TextView tv=    (TextView)findViewById(R.id.Burpee_title_below);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.burpee_challenge_animation );
        tv.startAnimation(animation);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {

                //here you can start your Activity B.
                Intent intent = new Intent(BurpeeScreenZero.this, BurpeeTutorial.class);
                startActivity(intent);
                finish();

            }

        }, 3000);

       // ImageView img_animation = (ImageView) findViewById(R.id.gymming_home);
        //Animation animation_home = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.home_screen_animation );
        //img_animation.startAnimation(animation_home);
    }

}
