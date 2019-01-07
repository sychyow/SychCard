package org.supremus.sych.sychnews;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import org.supremus.sych.sychnews.interfaces.CompleteListener;
import org.supremus.sych.sychnews.tasks.UpdateTask;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class UpdateService extends Service implements CompleteListener {
    public static final String CHANNEL_ID = "NEWSUPD";
    public static final String ACTION_STOP = "sychnews.network.update.STOP";
    private AsyncTask<Object, Void, Void> currentUpdateTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_STOP);
        registerReceiver(receiver, intentFilter);

        createNotificationChannel();
        Intent cancelIntent = new Intent();
        cancelIntent.setAction(ACTION_STOP);
        PendingIntent pIntent = PendingIntent.getBroadcast(this,0, cancelIntent,0);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_download)
                .setContentTitle("News update")
                .setContentText("Sych News is updating its news feed")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(0, "CANCEL", pIntent);
        startForeground(11, notifBuilder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentUpdateTask = new UpdateTask(this);
        currentUpdateTask.execute();
        return START_STICKY;
    }


    @Override
    public void taskComplete(boolean wasSkipped) {
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_ok)
                .setContentTitle("News update")
                .setContentText("Sych News has updated its news feed")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.cancel(11);
        if (!wasSkipped)
            nm.notify(12, notifBuilder.build());
        unregisterReceiver(receiver);
        stopSelf();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println("Cancel intent: "+action);
            if(action.equals(ACTION_STOP)){
               currentUpdateTask.cancel(true);
            }

        }
    };
}
