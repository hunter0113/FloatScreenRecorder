package com.evan.floatscreenrecorder.record.manager;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowMetrics;

/**
 * Created by Evan on 2021/11/9.
 * <p>
 * Description：
 * Get screen width and height
 */

public class ScreenManager {

    private static int mScreenWidth;
    private static int mScreenHeight;
    private static int mScreenDpi;

    public static void init(Activity activity) {

        if (mScreenWidth != 0 || mScreenHeight != 0 || mScreenDpi != 0) {
            return;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            WindowMetrics metrics = activity.getWindowManager().getCurrentWindowMetrics();
            mScreenWidth = metrics.getBounds().width();
            mScreenHeight = metrics.getBounds().height();
            mScreenDpi = activity.getResources().getConfiguration().densityDpi;
        } else {
            Display display = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getRealMetrics(metrics);
            mScreenWidth = metrics.widthPixels;
            mScreenHeight = metrics.heightPixels;
            mScreenDpi = metrics.densityDpi;
        }
    }

    public static int getScreenWidth() {
        return mScreenWidth;
    }

    public static int getScreenHeight() {
        return mScreenHeight;
    }

    public static int getScreenDpi() {
        return mScreenDpi;
    }

}
