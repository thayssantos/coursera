package com.coursera.lab.dailyselfie;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class AlarmBroadcastReceiver extends BroadcastReceiver {
	private static final int MY_NOTIFICATION_ID = 1;
	private final CharSequence tickerText = "Reminder: don't forget to take your dialy selfie!";
	private final CharSequence contentTitle = "REMINDER:";
	private final CharSequence contentText = "Let's take a selfie";
	private Intent mNotificationIntent;
	private PendingIntent mContentIntent;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		mNotificationIntent = new Intent(context, DailySelfieActivity.class);
		mContentIntent = PendingIntent.getActivity(context, 0, mNotificationIntent,
		Intent.FLAG_ACTIVITY_NEW_TASK);
		
		Notification.Builder notificationBuilder = new Notification.Builder(context)
		.setTicker(tickerText)
		.setSmallIcon(android.R.drawable.stat_sys_warning)
		.setAutoCancel(true).setContentTitle(contentTitle)
		.setContentText(contentText).setContentIntent(mContentIntent);
		NotificationManager mNotificationManager =
		(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder.build());
	}
}
