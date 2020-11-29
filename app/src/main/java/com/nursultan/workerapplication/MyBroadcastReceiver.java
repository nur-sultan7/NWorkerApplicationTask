package com.nursultan.workerapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.impl.utils.ForceStopRunnable;

import java.net.NetworkInterface;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyBroadcastReceiver extends BroadcastReceiver {
    //получаем intent и выводим уведомление
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null)
            switch (action) {
                case "android.net.conn.CONNECTIVITY_CHANGE":
                    if (intent.getExtras()!=null) {
                        NetworkInfo networkInfo = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                        if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED)
                            showNotification(context, "Интернет", "Доступно подключение к сети");
                    }
                    break;
                case "android.intent.action.ACTION_POWER_CONNECTED":
                    showNotification(context,"","Мобильное устройство поставлено на зарядку");
                    break;
                case "android.intent.action.BATTERY_LOW":
                    showNotification(context,"","Мобильное устройство разряжено");
                    break;
                case "android.intent.action.SCREEN_OFF":
                    showNotification(context,"","Мобильное устройство ушло в спячку");
            }
    }


    private void showNotification(Context context, String nTitle, String nText) {

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        String id = "ch_1";
        String description = "143";


        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            channel = new NotificationChannel(id, description, importance);
            channel.enableLights(true);
            channel.enableVibration(true);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
            Notification notification = new Notification.Builder(context, id)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle(nTitle)
                    .setContentText(nText)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .build();
            if (manager != null) {
                manager.notify(1, notification);
            }
        }
        else
        {
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, id)
                            .setSmallIcon(R.drawable.notification)
                            .setContentTitle(nTitle)
                            .setContentText(nText)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            if (manager != null) {
                manager.notify(1, builder.build());
            }
        }



    }
}
