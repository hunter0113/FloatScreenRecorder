package com.evan.floatscreenrecorder.fab.manager;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by Evan on 2022/9/22.
 * <p>
 * Description：
 * Get screen width and height (去掉狀態欄等高度)
 */

public class FABScreenManager {

    private static int mScreenWidth;
    private static int mScreenHeight;
    private static int mScreenDpi;

    public static void init(Activity activity){

        if (mScreenWidth != 0 || mScreenHeight != 0 || mScreenDpi != 0) {
            return;
        }

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        mScreenDpi = metrics.densityDpi;
    }

    public static int getScreenWidth(){
        return mScreenWidth;
    }

    public static int getScreenHeight(){
        return mScreenHeight;
    }

    public static int getScreenDpi(){
        return mScreenDpi;
    }

}
