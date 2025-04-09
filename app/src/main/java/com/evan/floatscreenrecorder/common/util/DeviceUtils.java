package com.evan.floatscreenrecorder.common.util;

import static com.evan.floatscreenrecorder.common.constant.Constants.ALL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Bruce on 2021/1/19.
 * <p>
 * Description：
 * Device Utils
 */

public class DeviceUtils {


    /**
     * Constants
     */
    @SuppressLint("StaticFieldLeak")
    private static Context m_ctx;
    private static final String command_cat = "/system/bin/cat";
    private static final String command_cpu = "/proc/cpuinfo";


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
     * Get Android System Version
     * <P>
     * @return Android System Version
     */
    //=============================================================
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }


    //=============================================================
    /**
     * Get Device System Version
     * <P>
     * @return Device System Version
     */
    //=============================================================
    public static String getSystemModel() {
        return Build.MODEL;
    }


    //=============================================================
    /**
     * Get Device System Brand
     * <P>
     * @return Device System Brand
     */
    //=============================================================
    public static String getDeviceBrand() {
        return Build.BRAND.toUpperCase();
    }


    //=============================================================
    /**
     * Get Package Name
     * <P>
     * @return Package Name
     */
    //=============================================================
    public static String getPackageName() {
        return m_ctx.getPackageName();
    }




    //=============================================================
    /**
     * Get ROM Space
     * <P>
     * @return AvailAble Size & Total Size
     */
    //=============================================================
    public static String getRomSpace(int spaceType) {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long availableBlocks = stat.getAvailableBlocks();

        String totalSize = Formatter.formatFileSize(m_ctx, totalBlocks * blockSize);
        String availableSize = Formatter.formatFileSize(m_ctx, availableBlocks * blockSize);

        return ALL == spaceType ? totalSize : availableSize;
    }



    //=============================================================
    /**
     * Get Display
     * <P>
     * @return Weight、Height
     */
    //=============================================================
    public static String getDisplay() {
        Display display = ((WindowManager) m_ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        double height = metrics.heightPixels / metrics.density;
        double weight = metrics.widthPixels / metrics.density;
        return weight + " x " + height;
    }
}
