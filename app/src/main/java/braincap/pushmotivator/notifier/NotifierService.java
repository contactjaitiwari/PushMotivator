package braincap.pushmotivator.notifier;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

import braincap.pushmotivator.MainActivity;
import braincap.pushmotivator.R;
import braincap.pushmotivator.beans.ResultQuote;

public class NotifierService extends Service {
    private static final String TAG = "JT";
    ArrayList<ResultQuote> list;
    int itr;
    int hours;

    public NotifierService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        list = intent.getExtras().getParcelableArrayList("list");
        Log.d(TAG, "onStartCommand: " + intent.getIntExtra("hours", 0));
        itr = intent.getIntExtra("location", 0);
        hours = intent.getIntExtra("hours", 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.app_icon)
                        .setAutoCancel(true)
                        .setDefaults(0)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentTitle("Push Motivator")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(list.get(itr).getPOST_DESCRIPTION().trim() + "\n\n-" + list.get(itr).getAUTH_TITLE().trim()));

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("Type", "QuoteFrag");
        resultIntent.putExtra("Quote", list.get(itr).getPOST_DESCRIPTION());
        resultIntent.putExtra("Author", list.get(itr).getAUTH_TITLE());
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
        Intent intentRepeatNow = new Intent(this, NotifierService.class);
        intentRepeatNow.putExtra("list", list);
        intentRepeatNow.putExtra("location", itr < list.size() - 1 ? ++itr : 0);
        intentRepeatNow.putExtra("hours", hours);
        intentRepeatNow.putExtra("NotificationMessage", "NotificationMessage");
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm.set(
                AlarmManager.RTC,
                System.currentTimeMillis() + (1000 * hours),
                PendingIntent.getService(this, 2, intentRepeatNow, PendingIntent.FLAG_CANCEL_CURRENT)
        );
        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
