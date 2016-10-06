package edu.neu.madcourse.mayankranjandayal.two_player_word_game.two_player;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import edu.neu.madcourse.mayankranjandayal.R;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	static final String TAG = "GCM_Communication";

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

/*		String alertText = CommunicationConstants.alertText;
		String titleText = CommunicationConstants.titleText;
		String contentText = CommunicationConstants.contentText;
		sendNotification(alertText, titleText, contentText);*/

		Bundle extras = intent.getExtras();

		String alertText = extras.getString("malertText");
		String titleText = extras.getString("mtitleText");
		String contentText = extras.getString("mcontentText");

		Log.d("**outs intent service", contentText);
		/*String message;
		String titleText;
		String contentText;*/


		if(!extras.isEmpty()){

			/*message = String.valueOf(extras.get("alertText"));
			titleText = String.valueOf(extras.get("titleText"));
			contentText = String.valueOf(extras.get("contentText"));*/
			Log.d("**inside intent service", contentText);
			sendNotification(alertText, titleText, contentText);
		}
		else
		{
			Log.d("**inside intent service", "extras is empty");
		}

		Log.d(String.valueOf(extras.size()), extras.toString());

		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	public void sendNotification(String alertText, String titleText,
			String contentText) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent notificationIntent;
		notificationIntent = new Intent(this,
				TwoPlayerUserSelection.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		notificationIntent.putExtra("show_response", "show_response");
		PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(
						this, TwoPlayerStage1.class),
				PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.ic_stat_cloud)
				.setContentTitle(titleText)
				.setStyle(
						new NotificationCompat.BigTextStyle()
								.bigText(contentText))
				.setContentText(contentText).setTicker(alertText)
				.setAutoCancel(true);
		mBuilder.setContentIntent(intent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

}