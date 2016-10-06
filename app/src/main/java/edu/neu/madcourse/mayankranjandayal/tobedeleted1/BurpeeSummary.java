package edu.neu.madcourse.mayankranjandayal.tobedeleted1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;

import edu.neu.madcourse.mayankranjandayal.R;

public class BurpeeSummary extends Activity {
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    MessageDialog messageDialog;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String currentBurpeeCount = "currBurCnt";
    public static final String lifetimeBurpeeCount = "lifeBurCnt";
    TextView tv_currCnt;
    TextView tv_lifeCnt;
    int currBurrCnt;
    int lifeBurrCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        super.onCreate(savedInstanceState);

        //AppEventsLogger.activateApp(this);
        // accessToken = AccessToken.getCurrentAccessToken();
        setContentView(R.layout.burpee_activity_summary);

        manageBurpeeCounts();

        //ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
        //shareButton.setShareContent(content);
        //shareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();
        // shareDialog.show(this, content);
        //MessageDialog.show(this, content);*/

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void manageBurpeeCounts()
    {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        currBurrCnt = Integer.parseInt(sharedPreferences.getString(currentBurpeeCount, Integer.toString(0)));
        lifeBurrCnt = Integer.parseInt(sharedPreferences.getString(lifetimeBurpeeCount, Integer.toString(0)));
        tv_currCnt = (TextView) findViewById(R.id.textView_burpeeCount);
        tv_lifeCnt = (TextView) findViewById(R.id.textView_lifetimeCount);
        tv_currCnt.setText(Integer.toString(currBurrCnt));
        tv_lifeCnt.setText(Integer.toString(lifeBurrCnt));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(currentBurpeeCount, "0");
        editor.commit();

    }

    public void share2fb(View view) {
        if(haveNetworkConnection()!= false) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://facebook.com"))
                    .setContentTitle("Take The BURPEE CHALLENGE")
                    .setContentDescription("Hey, I did "+currBurrCnt+" Burpees in a go and "+lifeBurrCnt+" Burpees in total. I challenge you to break my record. Download the 'Burpee Challenge' app now from Play Store.")
                    .build();

            shareDialog.show(this, content);

        }
        else
            Toast.makeText(this, "No Internet Connection !",
                    Toast.LENGTH_LONG).show();

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void retryGame(View v){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(currentBurpeeCount, "0");
        editor.commit();
        Intent intent = new Intent(this, BurpeeGetSet.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }
}