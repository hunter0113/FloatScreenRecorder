package com.evan.floatscreenrecorder.record.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.evan.floatscreenrecorder.common.constant.RecordingConstants;
import com.evan.floatscreenrecorder.record.callback.OutputVideoCallback;
import com.evan.floatscreenrecorder.record.callback.RecordingStatusCallback;
import com.evan.floatscreenrecorder.record.callback.ShareVideoCallback;
import com.evan.floatscreenrecorder.record.service.RecordingService;

import java.lang.ref.WeakReference;


/**
 * Created by Evan on 2021/11/18.
 * <p>
 * Description： Recording Manager
 */


public class RecordingManager {

    //=============================================================
    /**
     * Construct
     */
    //=============================================================
    private static RecordingService sm_ScreenRecordService;

    // Final File URI //
    private static Uri sm_ManagerFinalFile_URI;

    // Is currently Recording //
    private static boolean sm_isRecording = false;

    // Callback //
    private static WeakReference<RecordingStatusCallback> recordingStatusCallback;
    private static WeakReference<OutputVideoCallback> outputVideoCallback;
    private static WeakReference<ShareVideoCallback> shareVideoCallback;

    public static RecordingStatusCallback getRecordingStatusCallback() {
        return recordingStatusCallback != null ? recordingStatusCallback.get() : null;
    }

    public static void setRecordingStatusCallback(RecordingStatusCallback callback) {
        recordingStatusCallback = callback != null ? new WeakReference<>(callback) : null;
    }

    public static OutputVideoCallback getOutputVideoCallback() {
        return outputVideoCallback != null ? outputVideoCallback.get() : null;
    }

    public static void setOutputVideoCallback(OutputVideoCallback callback) {
        outputVideoCallback = callback != null ? new WeakReference<>(callback) : null;
    }

    public static ShareVideoCallback getShareVideoCallback() {
        return shareVideoCallback != null ? shareVideoCallback.get() : null;
    }

    public static void setShareVideoCallback(ShareVideoCallback callback) {
        shareVideoCallback = callback != null ? new WeakReference<>(callback) : null;
    }


    //=============================================================
    /**
     * Recording Status（Success）
     * @param isRecording 錄製狀態
     */
    //=============================================================
    public static void safelyCallRecordingStatusSuccess(boolean isRecording) {
        RecordingStatusCallback callback = getRecordingStatusCallback();
        if (callback != null) {
            new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(isRecording));
        }
    }

    //=============================================================
    /**
     * Recording Status（Error）
     * @param errorMsg 錯誤信息
     */
    //=============================================================
    public static void safelyCallRecordingStatusError(String errorMsg) {
        RecordingStatusCallback callback = getRecordingStatusCallback();
        if (callback != null) {
            new Handler(Looper.getMainLooper()).post(() -> callback.onError(errorMsg));
        }
    }

    //=============================================================
    /**
     * Output Video（Success）
     * @param filePath 文件路徑
     */
    //=============================================================
    public static void safelyCallOutputVideoSuccess(String filePath) {
        OutputVideoCallback callback = getOutputVideoCallback();
        if (callback != null) {
            new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(filePath));
        }
    }
    

    //=============================================================
    /**
     * Share Video（Finish）
     */
    //=============================================================
    public static void safelyCallShareVideoFinish() {
        ShareVideoCallback callback = getShareVideoCallback();
        if (callback != null) {
            new Handler(Looper.getMainLooper()).post(callback::onFinish);
        }
    }

    //=============================================================
    /**
     * Share Video（Error）
     * @param errorMsg 錯誤信息
     */
    //=============================================================
    public static void safelyCallShareVideoError(String errorMsg) {
        ShareVideoCallback callback = getShareVideoCallback();
        if (callback != null) {
            new Handler(Looper.getMainLooper()).post(() -> callback.onError(errorMsg));
        }
    }


    //=============================================================
    /**
     * Getter and Setter
     */
    //=============================================================
    public static void setScreenService(RecordingService screenService) {
        sm_ScreenRecordService = screenService;
    }
    public static RecordingService getScreenRecordService() {
        return sm_ScreenRecordService;
    }

    public static String getManagerFinalFilePath() {
        return sm_ManagerFinalFile_URI.toString();
    }

    public static Uri getManagerFinalFileUri() {
        return sm_ManagerFinalFile_URI;
    }

    public static void setManagerFinalFileUri(Uri Uri) {
        sm_ManagerFinalFile_URI = Uri;
    }

    public static boolean getIsRecording() {
        return sm_isRecording;
    }

    public static void setIsRecording(boolean status) {
        sm_isRecording = status;
    }


    //=============================================================
    /**
     * Clear All Callback
     */
    //=============================================================
    public static void clearAllCallback() {
        recordingStatusCallback = null;
        outputVideoCallback = null;
        shareVideoCallback = null;
    }


    //=============================================================
    /**
     * Set The Necessary Data
     */
    //=============================================================
    public static void setUpData(int resultCode, Intent resultData) {
        sm_ScreenRecordService.startNotification(); // Foreground Notification //
        sm_ScreenRecordService.setResultData(resultCode, resultData); // Set MediaProjection //
        sm_ScreenRecordService.startRecord();
    }


    //=============================================================
    /**
     * Start Recording
     */
    //=============================================================
    public static void startScreenRecord(Activity activity, int requestCode) {

        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) activity.
                getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        Intent intent = mediaProjectionManager.createScreenCaptureIntent();

        activity.startActivityForResult(intent, requestCode);

    }


    //=============================================================
    /**
     * Stop Recording
     */
    //=============================================================
    public static void stopRecord(RecordingStatusCallback callback) {
        sm_ScreenRecordService.stopRecord(callback);
    }


    //=============================================================
    /**
     * Stop and Output Recording
     */
    //=============================================================
    public static void outputRecord(Context context) {
        sm_ScreenRecordService.outputRecord(context);
    }


}
