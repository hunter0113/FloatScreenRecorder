package com.evan.floatscreenrecorder.common.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Bruce on 202 0/7/23.
 * <p>
 * Description：
 * Toast Util
 */

public class ToastUtil {

    private static Context m_ctx;

    //=============================================================

    /**
     * init
     */
    //=============================================================
    public static void init(Context ctx) {
        m_ctx = ctx;
    }

    //=============================================================

    /**
     * show
     */
    //=============================================================
    public static void show(String msg) {
        show(Toast.LENGTH_SHORT, msg);
    }

    public static void show(int time, String msg) {
        if (m_ctx != null) {
            Toast.makeText(m_ctx, msg, time).show();
        }
    }
    
    /**
     * 顯示 Toast（使用指定 Context）
     * @param context 上下文
     * @param msg 訊息
     */
    public static void showToast(Context context, String msg) {
        if (context != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 顯示 Toast（使用指定 Context 和時長）
     * @param context 上下文
     * @param msg 訊息
     * @param duration 顯示時長
     */
    public static void showToast(Context context, String msg, int duration) {
        if (context != null) {
            Toast.makeText(context, msg, duration).show();
        }
    }

    //=============================================================

    /**
     * show if
     */
    //=============================================================
    public static void showIf(boolean b, String msg) {
        if (b) show(msg);
    }

    public static void showLongIf(boolean b, String msg) {
        if (b) Toast.makeText(m_ctx, msg, Toast.LENGTH_LONG).show();
    }

}
