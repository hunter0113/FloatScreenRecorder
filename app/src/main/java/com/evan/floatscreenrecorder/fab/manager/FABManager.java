package com.evan.floatscreenrecorder.fab.manager;

import static com.evan.floatscreenrecorder.common.constant.Constants.FAB_CLOSE_ACTION;

import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;

import com.evan.floatscreenrecorder.common.util.DeviceUtils;
import com.evan.floatscreenrecorder.fab.model.FloatingConfigurationModel;

/**
 * 浮動按鈕管理器
 * 負責管理浮動按鈕的狀態、位置和配置
 */
public class FABManager {

    // WindowManager 相關
    private static WindowManager windowManager;
    private static WindowManager.LayoutParams floatingLayoutParams;
    private static WindowManager.LayoutParams deleteLayoutParams;
    private static WindowManager.LayoutParams childLayoutParams;

    // 配置模型
    private static FloatingConfigurationModel configurationModel;

    // 位置相關
    private static int screenInvalidMargin; // 螢幕邊距
    private static int lastLocationX; // 最後X座標
    private static int lastLocationY; // 最後Y座標

    // 狀態相關
    private static boolean fromExternalInterface = false; // 是否從外部介面啟動
    private static boolean isServiceAlive = false; // 服務是否存活
    private static boolean isFloatingShowing = false; // 是否正在顯示
    private static boolean isUp = false; // 是否位於上半部

    // 浮動狀態常數
    public static final int UNFOLDING = 0; // 展開狀態
    public static final int FOLDING = 1; // 收合狀態
    private static int floatingStatus = FOLDING; // 當前浮動狀態

    // 尺寸相關
    private static int spacing; // 按鈕間距
    private static int sideLength; // 按鈕邊長

    // ==================== Getter 和 Setter 方法 ====================

    /**
     * 服務狀態相關
     */
    public static boolean isServiceAlive() {
        return isServiceAlive;
    }

    public static void setServiceAlive(boolean serviceAlive) {
        FABManager.isServiceAlive = serviceAlive;
    }

    /**
     * 位置相關
     */
    public static int getLastLocationX() {
        return lastLocationX;
    }

    public static void setLastLocationX(int lastX) {
        FABManager.lastLocationX = lastX;
    }

    public static int getLastLocationY() {
        return lastLocationY;
    }

    public static void setLastLocationY(int lastY) {
        FABManager.lastLocationY = lastY;
    }

    /**
     * 浮動狀態相關
     */
    public static int getFloatingStatus() {
        return floatingStatus;
    }

    public static void setFloatingStatus(int status) {
        FABManager.floatingStatus = status;
    }

    public static boolean isFloatingShowing() {
        return isFloatingShowing;
    }

    public static void setFloatingShowing(boolean showing) {
        FABManager.isFloatingShowing = showing;
    }

    /**
     * WindowManager 相關
     */
    public static WindowManager getWindowManager() {
        return windowManager;
    }

    public static void setWindowManager(WindowManager manager) {
        windowManager = manager;
    }

    public static WindowManager.LayoutParams getFloatingLayoutParams() {
        return floatingLayoutParams;
    }

    public static void setFloatingLayoutParams(WindowManager.LayoutParams params) {
        floatingLayoutParams = params;
    }

    public static WindowManager.LayoutParams getDeleteLayoutParams() {
        return deleteLayoutParams;
    }

    public static void setDeleteLayoutParams(WindowManager.LayoutParams params) {
        deleteLayoutParams = params;
    }

    public static WindowManager.LayoutParams getChildLayoutParams() {
        return childLayoutParams;
    }

    public static void setChildLayoutParams(WindowManager.LayoutParams params) {
        childLayoutParams = params;
    }

    /**
     * 配置相關
     */
    public static FloatingConfigurationModel getConfigurationModel() {
        return configurationModel;
    }

    public static void setConfigurationModel(FloatingConfigurationModel model) {
        configurationModel = model;
    }

    /**
     * 尺寸和間距相關
     */
    public static int getScreenInvalidMargin() {
        return screenInvalidMargin;
    }

    public static void setScreenInvalidMargin(int margin) {
        screenInvalidMargin = margin;
    }

    public static int getSpacing() {
        return spacing;
    }

    public static void setSpacing(int spacingValue) {
        spacing = spacingValue;
    }

    public static int getSideLength() {
        return sideLength;
    }

    public static void setSideLength(int length) {
        sideLength = length;
    }

    /**
     * 外部介面相關
     */
    public static boolean getFromExternalInterface() {
        return fromExternalInterface;
    }

    public static void setFromExternalInterface(boolean fromExternal) {
        fromExternalInterface = fromExternal;
    }

    /**
     * 位置方向相關
     */
    public static boolean isUp() {
        return isUp;
    }

    public static void setUp(boolean up) {
        isUp = up;
    }



    // ==================== 業務方法 ====================

    /**
     * 重置所有狀態
     * 用於清理靜態變數，防止記憶體洩漏
     */
    public static void reset() {
        windowManager = null;
        floatingLayoutParams = null;
        deleteLayoutParams = null;
        childLayoutParams = null;
        configurationModel = null;
        
        screenInvalidMargin = 0;
        lastLocationX = 0;
        lastLocationY = 0;
        
        fromExternalInterface = false;
        isServiceAlive = false;
        isFloatingShowing = false;
        isUp = false;
        floatingStatus = FOLDING;
        
        spacing = 0;
        sideLength = 0;
    }
}
