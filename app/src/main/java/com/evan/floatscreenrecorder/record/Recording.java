package com.evan.floatscreenrecorder.record;


import static com.evan.floatscreenrecorder.common.constant.Constants.PERMISSION_LIST;
import static com.evan.floatscreenrecorder.common.constant.Constants.PERMISSION_TYPE;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.os.Build;


import com.evan.floatscreenrecorder.common.constant.Constants;
import com.evan.floatscreenrecorder.common.util.FileUtil;
import com.evan.floatscreenrecorder.record.activity.PermissionActivity;
import com.evan.floatscreenrecorder.record.callback.RecordingStatusCallback;
import com.evan.floatscreenrecorder.record.callback.ShareVideoCallback;
import com.evan.floatscreenrecorder.record.manager.RecordingManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Evan on 2021/11/17.
 * <p>
 * Description：
 * Guide To The Corresponding Method and Set Callback
 */

public class Recording {


    //=============================================================
    /**
     * Set LoopRecording Status Handle
     */
    //=============================================================
    public static void setLoopRecordingStatusHandle(Context context, boolean state, RecordingStatusCallback callback) {
        if (state) {
            // 如果已經開啟錄影，直接回傳成功 //
            if (RecordingManager.getIsRecording()) {
                callback.onSuccess(true);
                return;
            }
            startLoopRecording(context, callback);

        } else {
            // 如果已經關閉錄影，直接回傳成功 //
            if (!RecordingManager.getIsRecording()) {
                callback.onSuccess(false);
                return;
            }
            stopLoopRecording(callback);
        }
    }


    //=============================================================
    /**
     * Start LoopRecording
     */
    //=============================================================
    public static void startLoopRecording(Context context, RecordingStatusCallback callback) {

        if (!checkIfItSupports(context, callback)) {
            return;
        }

        // clear Callback //
        RecordingManager.clearAllCallback();

        RecordingManager.setRecordingStatusCallback(callback);

        // Go TO PermissionActivity //
        String[] permissionStrings;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissionStrings = new String[]{"RECORD_AUDIO", "READ_EXTERNAL_STORAGE", "WRITE_EXTERNAL_STORAGE"};
        } else {
            permissionStrings = new String[]{"RECORD_AUDIO"};
        }

        goToActivity(context, PermissionActivity.class, permissionStrings, "LoopRecord");

    }


    //=============================================================
    /**
     * Stop LoopRecording
     */
    //=============================================================
    public static void stopLoopRecording(RecordingStatusCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    RecordingManager.stopRecord(callback);
                }
            });
        } finally {
            executor.shutdown();
        }
    }



    //=============================================================
    /**
     * Share Record Video
     */
    //=============================================================
    public static void shareRecordVideo(Context context, ShareVideoCallback callback) {
        if (!RecordingManager.getIsRecording()) {
            callback.onError(Constants.RECORD_NOT_TURNED_ON_MSG);
            return;
        }

        // clear Callback //
        RecordingManager.clearAllCallback();

        RecordingManager.setShareVideoCallback(callback);
        RecordingManager.outputRecord(context);
    }


    //=============================================================
    /**
     * check If It Supports
     */
    //=============================================================
    private static boolean checkIfItSupports(Context context, RecordingStatusCallback callback) {

        // Check SDK Version //
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            callback.onError(Constants.RECORD_VERSION_NOT_SUPPORT_MSG);
            return false;
        }

        // Check Have Recording Function //
        if (!haveRecordingFunction(context)) {
            callback.onError(Constants.RECORD_DEVICE_NOT_SUPPORT_MSG);
            return false;
        }

        // Check Storage Space Enough //
        if (!FileUtil.CheckStorageSpaceEnough(context)) {
            callback.onError(Constants.RECORD_NO_STORAGE_MSG);
            return false;
        }

        return true;
    }


    //=============================================================
    /**
     * Does The Phone Have a Recording Function
     */
    //=============================================================
    private static boolean haveRecordingFunction(Context context) {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) context.
                getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent intent = mediaProjectionManager.createScreenCaptureIntent();
        PackageManager packageManager = context.getPackageManager();

        return packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null;
    }


    //=============================================================
    /**
     * Go To Activity
     */
    //=============================================================
    private static void goToActivity(Context context, Class<?> cls, String[] strings, String type) {
        Intent intent = new Intent(context, cls);
        if (null != strings) {
            intent.putExtra(PERMISSION_LIST, strings);
        }

        if (null != strings) {
            intent.putExtra(PERMISSION_TYPE, type);
        }

        // android.util.AndroidRuntimeException: Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
