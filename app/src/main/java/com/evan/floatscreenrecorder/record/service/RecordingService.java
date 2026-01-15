package com.evan.floatscreenrecorder.record.service;


import static com.evan.floatscreenrecorder.common.constant.Constants.NOTIFICATION_RECODE_CHANNEL_ID;
import static com.evan.floatscreenrecorder.common.constant.Constants.RECORD_STOP_ACTION;
import static com.evan.floatscreenrecorder.common.constant.RecordingConstants.*;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.evan.floatscreenrecorder.R;
import com.evan.floatscreenrecorder.common.constant.Constants;
import com.evan.floatscreenrecorder.common.constant.RecordErrorType;
import com.evan.floatscreenrecorder.common.constant.RecordingConstants;
import com.evan.floatscreenrecorder.common.util.FileUtil;
import com.evan.floatscreenrecorder.record.activity.ShareVideoActivity;
import com.evan.floatscreenrecorder.record.callback.RecordingStatusCallback;
import com.evan.floatscreenrecorder.record.manager.LiveDataManager;
import com.evan.floatscreenrecorder.record.manager.RecordingManager;
import com.evan.floatscreenrecorder.record.manager.ScreenManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Evan on 2021/11/17.
 * <p>
 * Description：
 * Control Recording Service
 */

public class RecordingService extends Service implements Handler.Callback {

    private final String TAG = "RecordingService";

    /**
     * Media Construct
     */
    private MediaProjectionManager m_ProjectionManager;
    private MediaProjection m_MediaProjection;
    private MediaRecorder m_MediaRecorder;

    private final int m_iRecordWidth = ScreenManager.getScreenWidth();
    private final int m_iRecordHeight = ScreenManager.getScreenHeight();
    private final int m_iScreenDpi = ScreenManager.getScreenDpi();

    private int m_ResultCode;
    private Intent m_ResultData;


    /**
     * Video Path
     */
    private static String m_RecordFilePath_A;
    private static String m_RecordFilePath_Final;
    private static final String DOT_MP4 = VIDEO_EXTENSION;



    private Handler m_Handler;

    /**
     * 錄製總時長度
     */
    private static int m_RecordAllSeconds = 0;


    /**
     * 單一檔案已經錄製多久。
     */
    private static int m_SingleRecordSeconds = 0;

    private static final int MSG_TYPE_COUNT_DOWN = 110;

    /**
     * Notification
     */
    private Notification notification;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        setNotification();
        RecordingManager.setScreenService(this);

        LiveDataManager.recordingServiceLiveData.setValue("ok");

        RecordingManager.setIsRecording(false);
        m_MediaRecorder = new MediaRecorder();
        m_Handler = new Handler(Looper.getMainLooper(), this);

    }


    //=============================================================
    /**
     * Set Notification
     */
    //=============================================================
    private void setNotification() {

        // Android 12 強迫加上 FLAG_MUTABLE or FLAG_IMMUTABLE
        PendingIntent pendingIntent;
        Intent intent = new Intent(this, RecordingService.class);
        intent.setAction(RECORD_STOP_ACTION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getService(this, RecordingConstants.PENDING_INTENT_REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getService(this, RecordingConstants.PENDING_INTENT_REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

        notification = new NotificationCompat.Builder(this, NOTIFICATION_RECODE_CHANNEL_ID)
                .setSmallIcon(R.mipmap.recode_notification_icon)
                .setContentText("正在錄影中")
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "停止", pendingIntent)
                .build();

        notification.flags |= Notification.FLAG_NO_CLEAR;
    }


    //=============================================================
    /**
     * Start Notification
     */
    //=============================================================
    public void startNotification() {
        startForeground(Constants.NOTIFICATION_FOREGROUND_ID, notification);
    }


    //=============================================================
    /**
     * Getter
     */
    //=============================================================
    public static String getFinalFilePath() {
        return m_RecordFilePath_Final;
    }


    //=============================================================
    /**
     * Set Result Data
     */
    //=============================================================
    public void setResultData(int resultCode, Intent resultData) {
        m_ResultCode = resultCode;
        m_ResultData = resultData;

        m_ProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        m_MediaProjection = m_ProjectionManager.getMediaProjection(m_ResultCode, m_ResultData);
        m_MediaProjection.registerCallback(new MediaProjection.Callback() {
            @Override
            public void onStop() {
                super.onStop();
            }
        }, null);
    }


    //=============================================================
    /**
     * Create Virtual Display
     */
    //=============================================================
    private void createVirtualDisplay() {
        m_MediaProjection.createVirtualDisplay("MainScreen",
                m_iRecordWidth, m_iRecordHeight, m_iScreenDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, m_MediaRecorder.getSurface(), null, null);
    }


    //=============================================================
    /**
     * SetUp Media Recorder
     */
    //=============================================================
    private void setUpMediaRecorder() {
        String m_saveDirectory = FileUtil.getSaveDirectory(this);

        SimpleDateFormat df = new SimpleDateFormat(RecordingConstants.DATE_FORMAT, Locale.getDefault());

        Date dateA = new Date();
        String timeA = df.format(dateA);
        String timeB = df.format(addSeconds(dateA, RecordingConstants.FILE_TIME_OFFSET_B));
        String timeC = df.format(addSeconds(dateA, RecordingConstants.FILE_TIME_OFFSET_C));
        String timeFinal = df.format(addSeconds(dateA, RecordingConstants.FILE_TIME_OFFSET_FINAL));


        m_RecordFilePath_A = m_saveDirectory + File.separator + timeA + DOT_MP4;
        m_RecordFilePath_Final = m_saveDirectory + File.separator + timeFinal + DOT_MP4;


        // 參數變為預設 //
        m_RecordAllSeconds = RecordingConstants.INITIAL_RECORD_SECONDS;
        m_SingleRecordSeconds = RecordingConstants.INITIAL_RECORD_SECONDS;


        // DEFAULT,MIC 聲音內外皆有 //
        // VOICE_RECOGNITION 手機音無 環境音小 //
        // VOICE_COMMUNICATION 手機音幾乎沒有 環境音小 //
        m_MediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        m_MediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        m_MediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        m_MediaRecorder.setOutputFile(m_RecordFilePath_A);
        m_MediaRecorder.setVideoSize(m_iRecordWidth, m_iRecordHeight);
        m_MediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        m_MediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        m_MediaRecorder.setAudioEncodingBitRate(RecordingConstants.AUDIO_BITRATE);
        m_MediaRecorder.setAudioSamplingRate(RecordingConstants.AUDIO_SAMPLE_RATE);


        // setVideoEncodingBitRate //
        // https://support.google.com/youtube/answer/1722171?hl=en#zippy=%2Cbitrate //
        // BitRate 8百萬為1080p, 5百萬為720p //
        m_MediaRecorder.setVideoEncodingBitRate(RecordingConstants.VIDEO_BITRATE_1080P);
        m_MediaRecorder.setCaptureRate(RecordingConstants.VIDEO_CAPTURE_RATE);
        m_MediaRecorder.setVideoFrameRate(RecordingConstants.VIDEO_FRAME_RATE);

        try {
            m_MediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "Failed to prepare MediaRecorder", e);
            RecordingManager.safelyCallRecordingStatusError(
                    "Failed to prepare recorder: " + e.getMessage()
            );
            return; // 或 throw RuntimeException
        }
    }


    //=============================================================
    /**
     * Start Record
     */
    //=============================================================
    public void startRecord() {

        setUpMediaRecorder();
        createVirtualDisplay();
        m_MediaRecorder.start();

        m_Handler.sendEmptyMessageDelayed(MSG_TYPE_COUNT_DOWN, RecordingConstants.HANDLER_SEND_IMMEDIATELY);

        RecordingManager.setIsRecording(true);

        RecordingManager.safelyCallRecordingStatusSuccess(true);
    }


    //=============================================================
    /**
     * Output Record Video
     */
    //=============================================================
    public void outputRecord(final Context context) {

        // Remove Notification //
        stopForeground(true);

        // The video needs to have a basic length //
        if (m_RecordAllSeconds <= RecordingConstants.MIN_RECORD_DURATION_SECONDS) {
            outputRecordErrorHandle();
            return;
        }

        // output Record Handle //
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    outputRecordHandle(context);
                }
            });
        } finally {
            executor.shutdown();
        }
    }


    //=============================================================
    /**
     * Output Record Error
     */
    //=============================================================
    private void outputRecordErrorHandle() {
        deleteAFile();
        stopAndRelease();
        errorCallbackHandle(RecordErrorType.DURATION_SHORT.getType());
    }


    //=============================================================
    /**
     * Output Record Handle
     */
    //=============================================================
    private void outputRecordHandle(Context context) {
        stopAndRelease();

        // 換檔案後，新檔案儲存不到兩秒，只保留另一個檔案，不做合併 //
        if(m_SingleRecordSeconds < RecordingConstants.MIN_SINGLE_FILE_DURATION_SECONDS){
            return;
        }

        setFinalFilePathAndSave(m_RecordFilePath_A, context);
    }


    //=============================================================
    /**
     * Stop Record Video
     */
    //=============================================================
    public void stopRecord(RecordingStatusCallback callback) {

        // Remove Notification //
        stopForeground(true);

        // 狀態改為false //
        RecordingManager.setIsRecording(false);

        stopAndRelease();

        deleteAFile();

        if(null != callback){
            callback.onSuccess(false);
        }

        stopSelf();
    }


    //=============================================================
    /**
     * Handle Message
     */
    //=============================================================
    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == MSG_TYPE_COUNT_DOWN) {
            if (!RecordingManager.getIsRecording()) {
                return true;
            }
            m_RecordAllSeconds++;
            m_SingleRecordSeconds++;

            m_Handler.sendEmptyMessageDelayed(MSG_TYPE_COUNT_DOWN, RecordingConstants.COUNTDOWN_INTERVAL_MS);
        }
        return true;
    }


    //=============================================================
    /**
     * Set Final File Path and Save
     */
    //=============================================================
    private static void setFinalFilePathAndSave(String FinalFilePath, Context context) {
        // FinalFilePath 可能為 A C Final 需要更改檔案名稱 全部換成final //
        m_RecordFilePath_Final = FinalFilePath;

        // Recording 狀態改為false //
        RecordingManager.setIsRecording(false);

        // 經過剪輯與合併的檔案新增置系統圖庫 //
        boolean saveFile = FileUtil.fileScanVideo(context, m_RecordFilePath_Final, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight());

        deleteFileHandle();

        if (!saveFile) {
            errorCallbackHandle(RecordErrorType.PUBLIC_STORAGE_ERROR.getType());
            return;
        }

        successHandle(context);
    }


    //=============================================================
    /**
     * Success Save File Callback and Handle
     */
    //=============================================================
    private static void successHandle(Context context) {

        // OutputVideo Callback //
        if (null != RecordingManager.getOutputVideoCallback()) {
            RecordingManager.safelyCallOutputVideoSuccess(RecordingManager.getManagerFinalFilePath());
            return;
        }

        // Share //
        if (null != RecordingManager.getShareVideoCallback()) {
            goToActivity(context);
            return;
        }

        RecordingManager.getScreenRecordService().stopSelf();
    }


    //=============================================================
    /**
     * Delete File Handle
     */
    //=============================================================
    private static void deleteFileHandle() {
        deleteAllFile();
    }


    //=============================================================
    /**
     * Delete All File
     */
    //=============================================================
    private static void deleteAllFile() {
        File file_A = new File(m_RecordFilePath_A);
        FileUtil.deleteSDFile(file_A.getAbsolutePath());

        File file_Final = new File(m_RecordFilePath_Final);
        FileUtil.deleteSDFile(file_Final.getAbsolutePath());
    }


    //=============================================================
    /**
     * Delete A File
     */
    //=============================================================
    public static void deleteAFile() {
        File file_A = new File(m_RecordFilePath_A);
        FileUtil.deleteSDFile(file_A.getAbsolutePath());
    }


    //=============================================================
    /**
     * Delete Final File
     */
    //=============================================================
    public static void deleteFinalFile() {
        File file_Final = new File(m_RecordFilePath_Final);
        FileUtil.deleteSDFile(file_Final.getAbsolutePath());
    }


    //=============================================================
    /**
     * Stop And Release
     */
    //=============================================================
    public void stopAndRelease() {
        try {
            m_MediaRecorder.stop();
            m_MediaRecorder.reset();
            m_MediaRecorder.release();

        } catch (IllegalStateException e) {
            m_MediaRecorder.release();
        }

        m_MediaProjection.stop();
        m_MediaProjection = null;
        m_Handler.removeMessages(MSG_TYPE_COUNT_DOWN);
    }


    //=============================================================
    /**
     * Stop And Release
     */
    //=============================================================
    public static void errorCallbackHandle(String ErrorCode) {
        // Recording 狀態改為false //
        RecordingManager.setIsRecording(false);

        if (null != RecordingManager.getOutputVideoCallback()) {
            RecordingManager.getOutputVideoCallback().onError(Constants.RECORD_ERROR_MSG + "(" + ErrorCode + ").");
            deleteAllFile();
            return;
        }

        if (null != RecordingManager.getShareVideoCallback()) {
            RecordingManager.safelyCallShareVideoError(Constants.RECORD_ERROR_MSG + "(" + ErrorCode + ").");
            deleteAllFile();
            return;
        }

        RecordingManager.getScreenRecordService().stopSelf();
    }


    //=============================================================
    /**
     * Date add Seconds For FileName
     */
    //=============================================================
    private static Date addSeconds(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    public void stopRecord() {
        // Remove Notification //
        stopForeground(true);

        // 狀態改為false
        RecordingManager.setIsRecording(false);

        stopAndRelease();

        deleteAFile();

        RecordingManager.safelyCallRecordingStatusError(Constants.RECORD_NOTIFICATION_CLOSE_MSG);

        stopSelf();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (null != intent) {
            if (RECORD_STOP_ACTION.equals(intent.getAction())) {
                stopRecord();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        RecordingManager.setIsRecording(false);
        RecordingManager.setScreenService(null);
        stopForeground(true);

        super.onDestroy();
    }


    //=============================================================
    /**
     * Go To Activity
     */
    //=============================================================
    private static void goToActivity(Context context) {
        Intent intent = new Intent(context, ShareVideoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
