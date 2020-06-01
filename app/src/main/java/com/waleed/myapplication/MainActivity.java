package com.waleed.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static final String MESSAGE_STATUS = "message_status";
    TextView tvStatus;
    Button btnSend, btnCancel;

    WorkManager mWorkManager;
    OneTimeWorkRequest mRequest;
    PeriodicWorkRequest workRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStatus = findViewById(R.id.textView);
        btnSend = findViewById(R.id.button);
        btnCancel = findViewById(R.id.buttonCancel);
        btnCancel.setVisibility(View.GONE);

        mWorkManager = WorkManager.getInstance();


        // one time work manager

//        mRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).build();
//        btnSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mWorkManager.enqueue(mRequest);
//            }
//        });
//
//        mWorkManager.getWorkInfoByIdLiveData(mRequest.getId()).observe(this, new Observer<WorkInfo>() {
//            @Override
//            public void onChanged(@Nullable WorkInfo workInfo) {
//                if (workInfo != null) {
//                    WorkInfo.State state = workInfo.getState();
//                    tvStatus.append(state.toString() + "\n");
//                }
//            }
//        });


        // multi time work manager
        // اقل وقت ليه عشان يشتغل هو 20 دقيقة
        btnCancel.setVisibility(View.VISIBLE);

//        Constraints constraints = new Constraints.Builder()
//                .setRequiresDeviceIdle(true)
//                .setRequiresCharging(true)
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .setRequiresBatteryNotLow(true)
//                .setRequiresStorageNotLow(false)
//                .build();

        workRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class,
                20, TimeUnit.MINUTES)
                // .setConstraints(constraints)
                .build();


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mWorkManager.enqueueUniquePeriodicWork("Send Data", ExistingPeriodicWorkPolicy.REPLACE, workRequest);
                mWorkManager.enqueue(workRequest);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance().cancelWorkById(workRequest.getId());

            }
        });
        mWorkManager.getWorkInfoByIdLiveData(workRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    tvStatus.append(state.toString() + "\n");
                }
            }
        });


    }

}
