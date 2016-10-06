package edu.neu.madcourse.mayankranjandayal.two_player_word_game.two_player;

public class CommunicationConstants
{
    public static final String TAG = "GCM_Globals";
    public static final String GCM_SENDER_ID = "411047697621";
    public static final String BASE_URL = "https://android.googleapis.com/gcm/send";
    public static final String PREFS_NAME = "GCM_Communication";

    // replace it with your own gcm api key here
    public static final String GCM_API_KEY = "AIzaSyC1qwgCiir1vJrF5QJEWM2fL8mYWhaTliE";
    public static final int SIMPLE_NOTIFICATION = 22;
    public static final long GCM_TIME_TO_LIVE = 60L * 60L * 24L * 7L * 4L; // 4 Weeks
    public static int mode = 0;

    public static String alertText = "";
    public static String titleText = "";
    public static String contentText = "";
}