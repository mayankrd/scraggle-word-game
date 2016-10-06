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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.mayankranjandayal.R;


public class TwoPlayerStage1 extends Activity {

    MediaPlayer mMediaPlayer;
    private AlertDialog mDialog;
    Firebase mRef;
    RemoteClient remoteClient;
    final Handler handler = new Handler();
    boolean gameStartStatus;

    ArrayList<String> inputWord = new ArrayList<>();
    int points, bonusPoints;
    char[][]words = new char[9][9];
    char[][]gameWords = new char[9][9];
    boolean[] wordSelected = new boolean[9]; // set true if a word is found in a 3x3 tile
    boolean[][] letterClicked = new boolean[9][9]; // if a letter was clicked in 9x9 board
    boolean[][] correctClicks = new boolean[9][9];
    int large, small;
    ArrayList<Integer> smallPos = new ArrayList<Integer>();
    ArrayList<Integer> largePos = new ArrayList<Integer>();


    static private int mLargeIds[] = {R.id.large1, R.id.large2, R.id.large3,
            R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8,
            R.id.large9,};
    static private int mSmallIds[] = {R.id.small1, R.id.small2, R.id.small3,
            R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8,
            R.id.small9,};

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

    ArrayList<ArrayList> masterData;

    TextView points_tv;

    ToneGenerator tone;
    CountDownTimer tmr;
    long miliSecsLeft;
    long timerDuration;
    boolean blink;
    Activity newObj;

    Firebase myFirebaseRef;
    String completeData;
    RemoteClient remote;
    static final String TAG = "Stage 1";
    HashMap<String,String> fireData;
    String player;

    Timer timer;
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_player_activity_stage1);
        this.loadData();
        remoteClient = new RemoteClient(this);
        if(DataHolder.getInstance().isContinueButtonClicked())
        {
            DataHolder.getInstance().setContinueButtonClicked(false);
            restoreGame();
            updateAllTiles(gameWords, wordSelected, letterClicked);
            Toast.makeText(this.getApplicationContext(), "Game Resumed !", Toast.LENGTH_SHORT).show();
        }
        else
        {
            newGame();
        }

        points_tv = (TextView)findViewById(R.id.points);
        points_tv.setText("Points: " + Integer.toString(points));

        // for beep sound
        tone = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

        // Music ON/ OFF code (Inside OnCreate)....
        final ToggleButton toggleMusicButton = (ToggleButton)this.findViewById(R.id.sound);
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


        newObj = this;

        //starting timer on create
        final Activity obj = this;
        stage1CountdownTimer(timerDuration, obj);

        //To read and write data from your Firebase database, we need to first create a reference to it.
        // We do this by passing the URL of your database into the Firebase constructor:

        remote = new RemoteClient(this);
        myFirebaseRef = new Firebase("https://fiery-torch-775.firebaseio.com/");


        // start/ resume game as per data loaded in resume or new game
        playGame();


        //****FIREBASE CODE STARTS***
        fireData = new HashMap<String,String>();
        Firebase.setAndroidContext(getApplicationContext());
        mRef = new Firebase("https://fiery-torch-775.firebaseio.com/");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                completeData = dataSnapshot.getValue().toString();
                Log.d(TAG, "*****Firebase Data...." + completeData);
                fillHashMap();
                printHashMap();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //****FIREBASE CODE ENDS***

        // set if the current player is player1 or player2
        // player1 starts the game
        setPlayer();


        Log.d(TAG+"*", String.valueOf(isGameStarted()));

    } // OcCreate ends here

    public void fillHashMap()
    {
        ArrayList<String> key_values = new ArrayList<String>();
        String[] ar;
        fireData.clear();
        for (String retval: completeData.split(", ")){
            key_values.add(retval);
        }
        for (String temp : key_values)
        {
            ar = null;
            ar = temp.split("=");
            fireData.put(ar[0], ar[1]);
        }
        key_values = null;
        ar = null;
        //Log.d("value from hash map...", fireData.get(username));

    }

    public String getValue(String key)
    {
        return fireData.get(key);
    }

    public boolean isGameStarted()
    {
        //printHashMap();
        //return Boolean.getBoolean(fireData.get("*gameStarted"));
        remoteClient.fetchValue("*gameStarted");

        // any polling mechanism can be used
        startTimer("*gameStarted");
        return gameStartStatus;


    }


    public void startTimer(String key) {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask(key);
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        // The values can be adjusted depending on the performance
        timer.schedule(timerTask, 5000, 1000);
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask(final String key) {
        timerTask = new TimerTask() {
            public void run() {
                Log.d(TAG, "isDataFetched >>>>" + remoteClient.isDataFetched());
                if(remoteClient.isDataFetched())
                {
                    handler.post(new Runnable() {

                        public void run() {
                            Log.d(TAG, "Value >>>>" + remoteClient.getValue(key));
                            gameStartStatus = Boolean.getBoolean(remoteClient.getValue(key));
                            //Toast.makeText(TwoPlayerStage1.this, "Value   " + remoteClient.getValue(key), Toast.LENGTH_SHORT).show();
                        }
                    });

                    stoptimertask();
                }

            }
        };
    }

    public void setPlayer()
    {
       /* if (isGameStarted()) {
            player = "player2";
        }
        else {
            player = "player1";
            setGameStartedStatus("true");
        }
        Toast.makeText(this.getApplicationContext(), "You are "+player+" !", Toast.LENGTH_SHORT).show();*/
    }

    public void printHashMap() {
        Iterator it = fireData.entrySet().iterator();
        System.out.println("**HASH MAP DATA");
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public void setGameStartedStatus(String status)
    {

        Firebase usersRef = new Firebase("https://fiery-torch-775.firebaseio.com/*gameStarted");
        usersRef.setValue(status, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Log.d(TAG, "Data could not be saved. " + firebaseError.getMessage());
                } else {
                    Log.d(TAG, "Data saved successfully.");
                }
            }
        });
    }



    public void saveGame()
    {
        System.out.println("SAVE GAME CALLED");
        DataHolder.getInstance().setWords(gameWords);
        DataHolder.getInstance().setPoints(points);
        DataHolder.getInstance().setBonusPoints(bonusPoints);
        DataHolder.getInstance().setCorrectClicks(correctClicks);
        DataHolder.getInstance().setInputWord(inputWord);
        DataHolder.getInstance().setWordSelected(wordSelected);
        DataHolder.getInstance().setLetterClicked(letterClicked);
        //DataHolder.getInstance().setMiliSecsLeft(miliSecsLeft);
        DataHolder.getInstance().setLarge(large);
        DataHolder.getInstance().setSmall(small);
        DataHolder.getInstance().setSmallPos(smallPos);
        DataHolder.getInstance().setLargePos(largePos);
        timerDuration = miliSecsLeft;
        //System.out.println("INSIDE SAVE GAME....TIMER DURATION TO BE SET IS: " + timerDuration + "..MILISECSLEFT: " + miliSecsLeft);
        DataHolder.getInstance().setTimerDuration(timerDuration);
    }

    public void restoreGame()
    {
        System.out.println("RESTORE GAME CALLED");
        DataHolder.getInstance().setContinueFlagStage1(false);
        gameWords = DataHolder.getInstance().getWords();
        points = DataHolder.getInstance().getPoints();
        bonusPoints = DataHolder.getInstance().getBonusPoints();
        correctClicks = DataHolder.getInstance().getCorrectClicks();
        inputWord = DataHolder.getInstance().getInputWord();
        wordSelected = DataHolder.getInstance().getWordSelected();
        letterClicked = DataHolder.getInstance().getLetterClicked();
        large = DataHolder.getInstance().getLarge();
        small = DataHolder.getInstance().getSmall();
        smallPos = DataHolder.getInstance().getSmallPos();
        largePos = DataHolder.getInstance().getLargePos();
        timerDuration = DataHolder.getInstance().getTimerDuration();
        System.out.println("TIMER DURATION: " + timerDuration);
    }

    public void newGame()
    {
        System.out.println("NEW GAME CALLED");
        resetDataHolder();
        initTileData();
        timerDuration = 90000;
        fillTiles();

    }

    public void  resetDataHolder()
    {
        DataHolder.getInstance().setContinueFlagStage1(false);
        DataHolder.getInstance().setContinueFlagStage2(false);
        DataHolder.getInstance().setWords(null);
        DataHolder.getInstance().setPoints(0);
        DataHolder.getInstance().setBonusPoints(0);
        DataHolder.getInstance().setCorrectClicks(null);
        DataHolder.getInstance().setInputWord(null);
        DataHolder.getInstance().setWordSelected(null);
        DataHolder.getInstance().setLetterClicked(null);
        DataHolder.getInstance().setMiliSecsLeft(0);
        DataHolder.getInstance().setLarge(0);
        DataHolder.getInstance().setSmall(0);
        DataHolder.getInstance().setSmallPos(null);
        DataHolder.getInstance().setLargePos(null);
    }

    public void pauseGame(View view)
    {
        System.out.println("MLILISECSLEFT INSIDE PAUSE GAME before cancelling timer: "+miliSecsLeft);
        tmr.cancel();
        final FrameLayout frl = (FrameLayout) findViewById(R.id.stage1_layout);
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
                        stage1CountdownTimer(miliSecsLeft, newObj);
                    }
                });
        mDialog = builder.show();
    }


    public void restartGame(View view){

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
        Intent intent = new Intent(this, edu.neu.madcourse.mayankranjandayal.two_player_word_game.two_player.TwoPlayerStage1.class);
        startActivity(intent);

    }

    public void showHome(View view){

        saveGame();
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
        DataHolder.getInstance().setContinueFlagStage1(true);
        Intent intent = new Intent(this, TwoPlayerHome.class);
        startActivity(intent);

        // save game state here

    }

    public void stage1CountdownTimer(long duration, final Activity obj)
    {
        final int timeBlinkMiliSecs = 10000;
        final TextView timer;
        timer=(TextView) findViewById(R.id.timer);
        tmr = new CountDownTimer(duration, 1000) {

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
            }

            public void onFinish() {

                System.out.println("STAGE 1 FINISH CALLED");
                timer.setText("Time Over !");
                DataHolder.getInstance().setWords(gameWords);
                DataHolder.getInstance().setCorrectClicks(correctClicks);
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
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                mMediaPlayer.release();
                Intent intent = new Intent(obj, TwoPlayerStage2.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    public void checkWord(View view){

        // inputWord = DataHolder.getInstance().getArl();
        if(inputWord != null) {
            String word = "";
            for (String s : inputWord) {
                word += s;
            }

            if (word.length() > 0) {
                if (wordSearch(word)) {
                    Toast.makeText(this.getApplicationContext(), "Correct Word !", Toast.LENGTH_SHORT).show();
                    points = points + word.length();
                    if(word.length() == 9)
                        bonusPoints = bonusPoints + 1;
                    DataHolder.getInstance().setPoints(points);
                    DataHolder.getInstance().setBonusPoints(bonusPoints);
                    points_tv.setText("Points: " + Integer.toString(points));
                    for(int i=1; i<largePos.size(); i++)
                    {
                        correctClicks[largePos.get(i)][smallPos.get(i)] =true;
                    }

                    wordSelected[largePos.get(1)] = true;
                    updateAllTiles(gameWords, wordSelected, letterClicked);

                    //putting data to firebase
                    remote.saveValue("Points Player 1", Integer.toString(points));

                    initTileData();

                } else {
                    Toast.makeText(this.getApplicationContext(), "InCorrect Word !", Toast.LENGTH_SHORT).show();
                    for(int i=1; i<largePos.size(); i++)
                    {
                        letterClicked[largePos.get(i)][smallPos.get(i)] = false;
                    }
                    updateAllTiles(gameWords, wordSelected, letterClicked);
                    initTileData();
                }
            }
            else{}
        }
        else {
            Toast.makeText(this.getApplicationContext(), "Please select a word !", Toast.LENGTH_SHORT).show();
        }

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

                        if(isAdjacent(lrOld, smOld, large, small)) {

                            //inner1.setBackgroundColor(Color.RED);
                            inner1.setTextColor(Color.RED);

                            letterClicked[large][small] = true;
                            inputWord.add(inner1.getText().toString());
                            smallPos.add(small);
                            largePos.add(large);
                            tone.startTone(ToneGenerator.TONE_PROP_BEEP);

                        }
                        else if (smOld == small && lrOld == large)
                        {
                            inputWord.remove(inputWord.size() - 1);
                            smallPos.remove(smallPos.size() - 1);
                            largePos.remove(largePos.size() - 1);
                            inner1.setTextColor(Color.WHITE);
                            letterClicked[large][small] = false;

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Not an adjacent letter", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        }
    }

    public void initTileData()
    {
        inputWord=null;
        smallPos= null;
        largePos=null;
        inputWord = new ArrayList<String>();
        smallPos = new ArrayList<Integer>();
        largePos = new ArrayList<Integer>();
        smallPos.add(99);
        largePos.add(99);
    }



    private void updateAllTiles(char[][] words, boolean[] wordSelected, boolean[][] letterClicked) // words here refer to gameWords
    {
        for (int large = 0; large < 9; large++) {
            View outer = this.findViewById(mLargeIds[large]);

            for (int small = 0; small < 9; small++) {

                final Button inner1 = (Button) outer.findViewById(mSmallIds[small]);
                inner1.setText(Character.toString(words[large][small]));

                // buttonState[large][small] = true;

                if(letterClicked[large][small] == true)
                    inner1.setTextColor(Color.RED);
                else
                    inner1.setTextColor(Color.WHITE);

                if(wordSelected[large] == true)
                {
                    inner1.setEnabled(false);
                    inner1.setBackgroundColor(Color.GREEN);
                }

            }}
    }

    private void fillTiles(){

        ArrayList<String> nine = this.getNineLengthWords(lst_ab);
        int min=0;
        int max=30;
        int ran = 0;
        Random rn = new Random();

        for (int i=0; i<9; i++)
        {
            ran = rn.nextInt(max - min + 1) + min;
            words[i] = nine.get(ran).toString().toCharArray();
        }
       /*
        words = this.swapChars(words);
*//*      Random r = new Random();
      int ran = r.nextInt(20);*//*
        for (int large = 0; large < 9; large++) {
            View outer = this.findViewById(mLargeIds[large]);

            for (int small = 0; small < 9; small++) {

                final Button inner1 = (Button) outer.findViewById(mSmallIds[small]);
                inner1.setText(Character.toString(words[large][small]));
                buttonState[large][small] = true;
            }}*/


        //pattern 1
        View outer0 = this.findViewById(mLargeIds[0]);
        final Button inner00 = (Button) outer0.findViewById(mSmallIds[0]);
        gameWords[0][0] = words[0][0];
        inner00.setText(Character.toString(words[0][0]));

        final Button inner01 = (Button) outer0.findViewById(mSmallIds[1]);
        gameWords[0][1] = words[0][5];
        inner01.setText(Character.toString(words[0][5]));
        final Button inner02 = (Button) outer0.findViewById(mSmallIds[2]);
        gameWords[0][2] = words[0][6];
        inner02.setText(Character.toString(words[0][6]));
        final Button inner03 = (Button) outer0.findViewById(mSmallIds[3]);
        gameWords[0][3] = words[0][1];
        inner03.setText(Character.toString(words[0][1]));
        final Button inner04 = (Button) outer0.findViewById(mSmallIds[4]);
        gameWords[0][4] = words[0][4];
        inner04.setText(Character.toString(words[0][4]));
        final Button inner05 = (Button) outer0.findViewById(mSmallIds[5]);
        gameWords[0][5] = words[0][7];
        inner05.setText(Character.toString(words[0][7]));
        final Button inner06 = (Button) outer0.findViewById(mSmallIds[6]);
        gameWords[0][6] = words[0][2];
        inner06.setText(Character.toString(words[0][2]));
        final Button inner07 = (Button) outer0.findViewById(mSmallIds[7]);
        gameWords[0][7] = words[0][3];
        inner07.setText(Character.toString(words[0][3]));
        final Button inner08 = (Button) outer0.findViewById(mSmallIds[8]);
        gameWords[0][8] = words[0][8];
        inner08.setText(Character.toString(words[0][8]));

        //pattern 2
        View outer1 = this.findViewById(mLargeIds[1]);
        final Button inner10 = (Button) outer1.findViewById(mSmallIds[0]);
        gameWords[1][0] = words[1][8];
        inner10.setText(Character.toString(words[1][8]));
        final Button inner11 = (Button) outer1.findViewById(mSmallIds[1]);
        gameWords[1][1] = words[1][5];
        inner11.setText(Character.toString(words[1][5]));
        final Button inner12 = (Button) outer1.findViewById(mSmallIds[2]);
        gameWords[1][2] = words[1][4];
        inner12.setText(Character.toString(words[1][4]));
        final Button inner13 = (Button) outer1.findViewById(mSmallIds[3]);
        gameWords[1][3] = words[1][7];
        inner13.setText(Character.toString(words[1][7]));
        final Button inner14 = (Button) outer1.findViewById(mSmallIds[4]);
        gameWords[1][4] = words[1][6];
        inner14.setText(Character.toString(words[1][6]));
        final Button inner15 = (Button) outer1.findViewById(mSmallIds[5]);
        gameWords[1][5] = words[1][3];
        inner15.setText(Character.toString(words[1][3]));
        final Button inner16 = (Button) outer1.findViewById(mSmallIds[6]);
        gameWords[1][6] = words[1][0];
        inner16.setText(Character.toString(words[1][0]));
        final Button inner17 = (Button) outer1.findViewById(mSmallIds[7]);
        gameWords[1][7] = words[1][1];
        inner17.setText(Character.toString(words[1][1]));
        final Button inner18 = (Button) outer1.findViewById(mSmallIds[8]);
        gameWords[1][8] = words[1][2];
        inner18.setText(Character.toString(words[1][2]));

        //pattern 3
        View outer2 = this.findViewById(mLargeIds[2]);
        final Button inner20 = (Button) outer2.findViewById(mSmallIds[0]);
        gameWords[2][0] = words[2][2];
        inner20.setText(Character.toString(words[2][2]));
        final Button inner21 = (Button) outer2.findViewById(mSmallIds[1]);
        gameWords[2][1] = words[2][3];
        inner21.setText(Character.toString(words[2][3]));
        final Button inner22 = (Button) outer2.findViewById(mSmallIds[2]);
        gameWords[2][2] = words[2][4];
        inner22.setText(Character.toString(words[2][4]));
        final Button inner23 = (Button) outer2.findViewById(mSmallIds[3]);
        gameWords[2][3] = words[2][1];
        inner23.setText(Character.toString(words[2][1]));
        final Button inner24 = (Button) outer2.findViewById(mSmallIds[4]);
        gameWords[2][4] = words[2][0];
        inner24.setText(Character.toString(words[2][0]));
        final Button inner25 = (Button) outer2.findViewById(mSmallIds[5]);
        gameWords[2][5] = words[2][5];
        inner25.setText(Character.toString(words[2][5]));
        final Button inner26 = (Button) outer2.findViewById(mSmallIds[6]);
        gameWords[2][6] = words[2][8];
        inner26.setText(Character.toString(words[2][8]));
        final Button inner27 = (Button) outer2.findViewById(mSmallIds[7]);
        gameWords[2][7] = words[2][7];
        inner27.setText(Character.toString(words[2][7]));
        final Button inner28 = (Button) outer2.findViewById(mSmallIds[8]);
        gameWords[2][8] = words[2][6];
        inner28.setText(Character.toString(words[2][6]));

        //pattern 3
        View outer3 = this.findViewById(mLargeIds[3]);
        final Button inner30 = (Button) outer3.findViewById(mSmallIds[0]);
        gameWords[3][0] = words[3][2];
        inner30.setText(Character.toString(words[3][2]));
        final Button inner31 = (Button) outer3.findViewById(mSmallIds[1]);
        gameWords[3][1] = words[3][3];
        inner31.setText(Character.toString(words[3][3]));
        final Button inner32 = (Button) outer3.findViewById(mSmallIds[2]);
        gameWords[3][2] = words[3][4];
        inner32.setText(Character.toString(words[3][4]));
        final Button inner33 = (Button) outer3.findViewById(mSmallIds[3]);
        gameWords[3][3] = words[3][1];
        inner33.setText(Character.toString(words[3][1]));
        final Button inner34 = (Button) outer3.findViewById(mSmallIds[4]);
        gameWords[3][4] = words[3][0];
        inner34.setText(Character.toString(words[3][0]));
        final Button inner35 = (Button) outer3.findViewById(mSmallIds[5]);
        gameWords[3][5] = words[3][5];
        inner35.setText(Character.toString(words[3][5]));
        final Button inner36 = (Button) outer3.findViewById(mSmallIds[6]);
        gameWords[3][6] = words[3][8];
        inner36.setText(Character.toString(words[3][8]));
        final Button inner37 = (Button) outer3.findViewById(mSmallIds[7]);
        gameWords[3][7] = words[3][7];
        inner37.setText(Character.toString(words[3][7]));
        final Button inner38 = (Button) outer3.findViewById(mSmallIds[8]);
        gameWords[3][8] = words[3][6];
        inner38.setText(Character.toString(words[3][6]));

        //pattern 1
        View outer4 = this.findViewById(mLargeIds[4]);
        final Button inner40 = (Button) outer4.findViewById(mSmallIds[0]);
        gameWords[4][0] = words[4][0];
        inner40.setText(Character.toString(words[4][0]));
        final Button inner41 = (Button) outer4.findViewById(mSmallIds[1]);
        gameWords[4][1] = words[4][5];
        inner41.setText(Character.toString(words[4][5]));
        final Button inner42 = (Button) outer4.findViewById(mSmallIds[2]);
        gameWords[4][2] = words[4][6];
        inner42.setText(Character.toString(words[4][6]));
        final Button inner43 = (Button) outer4.findViewById(mSmallIds[3]);
        gameWords[4][3] = words[4][1];
        inner43.setText(Character.toString(words[4][1]));
        final Button inner44 = (Button) outer4.findViewById(mSmallIds[4]);
        gameWords[4][4] = words[4][4];
        inner44.setText(Character.toString(words[4][4]));
        final Button inner45 = (Button) outer4.findViewById(mSmallIds[5]);
        gameWords[4][5] = words[4][7];
        inner45.setText(Character.toString(words[4][7]));
        final Button inner46 = (Button) outer4.findViewById(mSmallIds[6]);
        gameWords[4][6] = words[4][2];
        inner46.setText(Character.toString(words[4][2]));
        final Button inner47 = (Button) outer4.findViewById(mSmallIds[7]);
        gameWords[4][7] = words[4][3];
        inner47.setText(Character.toString(words[4][3]));
        final Button inner48 = (Button) outer4.findViewById(mSmallIds[8]);
        gameWords[4][8] = words[4][8];
        inner48.setText(Character.toString(words[4][8]));

        //pattern 2
        View outer5 = this.findViewById(mLargeIds[5]);
        final Button inner50 = (Button) outer5.findViewById(mSmallIds[0]);
        gameWords[5][0] = words[5][8];
        inner50.setText(Character.toString(words[5][8]));
        final Button inner51 = (Button) outer5.findViewById(mSmallIds[1]);
        gameWords[5][1] = words[5][5];
        inner51.setText(Character.toString(words[5][5]));
        final Button inner52 = (Button) outer5.findViewById(mSmallIds[2]);
        gameWords[5][2] = words[5][4];
        inner52.setText(Character.toString(words[5][4]));
        final Button inner53 = (Button) outer5.findViewById(mSmallIds[3]);
        gameWords[5][3] = words[5][7];
        inner53.setText(Character.toString(words[5][7]));
        final Button inner54 = (Button) outer5.findViewById(mSmallIds[4]);
        gameWords[5][4] = words[5][6];
        inner54.setText(Character.toString(words[5][6]));
        final Button inner55 = (Button) outer5.findViewById(mSmallIds[5]);
        gameWords[5][5] = words[5][3];
        inner55.setText(Character.toString(words[5][3]));
        final Button inner56 = (Button) outer5.findViewById(mSmallIds[6]);
        gameWords[5][6] = words[5][0];
        inner56.setText(Character.toString(words[5][0]));
        final Button inner57 = (Button) outer5.findViewById(mSmallIds[7]);
        gameWords[5][7] = words[5][1];
        inner57.setText(Character.toString(words[5][1]));
        final Button inner58 = (Button) outer5.findViewById(mSmallIds[8]);
        gameWords[5][8] = words[5][2];
        inner58.setText(Character.toString(words[5][2]));


        //pattern 2
        View outer6 = this.findViewById(mLargeIds[6]);
        final Button inner60 = (Button) outer6.findViewById(mSmallIds[0]);
        gameWords[6][0] = words[6][8];
        inner60.setText(Character.toString(words[6][8]));
        final Button inner61 = (Button) outer6.findViewById(mSmallIds[1]);
        gameWords[6][1] = words[6][5];
        inner61.setText(Character.toString(words[6][5]));
        final Button inner62 = (Button) outer6.findViewById(mSmallIds[2]);
        gameWords[6][2] = words[6][4];
        inner62.setText(Character.toString(words[6][4]));
        final Button inner63 = (Button) outer6.findViewById(mSmallIds[3]);
        gameWords[6][3] = words[6][7];
        inner63.setText(Character.toString(words[6][7]));
        final Button inner64 = (Button) outer6.findViewById(mSmallIds[4]);
        gameWords[6][4] = words[6][6];
        inner64.setText(Character.toString(words[6][6]));
        final Button inner65 = (Button) outer6.findViewById(mSmallIds[5]);
        gameWords[6][5] = words[6][3];
        inner65.setText(Character.toString(words[6][3]));
        final Button inner66 = (Button) outer6.findViewById(mSmallIds[6]);
        gameWords[6][6] = words[6][0];
        inner66.setText(Character.toString(words[6][0]));
        final Button inner67 = (Button) outer6.findViewById(mSmallIds[7]);
        gameWords[6][7] = words[6][1];
        inner67.setText(Character.toString(words[6][1]));
        final Button inner68 = (Button) outer6.findViewById(mSmallIds[8]);
        gameWords[6][8] = words[6][2];
        inner68.setText(Character.toString(words[6][2]));


        //pattern 1
        View outer7 = this.findViewById(mLargeIds[7]);
        final Button inner70 = (Button) outer7.findViewById(mSmallIds[0]);
        gameWords[7][0] = words[7][0];
        inner70.setText(Character.toString(words[7][0]));
        final Button inner71 = (Button) outer7.findViewById(mSmallIds[1]);
        gameWords[7][1] = words[7][5];
        inner71.setText(Character.toString(words[7][5]));
        final Button inner72 = (Button) outer7.findViewById(mSmallIds[2]);
        gameWords[7][2] = words[7][6];
        inner72.setText(Character.toString(words[7][6]));
        final Button inner73 = (Button) outer7.findViewById(mSmallIds[3]);
        gameWords[7][3] = words[7][1];
        inner73.setText(Character.toString(words[7][1]));
        final Button inner74 = (Button) outer7.findViewById(mSmallIds[4]);
        gameWords[7][4] = words[7][4];
        inner74.setText(Character.toString(words[7][4]));
        final Button inner75 = (Button) outer7.findViewById(mSmallIds[5]);
        gameWords[7][5] = words[7][7];
        inner75.setText(Character.toString(words[7][7]));
        final Button inner76 = (Button) outer7.findViewById(mSmallIds[6]);
        gameWords[7][6] = words[7][2];
        inner76.setText(Character.toString(words[7][2]));
        final Button inner77 = (Button) outer7.findViewById(mSmallIds[7]);
        gameWords[7][7] = words[7][3];
        inner77.setText(Character.toString(words[7][3]));
        final Button inner78 = (Button) outer7.findViewById(mSmallIds[8]);
        gameWords[7][8] = words[7][8];
        inner78.setText(Character.toString(words[7][8]));


        //pattern 3
        View outer8 = this.findViewById(mLargeIds[8]);
        final Button inner80 = (Button) outer8.findViewById(mSmallIds[0]);
        gameWords[8][0] = words[8][2];
        inner80.setText(Character.toString(words[8][2]));
        final Button inner81 = (Button) outer8.findViewById(mSmallIds[1]);
        gameWords[8][1] = words[8][3];
        inner81.setText(Character.toString(words[8][3]));
        final Button inner82 = (Button) outer8.findViewById(mSmallIds[2]);
        gameWords[8][2] = words[8][4];
        inner82.setText(Character.toString(words[8][4]));
        final Button inner83 = (Button) outer8.findViewById(mSmallIds[3]);
        gameWords[8][3] = words[8][1];
        inner83.setText(Character.toString(words[8][1]));
        final Button inner84 = (Button) outer8.findViewById(mSmallIds[4]);
        gameWords[8][4] = words[8][0];
        inner84.setText(Character.toString(words[8][0]));
        final Button inner85 = (Button) outer8.findViewById(mSmallIds[5]);
        gameWords[8][5] = words[8][5];
        inner85.setText(Character.toString(words[8][5]));
        final Button inner86 = (Button) outer8.findViewById(mSmallIds[6]);
        gameWords[8][6] = words[8][8];
        inner86.setText(Character.toString(words[8][8]));
        final Button inner87 = (Button) outer8.findViewById(mSmallIds[7]);
        gameWords[8][7] = words[8][7];
        inner87.setText(Character.toString(words[8][7]));
        final Button inner88 = (Button) outer8.findViewById(mSmallIds[8]);
        gameWords[8][8] = words[8][6];
        inner88.setText(Character.toString(words[8][6]));

    }

    public char[][] swapChars(char[][] chr)
    {
        char temp;
        ArrayList<String> nine = new ArrayList<String>();
        for (int j=0; j<chr.length; j++)
        {
            temp = chr[j][3];
            chr[j][3] = chr[j][5];
            chr[j][5] = temp;
        }
        return chr;
    }

    public ArrayList getNineLengthWords(ArrayList ar)
    {
        int i=0, j=0;
        // this.nineLenghtWords=ar;

        ArrayList<String> nine = new ArrayList<String>();
        for (j=0; j<ar.size(); j++)
        {
            if (ar.get(j).toString().length()==9)
                nine.add(ar.get(j).toString());
        }
        return nine;

    }

    public void loadData()
    {
        try {
            String data;
            InputStream ins;
            BufferedReader reader;

            this.lst_ab =  new ArrayList<String>();
            this.lst_c =  new ArrayList<String>();
            this.lst_de =  new ArrayList<String>();
            this.lst_fgh =  new ArrayList<String>();
            this.lst_ijkl = new ArrayList<String>();
            this.lst_mn = new ArrayList<String>();
            this.lst_op = new ArrayList<String>();
            this.lst_qr = new ArrayList<String>();
            this.lst_s = new ArrayList<String>();
            this.lst_tu = new ArrayList<String>();
            this.lst_vwxyz = new ArrayList<String>();
            this.masterData = new ArrayList<ArrayList>();

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

    public boolean isAdjacent(int largeOld, int smallOld, int largeNew, int smallNew)
    {
        if((largeNew == largeOld) || (largeOld == 99)) {
            switch (smallOld) {

                case 0:
                    if (smallNew == 1 || smallNew == 4 || smallNew == 3)
                        return true;
                    else return false;

                case 1:
                    if (smallNew == 2 || smallNew == 4 || smallNew == 3 || smallNew == 5 || smallNew == 0)
                        return true;
                    else return false;

                case 2:
                    if (smallNew == 5 || smallNew == 4 || smallNew == 1)
                        return true;
                    else return false;

                case 3:
                    if (smallNew == 7 || smallNew == 4 || smallNew == 6 || smallNew == 0 || smallNew == 1)
                        return true;
                    else return false;

                case 4:
                    if (smallNew == 7 || smallNew == 8 || smallNew == 5 || smallNew == 6 || smallNew == 3 || smallNew == 0 || smallNew == 1 || smallNew == 2)
                        return true;
                    else return false;

                case 5:
                    if (smallNew == 1 || smallNew == 2 || smallNew == 4 || smallNew == 7 || smallNew == 8)
                        return true;
                    else return false;

                case 6:
                    if (smallNew == 3 || smallNew == 7 || smallNew == 4)
                        return true;
                    else return false;

                case 7:
                    if (smallNew == 3 || smallNew == 5 || smallNew == 4 || smallNew == 6 || smallNew == 8)
                        return true;
                    else return false;

                case 8:
                    if (smallNew == 7 || smallNew == 5 || smallNew == 4)
                        return true;

                case 99:
                    return true;

                default: return false;
            }
        }
        else return false;

    }
}
