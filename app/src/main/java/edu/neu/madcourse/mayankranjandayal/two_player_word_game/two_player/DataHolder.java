package edu.neu.madcourse.mayankranjandayal.two_player_word_game.two_player;

import java.util.ArrayList;

import edu.neu.madcourse.mayankranjandayal.scraggle.GameFragment;

/**
 * Created by Mayank RD on 2/23/2016.
 */
public class DataHolder {
    private static DataHolder dataObject = null;

    private DataHolder() {
        // left blank intentionally
    }

    public static DataHolder getInstance() {
        if (dataObject == null)
            dataObject = new DataHolder();
        return dataObject;
    }

    //**********new data variables******************

    private char[][] words;
    private boolean[][] correctClicks;
    private int points;
    private int bonusPoints;
    private boolean continueFlagStage1;
    private boolean continueFlagStage2;


    private boolean continueButtonClicked;
    private long timerDuration;

    private ArrayList<String> inputWord;
    private boolean[] wordSelected;
    private boolean[][] letterClicked;
    private long miliSecsLeft;
    int large, small;
    private ArrayList<Integer> smallPos;
    private ArrayList<Integer> largePos;

    private boolean[][] stage2LetterClicked;
    private ArrayList<String> inputWordStage2;
    private long stage2TimerDuration;


    //**********new data getters and setters*************

    public long getStage2TimerDuration() {
        return stage2TimerDuration;
    }

    public void setStage2TimerDuration(long stage2TimerDuration) {
        this.stage2TimerDuration = stage2TimerDuration;
    }


    public ArrayList<String> getInputWordStage2() {
        return inputWordStage2;
    }

    public void setInputWordStage2(ArrayList<String> inputWordStage2) {
        this.inputWordStage2 = inputWordStage2;
    }

    public boolean[][] getStage2LetterClicked() {
        return stage2LetterClicked;
    }

    public void setStage2LetterClicked(boolean[][] stage2LetterClicked) {
        this.stage2LetterClicked = stage2LetterClicked;
    }

    public boolean isContinueButtonClicked() {
        return continueButtonClicked;
    }

    public void setContinueButtonClicked(boolean continueButtonClicked) {
        this.continueButtonClicked = continueButtonClicked;
    }

    public long getTimerDuration() {
        return timerDuration;
    }

    public void setTimerDuration(long timerDuration) {
        this.timerDuration = timerDuration;
    }


    public ArrayList<String> getInputWord() {
        return inputWord;
    }

    public void setInputWord(ArrayList<String> inputWord) {
        this.inputWord = inputWord;
    }

    public boolean[] getWordSelected() {
        return wordSelected;
    }

    public void setWordSelected(boolean[] wordSelected) {
        this.wordSelected = wordSelected;
    }

    public boolean[][] getLetterClicked() {
        return letterClicked;
    }

    public void setLetterClicked(boolean[][] letterClicked) {
        this.letterClicked = letterClicked;
    }

    public long getMiliSecsLeft() {
        return miliSecsLeft;
    }

    public void setMiliSecsLeft(long miliSecsLeft) {
        this.miliSecsLeft = miliSecsLeft;
    }

    public int getLarge() {
        return large;
    }

    public void setLarge(int large) {
        this.large = large;
    }

    public int getSmall() {
        return small;
    }

    public void setSmall(int small) {
        this.small = small;
    }

    public ArrayList<Integer> getSmallPos() {
        return smallPos;
    }

    public void setSmallPos(ArrayList<Integer> smallPos) {
        this.smallPos = smallPos;
    }

    public ArrayList<Integer> getLargePos() {
        return largePos;
    }

    public void setLargePos(ArrayList<Integer> largePos) {
        this.largePos = largePos;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(int bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public char[][] getWords() {
        return words;
    }

    public void setWords(char[][] words) {
        this.words = words;
    }

    public boolean[][] getCorrectClicks() {
        return correctClicks;
    }

    public void setCorrectClicks(boolean[][] correctClicks) {
        this.correctClicks = correctClicks;
    }

    public boolean isContinueFlagStage1() {
        return continueFlagStage1;
    }

    public void setContinueFlagStage1(boolean continueFlagStage1) {
        this.continueFlagStage1 = continueFlagStage1;
    }

    public boolean isContinueFlagStage2() {
        return continueFlagStage2;
    }

    public void setContinueFlagStage2(boolean continueFlagStage2) {
        this.continueFlagStage2 = continueFlagStage2;
    }



    //*************************************************************************
    private ArrayList<String> arl;
    private String distributor_id;

    private GameFragment controlObj;

    public GameFragment getControlObj() {
        return controlObj;
    }

    public void setControlObj(GameFragment controlObj) {
        this.controlObj = controlObj;
    }



    public ArrayList<String> getArl() {
        return arl;
    }

    public void setArl(ArrayList<String> arl) {
        this.arl = arl;
    }

    public String getDistributor_id() {
        return distributor_id;
    }

    public void setDistributor_id(String distributor_id) {
        this.distributor_id = distributor_id;
    }
}
