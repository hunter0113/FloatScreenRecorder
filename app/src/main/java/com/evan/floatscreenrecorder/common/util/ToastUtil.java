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
        Toast.makeText(m_ctx, msg, time).show();
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
