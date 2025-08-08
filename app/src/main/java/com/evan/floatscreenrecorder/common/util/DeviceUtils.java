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

public class DeviceUtils {

    /**
     * Constants
     */
    private static Context sApplicationContext;
    /**
     * 初始化 DeviceUtils，應該在 Application 中調用
     * @param context 應用程式上下文，建議傳入 Application Context
     */
    public static void init(Context context) {
        if (context != null) {
            sApplicationContext = context.getApplicationContext();
        }
    }

    /**
     * Get Android System Version
     * @return Android System Version
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * Get Device System Version
     * @return Device System Version
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * Get Device System Brand
     * @return Device System Brand
     */
    public static String getDeviceBrand() {
        return Build.BRAND.toUpperCase();
    }

    /**
     * Get Package Name
     * @return Package Name
     */
    public static String getPackageName() {
        if (sApplicationContext != null) {
            return sApplicationContext.getPackageName();
        }
        return "";
    }

    /**
     * Get Package Name (使用指定 Context)
     * 推薦使用此方法，避免依賴全域 Context
     * @param context 上下文
     * @return Package Name
     */
    public static String getPackageName(Context context) {
        if (context != null) {
            return context.getPackageName();
        }
        return "";
    }

    /**
     * Get ROM Space
     * @param spaceType 空間類型
     * @return AvailAble Size & Total Size
     */
    public static String getRomSpace(int spaceType) {
        if (sApplicationContext == null) {
            return "";
        }
        
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long availableBlocks = stat.getAvailableBlocks();

        String totalSize = Formatter.formatFileSize(sApplicationContext, totalBlocks * blockSize);
        String availableSize = Formatter.formatFileSize(sApplicationContext, availableBlocks * blockSize);

        return ALL == spaceType ? totalSize : availableSize;
    }

    /**
     * Get ROM Space (使用指定 Context)
     * 推薦使用此方法，避免依賴全域 Context
     * @param context 上下文
     * @param spaceType 空間類型
     * @return AvailAble Size & Total Size
     */
    public static String getRomSpace(Context context, int spaceType) {
        if (context == null) {
            return "";
        }
        
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long availableBlocks = stat.getAvailableBlocks();

        String totalSize = Formatter.formatFileSize(context, totalBlocks * blockSize);
        String availableSize = Formatter.formatFileSize(context, availableBlocks * blockSize);

        return ALL == spaceType ? totalSize : availableSize;
    }

    /**
     * Get Display
     * @return Weight、Height
     */
    public static String getDisplay() {
        if (sApplicationContext == null) {
            return "";
        }
        
        Display display = ((WindowManager) sApplicationContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        double height = metrics.heightPixels / metrics.density;
        double weight = metrics.widthPixels / metrics.density;
        return weight + " x " + height;
    }

    /**
     * Get Display (使用指定 Context)
     * 推薦使用此方法，避免依賴全域 Context
     * @param context 上下文
     * @return Weight、Height
     */
    public static String getDisplay(Context context) {
        if (context == null) {
            return "";
        }
        
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        double height = metrics.heightPixels / metrics.density;
        double weight = metrics.widthPixels / metrics.density;
        return weight + " x " + height;
    }
}
