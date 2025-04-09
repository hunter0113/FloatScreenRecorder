package com.evan.floatscreenrecorder.record.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;

import com.evan.floatscreenrecorder.record.callback.OutputVideoCallback;
import com.evan.floatscreenrecorder.record.callback.RecordingStatusCallback;
import com.evan.floatscreenrecorder.record.callback.ShareVideoCallback;
import com.evan.floatscreenrecorder.record.service.RecordingService;


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

    // Total Recording Size //
    private static int sm_nRecord_Capacity = 30;

    // Final File URI //
    private static Uri sm_ManagerFinalFile_URI;

    // Is currently Recording //
    private static boolean sm_isRecording = false;

    // Callback //
    private static RecordingStatusCallback recordingStatusCallback;
    private static OutputVideoCallback outputVideoCallback;
    private static ShareVideoCallback shareVideoCallback;

    public static RecordingStatusCallback getRecordingStatusCallback() {
        return recordingStatusCallback;
    }

    public static void setRecordingStatusCallback(RecordingStatusCallback recordingStatusCallback) {
        RecordingManager.recordingStatusCallback = recordingStatusCallback;
    }

    public static OutputVideoCallback getOutputVideoCallback() {
        return outputVideoCallback;
    }

    public static void setOutputVideoCallback(OutputVideoCallback outputVideoCallback) {
        RecordingManager.outputVideoCallback = outputVideoCallback;
    }

    public static ShareVideoCallback getShareVideoCallback() {
        return shareVideoCallback;
    }

    public static void setShareVideoCallback(ShareVideoCallback shareVideoCallback) {
        RecordingManager.shareVideoCallback = shareVideoCallback;
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

    public static void setRecordCapacity(int capacity) {
        sm_nRecord_Capacity = capacity;
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

    public static int getRecord_Capacity() {
        return sm_nRecord_Capacity;
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


    //=============================================================
    /**
     * File location After Recording. For Upload
     */
    //=============================================================
    public static String getScreenRecordFilePath() {
        return RecordingService.getFinalFilePath();
    }

}
