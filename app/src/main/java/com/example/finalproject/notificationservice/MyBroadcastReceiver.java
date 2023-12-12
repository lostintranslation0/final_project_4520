package com.example.finalproject.notificationservice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.finalproject.R;

public class MyBroadcastReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "my_channel_id";
    private static final CharSequence CHANNEL_NAME = "Blog Notification Channel";
    private static final String CHANNEL_DESCRIPTION = "Notifications for new blogs from followers";

    private String displayName;

    public MyBroadcastReceiver(String displayName)
    {
        super();
        this.displayName = displayName;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        createNotificationChannel(context);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("New Blog Posted")
                        .setContentText(this.displayName + ", a user you are following posted a new blog!")
                        .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(getUniqueNotificationId(), builder.build());

        Toast.makeText(context, "Reminder: New Blog Posted!", Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            channel.setDescription(CHANNEL_DESCRIPTION);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private int getUniqueNotificationId() {
        // This method generates a unique ID for each notification
        return (int) System.currentTimeMillis();
    }
}
