package com.nursultan.workerapplication;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends androidx.work.Worker {
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String tag = getInputData().getString("tag");
        MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        String action = null;
        if (tag != null) {
            switch (tag) {
                case "internet":
                    action = "android.net.conn.CONNECTIVITY_CHANGE";
                    break;
                case "charging":
                    action = "android.intent.action.ACTION_POWER_CONNECTED";
                    break;
                case "chargeLow":
                    action = "android.intent.action.BATTERY_LOW";
                    break;
                case "deviceIdle":
                    action = "android.intent.action.SCREEN_OFF";
                    break;
            }
        }
        intentFilter.addAction(action);
        //Регистрируем приёмник широковещательных сообщений
        getApplicationContext().registerReceiver(myBroadcastReceiver, intentFilter);
        return Result.success();
    }
}
