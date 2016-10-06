package edu.neu.madcourse.mayankranjandayal.two_player_word_game.two_player;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import edu.neu.madcourse.mayankranjandayal.R;

public class TwoPlayerStage2 extends Activity {

    MediaPlayer mMediaPlayer;
    private AlertDialog mDialog;

    ArrayList<String> lst_ab;
    ArrayList<String> lst_c;
    ArrayList<String> lst_de;
    ArrayList<String> lst_fgh;
    ArrayList<String> lst_ijkl;
    ArrayList<String> lst_mn;
    ArrayList<String> lst_op;
    ArrayList<String> lst_qr;
    ArrayList<String> lst_s;
    ArrayList<String> lst_tu;
    ArrayList<String> lst_vwxyz;

    static private int mLargeIds[] = {R.id.large1, R.id.large2, R.id.large3,
            R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8,
            R.id.large9,};
    static private int mSmallIds[] = {R.id.small1, R.id.small2, R.id.small3,
            R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8,
            R.id.small9,};

    int large, small, points, bonusPoints;
    ArrayList<Integer> smallPos = new ArrayList<Integer>();
    ArrayList<Integer> largePos = new ArrayList<Integer>();
    ArrayList<String> inputWord = new ArrayList<>();
    boolean[][] letterClicked = new boolean[9][9];
    boolean[][] stage2LetterClicked = new boolean[9][9];

    TextView points_tv;
    ToneGenerator tone;
    CountDownTimer tmr;
    long timerDuration;
    boolean blink;
    long miliSecsLeft;
    Activity newObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_player_activity_stage2);
        this.loadData();
        if(DataHolder.getInstance().isContinueButtonClicked())
        {
            DataHolder.getInstance().setContinueButtonClicked(false);
            restoreGame();
            Toast.makeText(this.getApplicationContext(), "Game Resumed !", Toast.LENGTH_SHORT).show();
        }
        else
        {
            newGame();
            Toast.makeText(getApplicationContext(), "Stage 2 started !", Toast.LENGTH_SHORT).show();
        }

        smallPos.add(99);
        largePos.add(99);
        bonusPoints = DataHolder.getInstance().getBonusPoints();
        points = DataHolder.getInstance().getPoints();
        points_tv = (TextView) findViewById(R.id.points);
        tone = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

        // ****fill stage2 tiles as per data received from stage1
        fillStage2Tiles(DataHolder.getInstance().getWords(), DataHolder.getInstance().getCorrectClicks(), points, stage2LetterClicked);

        // for beep sound
        tone = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

        // Music ON/ OFF code (Inside OnCreate)....
        final ToggleButton toggleMusicButton = (ToggleButton) this.findViewById(R.id.sound);
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.track2);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        toggleMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (toggleMusicButton.isChecked()) {

                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.track2);
                    mMediaPlayer.setLooping(true);
                    mMediaPlayer.start();


                } else {
                    mMediaPlayer.stop();
                }
            }
        });


        //timer in stage on create
        newObj = this;
        final Activity obj = this;
        stage2CountdownTimer(timerDuration, obj);


        //*****play stage2 boggle
        playGame();

/*        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();*/
    }


    public void saveGame()
    {
        DataHolder.getInstance().setStage2LetterClicked(stage2LetterClicked);
        DataHolder.getInstance().setInputWordStage2(inputWord);
        DataHolder.getInstance().setStage2TimerDuration(miliSecsLeft);
    }

    public void restoreGame()
    {
        stage2LetterClicked = DataHolder.getInstance().getStage2LetterClicked();
        inputWord = DataHolder.getInstance().getInputWordStage2();
        timerDuration = DataHolder.getInstance().getStage2TimerDuration();

    }

    public void newGame()
    {
        timerDuration = 90000;

    }

    public void pauseGame(View view)
    {
        System.out.println("MLILISECSLEFT INSIDE PAUSE GAME before cancelling timer: "+miliSecsLeft);
        tmr.cancel();
        final FrameLayout frl = (FrameLayout) findViewById(R.id.stage2_layout);
        frl.setVisibility(View.INVISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.scraggle_game_paused);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.scraggle_resume_label,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        frl.setVisibility(View.VISIBLE);
                        System.out.println("MLILISECSLEFT INSIDE PAUSE GAME on click: "+miliSecsLeft);
                        stage2CountdownTimer(miliSecsLeft, newObj);
                    }
                });
        mDialog = builder.show();
    }


    public void stage2CountdownTimer(long timerDuration, final Activity obj)
    {
        final TextView timer;
        timer = (TextView) findViewById(R.id.timer);
        final int timeBlinkMiliSecs = 10000;
        tmr = new CountDownTimer(timerDuration, 1000) {

            public void onTick(long millisUntilFinished) {

                timer.setText("Seconds Left: " + millisUntilFinished / 1000);
                miliSecsLeft = millisUntilFinished;
                if ( millisUntilFinished < timeBlinkMiliSecs ) {

                    if ( blink ) {
                        timer.setVisibility(View.VISIBLE);

                    } else {
                        timer.setVisibility(View.INVISIBLE);
                    }
                    blink = !blink;
                }

                //timer.setVisibility(View.VISIBLE);

                        /*if (millisUntilFinished < 10000 && ((millisUntilFinished / 1000) / 2 == 0))
                            timer.setVisibility(View.INVISIBLE);*/
            }

            public void onFinish() {

                timer.setText("Time Over !");
                goToSummary();
            }
        }.start();

    }

    public void restartGame(View view) {

        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        lst_ab = null;
        lst_c = null;
        lst_de = null;
        lst_fgh = null;
        lst_ijkl = null;
        lst_mn = null;
        lst_op = null;
        lst_qr = null;
        lst_s = null;
        lst_tu = null;
        lst_vwxyz = null;
        tmr.cancel();
        finish();

        Intent intent = new Intent(this, TwoPlayerStage1.class);
        startActivity(intent);

    }

    public void showHome(View view) {

        saveGame();
        DataHolder.getInstance().setContinueFlagStage2(true);
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        lst_ab= null;
        lst_c = null;
        lst_de= null;
        lst_fgh= null;
        lst_ijkl= null;
        lst_mn= null;
        lst_op= null;
        lst_qr= null;
        lst_s= null;
        lst_tu= null;
        lst_vwxyz = null;
        finish();
        tmr.cancel();
        Intent intent = new Intent(this, TwoPlayerHome.class);
        startActivity(intent);
    }

    private void playGame() {

        for (large = 0; large < 9; large++) {
            View outer = this.findViewById(mLargeIds[large]);

            for (small = 0; small < 9; small++) {

                final Button inner1 = (Button) outer.findViewById(mSmallIds[small]);
                //  inner1.setText(Character.toString(nineWords[large][small]));}}
                inner1.setTag(large + ":" + small);
                inner1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //***********my code
                        int large = Integer.parseInt(view.getTag().toString().split(":")[0]);
                        int small = Integer.parseInt(view.getTag().toString().split(":")[1]);
                        int smOld = smallPos.get(smallPos.size() - 1);
                        int lrOld = largePos.get(largePos.size() - 1);
                        //if(isButtonAvailable(large, small)) {

                        if (isAdjacent(lrOld, smOld, large, small)) {

                            //inner1.setBackgroundColor(Color.RED);
                            inner1.setTextColor(Color.RED);
                            stage2LetterClicked[large][small] = true;

                            //letterClicked[large][small] = true;
                            inputWord.add(inner1.getText().toString());
                            smallPos.add(small);
                            largePos.add(large);
                            tone.startTone(ToneGenerator.TONE_PROP_BEEP);

                        } else if (smOld == small && lrOld == large) {
                            inputWord.remove(inputWord.size() - 1);
                            smallPos.remove(smallPos.size() - 1);
                            largePos.remove(largePos.size() - 1);
                            inner1.setTextColor(Color.WHITE);
                            letterClicked[large][small] = false; // no use here..to be checked
                            stage2LetterClicked[large][small] = false;

                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid selection !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        }
    }

    public void goToSummary() {
        lst_ab = null;
        lst_c = null;
        lst_de = null;
        lst_fgh = null;
        lst_ijkl = null;
        lst_mn = null;
        lst_op = null;
        lst_qr = null;
        lst_s = null;
        lst_tu = null;
        lst_vwxyz = null;
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        DataHolder.getInstance().setPoints(points);
        DataHolder.getInstance().setBonusPoints(bonusPoints);
        tmr.cancel();
        Intent intent = new Intent(this, TwoPlayerSummary.class);
        startActivity(intent);
        finish();
    }

    public void checkWord(View view) {

        // inputWord = DataHolder.getInstance().getArl();
        if (inputWord != null) {
            String word = "";
            for (String s : inputWord) {
                word += s;
            }

            if (word.length() > 0) {
                if (wordSearch(word)) {
                    Toast.makeText(this.getApplicationContext(), "Correct Word !", Toast.LENGTH_SHORT).show();
                    points = points + word.length();
                    if (word.length() == 9)
                        bonusPoints = bonusPoints + 1;
                    points_tv.setText("Points: " + Integer.toString(points));
                    goToSummary();
                    /*for(int i=1; i<largePos.size(); i++)
                    {
                        correctClicks[largePos.get(i)][smallPos.get(i)] =true;
                    }

                    wordSelected[largePos.get(1)] = true;
                    updateAllTiles(words, wordSelected, letterClicked);
                    initTileData();*/

                } else
                    Toast.makeText(this.getApplicationContext(), "InCorrect Word !", Toast.LENGTH_SHORT).show();
            } else {
            }
        } else {
            Toast.makeText(this.getApplicationContext(), "Please select a word !", Toast.LENGTH_SHORT).show();
        }

    }


    public boolean isAdjacent(int lrOld, int smOld, int largeNew, int smallNew) {
        if (lrOld != largeNew) {
            return true;
        }
        return false;

    }

    public void fillStage2Tiles(char[][] words, boolean[][] correctClicks, int points, boolean[][] stage2LetterClicked) {
        points_tv.setText("Points: " + Integer.toString(points));

        for (int large = 0; large < 9; large++) {
            View outer = this.findViewById(mLargeIds[large]);

            for (int small = 0; small < 9; small++) {

                final Button inner1 = (Button) outer.findViewById(mSmallIds[small]);
                inner1.setText(Character.toString(words[large][small]));
                if (correctClicks[large][small] == false) {
                    inner1.setText("");
                    inner1.setEnabled(false);
                }
                if (stage2LetterClicked[large][small] == true)
                {
                    inner1.setTextColor(Color.RED);
                }

            }
        }

    }


    public void loadData() {
        try {
            String data;
            InputStream ins;
            BufferedReader reader;

            this.lst_ab = new ArrayList<String>();
            this.lst_c = new ArrayList<String>();
            this.lst_de = new ArrayList<String>();
            this.lst_fgh = new ArrayList<String>();
            this.lst_ijkl = new ArrayList<String>();
            this.lst_mn = new ArrayList<String>();
            this.lst_op = new ArrayList<String>();
            this.lst_qr = new ArrayList<String>();
            this.lst_s = new ArrayList<String>();
            this.lst_tu = new ArrayList<String>();
            this.lst_vwxyz = new ArrayList<String>();


            ins = getResources().openRawResource(R.raw.wordlist_a_b);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_ab.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_c);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_c.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_d_e);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_de.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_f_h);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_fgh.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_i_l);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_ijkl.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_m_n);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_mn.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_o_p);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_op.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_q_r);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_qr.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_s);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_s.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_t_u);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_tu.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_v_z);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_vwxyz.add(data);
            }

            // masterData.addAll(Arrays.asList(lst_ab, lst_c, lst_de, lst_fgh, lst_ijkl, lst_mn, lst_op,lst_qr, lst_s,lst_tu, lst_vwxyz ));
            //masterData.add(lst_ab);


        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public boolean wordSearch(String key) {

        char firstChar = key.charAt(0);
        if (firstChar == 'a' || firstChar == 'b') {
            if (Collections.binarySearch(lst_ab, key) >= 0) {
                return true;
            } else return false;
        } else if (firstChar == 'C' || firstChar == 'c') {
            if (Collections.binarySearch(lst_c, key) >= 0) {
                return true;
            } else return false;
        } else if (firstChar == 'D' || firstChar == 'd' || firstChar == 'E' || firstChar == 'e') {
            if (Collections.binarySearch(lst_de, key) >= 0) {
                return true;
            } else return false;
        } else if (firstChar == 'F' || firstChar == 'f' || firstChar == 'G' || firstChar == 'g' || firstChar == 'H' || firstChar == 'h') {
            if (Collections.binarySearch(lst_fgh, key) >= 0) {
                return true;
            } else return false;
        } else if (firstChar == 'I' || firstChar == 'i' || firstChar == 'J' || firstChar == 'j' || firstChar == 'K' || firstChar == 'k' || firstChar == 'L' || firstChar == 'l') {
            if (Collections.binarySearch(lst_ijkl, key) >= 0) {
                return true;
            } else return false;
        } else if (firstChar == 'M' || firstChar == 'm' || firstChar == 'N' || firstChar == 'n') {
            if (Collections.binarySearch(lst_mn, key) >= 0) {
                return true;
            } else return false;
        } else if (firstChar == 'O' || firstChar == 'o' || firstChar == 'P' || firstChar == 'p') {
            if (Collections.binarySearch(lst_op, key) >= 0) {
                return true;
            } else return false;
        } else if (firstChar == 'Q' || firstChar == 'q' || firstChar == 'R' || firstChar == 'r') {
            if (Collections.binarySearch(lst_qr, key) >= 0) {
                return true;
            } else return false;
        } else if (firstChar == 'S' || firstChar == 's') {
            if (Collections.binarySearch(lst_s, key) >= 0) {
                return true;
            } else return false;
        } else if (firstChar == 'T' || firstChar == 't' || firstChar == 'U' || firstChar == 'u') {
            if (Collections.binarySearch(lst_tu, key) >= 0) {
                return true;
            } else return false;
        } else if (firstChar == 'V' || firstChar == 'v' || firstChar == 'W' || firstChar == 'w' || firstChar == 'X' || firstChar == 'x' || firstChar == 'Y' || firstChar == 'y' || firstChar == 'Z' || firstChar == 'z') {
            if (Collections.binarySearch(lst_vwxyz, key) >= 0) {
                return true;
            } else return false;

        } else return false;

    }

/*    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Stage2 Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://edu.neu.madcourse.com.scraggle.mayankranjandayal.scraggle/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Stage2 Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://edu.neu.madcourse.com.scraggle.mayankranjandayal.scraggle/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }*/
}
