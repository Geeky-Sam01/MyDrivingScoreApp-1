package com.example.mydrivingscore;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;

import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class MyForegroundService extends Service
{
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        final String CHANNELID = "Foreground Service ID";
        NotificationChannel channel = new NotificationChannel(
                CHANNELID,
                CHANNELID,
                NotificationManager.IMPORTANCE_LOW
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNELID)
                .setContentText("Activity Recognition is running....")
                .setContentTitle("Obesity Point");

        startForeground(1001, notification.build());
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

}


