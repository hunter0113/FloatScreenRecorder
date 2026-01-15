package com.evan.floatscreenrecorder;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.evan.floatscreenrecorder.common.manager.CallbackManager;
import com.evan.floatscreenrecorder.common.util.DeviceUtils;
import com.evan.floatscreenrecorder.common.util.ToastUtil;
import com.evan.floatscreenrecorder.fab.activity.FABActivity;
import com.evan.floatscreenrecorder.fab.manager.FABManager;
import com.evan.floatscreenrecorder.fab.model.FloatingConfigurationModel;
import com.evan.floatscreenrecorder.record.Recording;
import com.evan.floatscreenrecorder.record.callback.NativeFloatingButtonCallback;
import com.evan.floatscreenrecorder.record.callback.RecordingStatusCallback;
import com.evan.floatscreenrecorder.record.callback.ShareVideoCallback;


/**
 * 浮動屏幕錄制 SDK
 * 
 * 提供靜態方法接口，但內部使用單例模式管理實例
 */
public class EvanSDK {


    /**
     * 私有構造函數，防止外部實例化
     */
    private EvanSDK() {
    }


    /**
     * SDK 初始化
     * @param context 應用上下文
     */
    public static void init(Context context) {
        if (context != null) {
            DeviceUtils.init(context);
            ToastUtil.init(context);
        }
    }
    
    /**
     * 設置循環錄制狀態
     * @param context 上下文
     * @param state 錄制狀態
     * @param callback 結果回調
     */
    public static void setLoopRecordingStatus(Context context, boolean state, RecordingStatusCallback callback) {
        Recording.setLoopRecordingStatusHandle(context, state, callback);
    }

    /**
     * 分享錄制影片
     * @param context 上下文
     * @param callback 結果回調
     */
    public static void shareRecordVideo(Context context, ShareVideoCallback callback) {
        Recording.shareRecordVideo(context, callback);
    }

    /**
     * 顯示浮動按鈕
     * @param activity 活動
     * @param floatingConfigurationModel 浮動按鈕配置模型
     * @param callback 回調
     */
    public static void showFloatingButtonWithConfiguration(Activity activity, 
                                                    FloatingConfigurationModel floatingConfigurationModel, 
                                                    NativeFloatingButtonCallback callback) {
        CallbackManager.setNativeFloatingButtonCallback(callback);
        FABManager.setConfigurationModel(floatingConfigurationModel);
        goToActivity(activity, FABActivity.class);
    }

    /**
     * 設置浮動按鈕顯示狀態
     * @param activity 活動
     * @param display 是否顯示
     */
    public static void setFloatingButtonDisplay(Activity activity, boolean display) {
        FABActivity.setFloatingButtonDisplay(activity, display);
    }

    /**
     * 跳轉到指定活動
     * @param activity 當前活動
     * @param cls 目標活動類
     */
    private static void goToActivity(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }

}
