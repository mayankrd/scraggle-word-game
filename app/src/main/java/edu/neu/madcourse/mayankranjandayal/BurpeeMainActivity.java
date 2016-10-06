package edu.neu.madcourse.mayankranjandayal;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import edu.neu.madcourse.mayankranjandayal.tobedeleted1.BurpeeScreenZero;

public class BurpeeMainActivity extends Activity {

    TextView appDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burpee_activity_main);
        appDesc = (TextView)findViewById(R.id.tv_appDesc);
        appDesc.setMovementMethod(new ScrollingMovementMethod());
        String title = "Burpee Challenge: App Description";

        String description_line = "<p>If you find yourself lacking on your strength and conditioning work then it’s time to take the Burpee Challenge. " +
                "The Burpee is a full body strength training exercise and the ultimate example of functional fitness. " +
                "With every rep, you'll work your arms, chest, quads, glutes, hamstrings, and abs.<br><br>" +
                "The basic movement is performed in four steps and known as a \"four-count burpee\": <br>" +
                "<br>Step 0: Begin in a standing position <br>" +
                "Step 1: Drop into a squat position with your hands on the ground <br>" +
                "Step 2: Kick your feet back, while keeping your arms extended <br>" +
                "Step 3: Immediately return your feet to the squat position <br>" +
                "Step 4: Jump up from the squat position <br><br>" +
                "Burpees will prove that your own natural bodyweight provides plenty of resistance for an ass-kicking workout that blasts your stamina and fat loss through the roof. " +
                "With the Burpee Challenge app: <br><br>"+
                "- Prove your fitness level<br>" +
                "- Challenge your friends on Facebook with your fitness achievements<br>" +
                "- Improve your stamina and burn fat <br>" +
                "- Pump your body with some real fun workout <br>" +
                "- Workout anywhere; any time <br>" +
                "<br> Take the Burpee Challenge Now !</p>";

        appDesc.setText(Html.fromHtml("<b><u>" + title + "</u></b>" +  description_line));


             /*   appDesc.setText(Html.fromHtml("<b><u>" + title + "</u></b>" +  "<br />" +
                "<p>" + description_line1 + "</p><br>" +
                "<p>" + description_line2 + "</p><br />"+
                "<p>" + description_line3 + "</p><br />"+
                "<p>" + description_line4 + "</p><br />"+
                "<p>" + description_line5 + "</p><br />"+
                "<p>" + description_line6 + "</p>")); */
    }

    public void runBurpeeChall(View v)
    {
        Intent intent = new Intent(this, BurpeeScreenZero.class);
        startActivity(intent);
    }

    public void showBurpeeAck(View v){
        Intent intent = new Intent(this, BurpeeAck.class);
        startActivity(intent);
    }

}
