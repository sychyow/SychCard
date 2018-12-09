package org.supremus.sych.sychnews;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import org.supremus.sych.sychnews.interfaces.CompleteListener;
import org.supremus.sych.sychnews.tasks.UpdateTask;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class UpdateService extends Service implements CompleteListener {
    public static final String CHANNEL_ID = "NEWSUPD";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_download)
                .setContentTitle("News update")
                .setContentText("Sych News is updating its news feed")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
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
        new UpdateTask(this).execute();
        return START_STICKY;
    }


    @Override
    public void taskComplete() {
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_ok)
                .setContentTitle("News update")
                .setContentText("Sych News updated its news feed")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.cancel(11);
        nm.notify(12, notifBuilder.build());
        stopSelf();
    }
}
