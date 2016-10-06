package edu.neu.madcourse.mayankranjandayal.two_player_word_game.two_player;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.mayankranjandayal.R;

public class TwoPlayerUserSelection extends ListActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String PROPERTY_ALERT_TEXT = "alertText";
    public static final String PROPERTY_TITLE_TEXT = "titleText";
    public static final String PROPERTY_CONTENT_TEXT = "contentText";
    public static final String PROPERTY_NTYPE = "nType";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static final String TAG = "User Selection";
    TextView mDisplay;
    EditText mMessage;
    GoogleCloudMessaging gcm;
    SharedPreferences prefs;
    Context context;
    String regid;

    Firebase mRef;
    String completeData;

    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_player_activity_user_selection);
        mDisplay = (TextView) findViewById(R.id.communication_display);
        mMessage = (EditText) findViewById(R.id.communication_edit_message);
        gcm = GoogleCloudMessaging.getInstance(this);
        context = getApplicationContext();
        //To read and write data from your Firebase database, we need to first create a reference to it.
        // We do this by passing the URL of your database into the Firebase constructor:
        //****FIREBASE CODE STARTS***
        Firebase.setAndroidContext(context);
        mRef = new Firebase("https://fiery-torch-775.firebaseio.com/");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                completeData = dataSnapshot.getValue().toString();
                Log.d(TAG, "*****Firebase Data...." + completeData);
                populateUserList();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //****FIREBASE CODE ENDS***

        //****LISTVIEW CODE STARTS***

        listItems = new ArrayList<String>();
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        setListAdapter(adapter);

        ListView listview = (ListView) findViewById(android.R.id.list);
        listview.setOnItemClickListener(this);

    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i("FireBase DATA ListView", "You clicked Item: " + id + " at position:" + position);
        TextView tv = (TextView) v;
        String usernameClicked = tv.getText().toString();
        Log.d("Clicked on: ", usernameClicked);

        String message = ((EditText) findViewById(R.id.communication_edit_message))
                .getText().toString();

        if (message != "") {
            sendMessage(message, getRegID(usernameClicked));

            //starting a new game with the selected user

            startNewGame();

        } else {
            Toast.makeText(context, "Sending Context Empty!",
            Toast.LENGTH_LONG).show();
        }
    }

    public void startNewGame()
    {
        DataHolder.getInstance().setContinueButtonClicked(false);
        Intent intent = new Intent(this , TwoPlayerStage1.class);
        startActivity(intent);
        finish();

    }

    public String getRegID(String username)
    {
        HashMap<String,String> fireData = new HashMap<String,String>();
        ArrayList<String> key_values = new ArrayList<String>();
        String[] ar;
        for (String retval: completeData.split(", ")){
            key_values.add(retval);
        }
        for (String temp : key_values)
        {
            ar = null;
            ar = temp.split("=");
            fireData.put(ar[0], ar[1]);
        }
        //Log.d("value from hash map...", fireData.get(username));
        return fireData.get(username);
    }


    @SuppressLint("NewApi")
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        Log.i(TAG, String.valueOf(registeredVersion));
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(TwoPlayerUserSelection.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

/*    private static void setRegisterValues() {
        CommunicationConstants.alertText = "Register Notification";
        CommunicationConstants.titleText = "Register";
        CommunicationConstants.contentText = "Registering Successful!";
    }

    private static void setUnregisterValues() {
        CommunicationConstants.alertText = "Unregister Notification";
        CommunicationConstants.titleText = "Unregister";
        CommunicationConstants.contentText = "Unregistering Successful!";
    }

    private static void setSendMessageValues(String msg) {
        CommunicationConstants.alertText = "Message Notification";
        CommunicationConstants.titleText = "Sending Message";
        CommunicationConstants.contentText = msg;
    }*/

    private void registerInBackground(final String userName) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    //setRegisterValues();
                    regid = gcm.register(CommunicationConstants.GCM_SENDER_ID);


                    // implementation to store and keep track of registered devices here
                    //putting data to firebase
                    addUserToFireBase(userName, regid);



                    msg = "Device registered, registration ID=" + regid;
                    sendRegistrationIdToBackend();
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend() {
        // Your implementation here.
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onClick(final View view) {

		/*if (view == findViewById(R.id.communication_send)) {
			String message = ((EditText) findViewById(R.id.communication_edit_message))
					.getText().toString();

			if (message != "") {
				sendMessage(message);
			} else {
				Toast.makeText(context, "Sending Context Empty!",
						Toast.LENGTH_LONG).show();
			}
		}*/ if (view == findViewById(R.id.communication_clear)) {
            mMessage.setText("");
        } else if (view == findViewById(R.id.communication_unregistor_button)) {
            unregister();
        } else if (view == findViewById(R.id.communication_registor_button)) {
            if (checkPlayServices()) {
                regid = getRegistrationId(context);
                if (TextUtils.isEmpty(regid)) {
                    String username = ((EditText) findViewById(R.id.communication_edit_username))
                            .getText().toString();
                    registerInBackground(username);
                }
            }
        }

    }

    private void unregister() {
        Log.d(CommunicationConstants.TAG, "UNREGISTER USERID: " + regid);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    msg = "Sent unregistration";
                    //setUnregisterValues();
                    gcm.unregister();
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                removeRegistrationId(getApplicationContext());
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                ((TextView) findViewById(R.id.communication_display))
                        .setText(regid);
            }
        }.execute();
    }

    private void removeRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(CommunicationConstants.TAG, "Removig regId on app version "
                + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PROPERTY_REG_ID);
        editor.commit();
        regid = null;
    }

    @SuppressLint("NewApi")
    private void sendMessage(final String message, final String toRegId) {
        if (regid == null || regid.equals("")) {
            Toast.makeText(this, "You must register first", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (message.isEmpty()) {
            Toast.makeText(this, "Empty Message", Toast.LENGTH_LONG).show();
            return;
        }

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                List<String> regIds = new ArrayList<String>();
                //modified - replaced with to registration id
                String reg_device = toRegId;
                Log.d(TAG, "To registration id inside sendmessage() : "+toRegId);
                //String reg_device = regid;

                int nIcon = R.drawable.ic_stat_cloud;
                int nType = CommunicationConstants.SIMPLE_NOTIFICATION;
                Map<String, String> msgParams;
                msgParams = new HashMap<String, String>();
                msgParams.put("data.malertText", "Notification");
                msgParams.put("data.mtitleText", "Notification Title");
                msgParams.put("data.mcontentText", message);
                msgParams.put("data.nIcon", String.valueOf(nIcon));
                msgParams.put("data.nType", String.valueOf(nType));
                setSendMessageValues(message);
                GcmNotification gcmNotification = new GcmNotification();
                regIds.clear();
                regIds.add(reg_device);
                gcmNotification.sendNotification(msgParams, regIds,
                        TwoPlayerUserSelection.this);
                msg = "sending information...";

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }.execute(null, null, null);
    }

    private static void setSendMessageValues(String msg) {
        CommunicationConstants.alertText = "Message Notification";
        CommunicationConstants.titleText = "Sending Message";
        CommunicationConstants.contentText = msg;
    }

    public void addUserToFireBase(String userName, String regId)
    {

        Firebase usersRef = mRef.child(userName);
        usersRef.setValue(regId, new Firebase.CompletionListener() {
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

    public void removeUserFromFireBase(String userName, String regId)
    {

        Firebase usersRef = mRef.child(userName);
        usersRef.setValue(regId, new Firebase.CompletionListener() {
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

    public void populateUserList()
    {
        listItems.clear();
        adapter.notifyDataSetChanged();
        String user_regids;
        String ar[];
        ArrayList<String> key_values = new ArrayList<String>();
        ArrayList<String> keys = new ArrayList<String>();
        Log.d(TAG, "*****Firebase Data at an getDataForList...." + completeData);
        for (String retval: completeData.split(", ")){
            key_values.add(retval);
        }
        for (String temp : key_values)
        {
            ar = null;
            ar = temp.split("=");
            keys.add(ar[0]);
        }
        key_values.clear();
        for(String temp : keys)
        {
            if(temp.charAt(0) != '*' && temp.charAt(0) != '{' )
            {
                key_values.add(temp);
            }
        }
        for (String temp : key_values)
            listItems.add(temp);
        adapter.notifyDataSetChanged();


    }
	/*public void getAllRegIdsFromFireBase()
	{
		//remote.fetch
		//remote.getValue("mayank");
		String user_regids;
		String ar[];
		ArrayList<String> key_values = new ArrayList<String>();
		ArrayList<String> keys = new ArrayList<String>();
		remote.fetchAllValue();
		*//*while (remote.getValue("mayank") == null)
		{

		}*//*
		System.out.println(remote.getValue("mayank"));

		user_regids = remote.getDataset();
		for (String retval: user_regids.split(", ")){
			key_values.add(retval);
		}
		for (String temp : key_values)
		{
			ar = null;
			ar = temp.split("=");
			keys.add(ar[0]);
		}

		for (String temp : keys)
		Log.d(TAG, temp);


	}*/
}
