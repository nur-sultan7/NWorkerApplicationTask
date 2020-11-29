package com.nursultan.workerapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Задаем условия для запуска задачи
        Constraints internetConstraint = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        Constraints chargeLowConstraint = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();
        Constraints deviceIdleConstraint = new Constraints.Builder()
                .setRequiresDeviceIdle(true)
                .build();
        Constraints chargingConstraint = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        //Создаем список из OneTimeWorkRequest с условиями запуска и Worker
        List<OneTimeWorkRequest> workRequestList= new ArrayList<>();
        workRequestList.add(new OneTimeWorkRequest.Builder(MyWorker.class)
                .setConstraints(internetConstraint)
                .setInputData(new Data.Builder().putString("tag","internet").build())
                .build());
        workRequestList.add(new OneTimeWorkRequest.Builder(MyWorker.class)
                .setConstraints(chargingConstraint)
                .setInputData(new Data.Builder().putString("tag","charging").build())
                .build());
        workRequestList.add(new OneTimeWorkRequest.Builder(MyWorker.class)
                .setConstraints(chargeLowConstraint)
                .setInputData(new Data.Builder().putString("tag","chargeLow").build())
                .build());
        workRequestList.add(new OneTimeWorkRequest.Builder(MyWorker.class)
                .setConstraints(deviceIdleConstraint)
                .setInputData(new Data.Builder().putString("tag","deviceIdle").build())
                .build());
        //Запускаем список задач
        WorkManager.getInstance(this).enqueue(workRequestList);


    }
}
