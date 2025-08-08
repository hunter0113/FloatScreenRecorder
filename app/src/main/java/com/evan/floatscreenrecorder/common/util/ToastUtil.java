package com.evan.floatscreenrecorder.common.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    private static Context sApplicationContext;

    /**
     * 初始化 ToastUtil，應該在 Application 中調用
     * @param context 應用程式上下文，建議傳入 Application Context
     */
    public static void init(Context context) {
        if (context != null) {
            sApplicationContext = context.getApplicationContext();
        }
    }

    /**
     * 顯示短時間 Toast
     * @param msg 訊息內容
     */
    public static void show(String msg) {
        show(Toast.LENGTH_SHORT, msg);
    }

    /**
     * 顯示指定時長的 Toast
     * @param duration 顯示時長
     * @param msg 訊息內容
     */
    public static void show(int duration, String msg) {
        if (sApplicationContext != null && msg != null) {
            Toast.makeText(sApplicationContext, msg, duration).show();
        }
    }
    
    /**
     * 顯示 Toast（使用指定 Context）
     * 推薦使用此方法，避免依賴全域 Context
     * @param context 上下文
     * @param msg 訊息
     */
    public static void showToast(Context context, String msg) {
        if (context != null && msg != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 顯示 Toast（使用指定 Context 和時長）
     * 推薦使用此方法，避免依賴全域 Context
     * @param context 上下文
     * @param msg 訊息
     * @param duration 顯示時長
     */
    public static void showToast(Context context, String msg, int duration) {
        if (context != null && msg != null) {
            Toast.makeText(context, msg, duration).show();
        }
    }

    //=============================================================

    /**
     * 條件顯示短時間 Toast
     * @param condition 顯示條件
     * @param msg 訊息內容
     */
    //=============================================================
    public static void showIf(boolean condition, String msg) {
        if (condition) {
            show(msg);
        }
    }

    /**
     * 條件顯示長時間 Toast
     * @param condition 顯示條件
     * @param msg 訊息內容
     */
    public static void showLongIf(boolean condition, String msg) {
        if (condition) {
            show(Toast.LENGTH_LONG, msg);
        }
    }

    /**
     * 條件顯示 Toast（使用指定 Context）
     * @param context 上下文
     * @param condition 顯示條件
     * @param msg 訊息內容
     */
    public static void showToastIf(Context context, boolean condition, String msg) {
        if (condition) {
            showToast(context, msg);
        }
    }
}
