package com.project.my.inz.app.help;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.project.my.inz.activity.MainActivity;
import com.project.my.inz.app.R;

public class AlarmReceiver extends BroadcastReceiver {
	private final String SOMEACTION = "com.project.my.inz.alarm.ACTION";

	@Override
	public void onReceive(Context context, Intent intent) {
		generateNotification(context,"Pora na przerwe!");
		String action = intent.getAction();
		if (SOMEACTION.equals(action)) {
			generateNotification(context,"Pora na przerwe!");
		}
	}
	
	@SuppressWarnings("deprecation")
	private void generateNotification(Context context, String message) {
		  System.out.println(message+"++++++++++2");
		  int icon = R.drawable.ic_launcher;
		  long when = System.currentTimeMillis();

		  NotificationManager notificationManager =
                  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		  Notification notification = new Notification(icon, message, when);

		  String title = "Przestań kodować";
		  String subTitle = "Zabierz ręcę od klawiatury i pobaw się telefonem!";

		  PendingIntent intent =
                  PendingIntent.getActivity(context, 0, new Intent(), 0);

		  notification.setLatestEventInfo(context, title, subTitle, intent);

		  //To play the default sound with your notification:
		  notification.defaults |= Notification.DEFAULT_SOUND;
		  notification.flags |= Notification.FLAG_AUTO_CANCEL;
		  notification.defaults |= Notification.DEFAULT_VIBRATE;

		  notificationManager.notify(0, notification);
    }

}