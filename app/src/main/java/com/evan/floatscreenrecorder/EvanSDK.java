package com.evan.floatscreenrecorder;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.evan.floatscreenrecorder.common.manager.CallbackManager;
import com.evan.floatscreenrecorder.common.util.DeviceUtils;
import com.evan.floatscreenrecorder.fab.activity.FABActivity;
import com.evan.floatscreenrecorder.fab.manager.FABManager;
import com.evan.floatscreenrecorder.fab.model.FloatingConfigurationModel;
import com.evan.floatscreenrecorder.record.Recording;
import com.evan.floatscreenrecorder.record.callback.NativeFloatingButtonCallback;
import com.evan.floatscreenrecorder.record.callback.RecordingStatusCallback;
import com.evan.floatscreenrecorder.record.callback.ShareVideoCallback;


public class EvanSDK {

    @SuppressLint("StaticFieldLeak")
    private static volatile EvanSDK m_instance;


    private EvanSDK() {
        super();
    }


    private static EvanSDK getInstance() {
        if (m_instance == null) {
            synchronized (EvanSDK.class) {
                if (m_instance == null) {
                    m_instance = new EvanSDK();
                }
            }
        }

        return m_instance;
    }



    //=============================================================
    /**
     * SDK Init
     * @param context Context
     */
    //=============================================================
    public static void init(Context context) {
        getInstance();
        DeviceUtils.init(context);
    }

    //=============================================================
    /**
     * SDK Record Relevant
     */
    //=============================================================
    public static void setLoopRecordingStatus(Context context, boolean state, RecordingStatusCallback callback) {
        getInstance();
        Recording.setLoopRecordingStatusHandle(context, state, callback);
    }


    public static void shareRecordVideo(Context context, ShareVideoCallback callback) {
        getInstance();
        Recording.shareRecordVideo(context, callback);
    }


    //=============================================================
    /**
     * SDK FAB Relevant
     */
    //=============================================================
    public static void showFloatingButtonWithConfiguration(Activity activity, FloatingConfigurationModel floatingConfigurationModel, NativeFloatingButtonCallback callback) {
        getInstance();
        CallbackManager.setNativeFloatingButtonCallback(callback);
        FABManager.setConfigurationModel(floatingConfigurationModel);
        goToActivity(activity, FABActivity.class);
    }


    public static void setFloatingButtonDisplay(Activity activity, boolean display) {
        getInstance();
        FABActivity.setFloatingButtonDisplay(activity, display);
    }


    private static void goToActivity(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }
}
