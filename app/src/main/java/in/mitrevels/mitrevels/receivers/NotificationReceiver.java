package in.mitrevels.mitrevels.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;

import in.mitrevels.mitrevels.R;
import in.mitrevels.mitrevels.activities.MainActivity;

/**
 * Created by anurag on 22/2/17.
 */
public class NotificationReceiver extends BroadcastReceiver{

    private final String NOTIFICATION_TITLE="Upcoming Event";
    private String notificationText="";
    private final String LAUNCH_APPLICATION="Launch Revels'17";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent!=null){
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent appIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);

            notificationText = intent.getStringExtra("eventName")+" at "+intent.getStringExtra("startTime")+", "+intent.getStringExtra("eventVenue");

            Notification notify = new NotificationCompat.Builder(context)
                    .setContentTitle(NOTIFICATION_TITLE)
                    .setContentText(notificationText)
                    .setSmallIcon(R.drawable.ic_notif)
                    .setContentIntent(pendingIntent)
                    .setDefaults(NotificationCompat.DEFAULT_SOUND)
                    .setColor(ContextCompat.getColor(context, R.color.revels_grey))
                    .addAction(new android.support.v4.app.NotificationCompat.Action(0, LAUNCH_APPLICATION, pendingIntent))
                    .build();

            notificationManager.notify(Integer.parseInt(intent.getStringExtra("eventID")), notify);
        }

    }
}
