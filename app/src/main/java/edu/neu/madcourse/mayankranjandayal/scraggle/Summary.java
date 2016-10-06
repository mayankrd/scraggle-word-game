package edu.neu.madcourse.mayankranjandayal.scraggle;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import edu.neu.madcourse.mayankranjandayal.R;

public class Summary extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.scraggle_activity_summary);
        int points = DataHolder.getInstance().getPoints();
        int nineWords = DataHolder.getInstance().getBonusPoints();
        int bonusPoints = (DataHolder.getInstance().getBonusPoints()) * 5;
        int total = points + bonusPoints;
        TextView points_tv = (TextView)findViewById(R.id.points);
        TextView nineWords_tv = (TextView)findViewById(R.id.nineWords);
        TextView bonusPoints_tv = (TextView)findViewById(R.id.bonusPoints);
        TextView totalScore_tv = (TextView)findViewById(R.id.totalScore);
        points_tv.setText(Integer.toString(points));
        nineWords_tv.setText(Integer.toString(nineWords));
        bonusPoints_tv.setText(Integer.toString(bonusPoints));
        totalScore_tv.setText(Integer.toString(total));

    }

}
