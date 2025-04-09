package com.evan.floatscreenrecorder.record.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.evan.floatscreenrecorder.common.constant.Constants;
import com.evan.floatscreenrecorder.record.manager.LiveDataManager;
import com.evan.floatscreenrecorder.record.manager.RecordingManager;
import com.evan.floatscreenrecorder.record.manager.ScreenManager;
import com.evan.floatscreenrecorder.record.service.RecordingService;

/**
 * Created by Evan on 2021/11/18.
 * <p>
 * Description：Start Service, Set BroadcastReceiver to Get Service Messages and Obtain a One-time Recording Permission
 */

public class RecordingActivity extends AppCompatActivity {


    private final static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // Init //
        ScreenManager.init(this);


        // Start Service //
        startScreenRecordService();


        // LiveData After The Service OnCreate//
        LiveDataManager.recordingServiceLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String data) {
                // 開始錄影
                if (null == data) {
                    return;
                }

                if (!data.equals("ok")) {
                    return;
                }

                RecordingManager.setRecordCapacity(30);
                RecordingManager.startScreenRecord(RecordingActivity.this, REQUEST_CODE);
                LiveDataManager.recordingServiceLiveData.removeObserver(this);
            }
        });

    }


    //=============================================================
    /**
     * Start Service
     */
    //=============================================================
    private void startScreenRecordService() {
        Intent serviceIntent = new Intent(this, RecordingService.class);
        stopService(serviceIntent);
        startService(serviceIntent);
    }


    //=============================================================
    /**
     * The ActivityResult Received After Accepting The Screen Recording
     */
    //=============================================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            RecordingManager.setUpData(resultCode, data);

        } else {
            RecordingManager.getRecordingStatusCallback().onError(Constants.RECORD_USER_REFUSE_MSG);
        }

        finish();
        overridePendingTransition(0, 0);
    }
}
