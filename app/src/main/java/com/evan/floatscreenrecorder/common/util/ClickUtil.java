package com.evan.floatscreenrecorder.common.util;

/**
 * Created by Evan on 2021/12/4.
 * <p>
 * Description：
 */

public class ClickUtil {

    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;


    //=============================================================
    /**
     * is Double click
     */
    //=============================================================
    public static boolean isDoubleClick() {
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) <= MIN_CLICK_DELAY_TIME) {
            return true;
        }
        lastClickTime = curClickTime;
        return false;
    }
}
