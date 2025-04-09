package com.evan.floatscreenrecorder.fab.manager;


import static com.evan.floatscreenrecorder.common.constant.Constants.FAB_CLOSE_ACTION;

import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;

import com.evan.floatscreenrecorder.common.util.DeviceUtils;
import com.evan.floatscreenrecorder.fab.model.FloatingConfigurationModel;
import com.evan.floatscreenrecorder.record.callback.CloseFloatingButtonCallback;

/**
 * Created by Evan on 2021/11/9.
 * <p>
 * Description：
 */
public class FABManager {

    private static WindowManager sm_windowManager;
    private static WindowManager.LayoutParams sm_floatingLayoutParams;
    private static WindowManager.LayoutParams sm_deleteLayoutParams;
    private static FloatingConfigurationModel sm_configurationModel;

    private static WindowManager.LayoutParams sm_childLayoutParams;

    /**
     * 紀錄退至後台時的位置，重新顯示時依此位置
     */
    private static int sm_nScreenInvalidMargin;

    /**
     * 判斷是重開新的還是從後台回來
     */
    private static boolean sm_fromExternalInterface = false;

    /**
     * 紀錄退至後台時的位置，重新顯示時依此位置
     */
    private static int sm_nLastLocationX;
    private static int sm_nLastLocationY;

    /**
     * 選單展開 / 摺疊狀態
     */
    public static final int UNFOLDING = 0;
    public static final int FOLDING = 1;
    private static int sm_nFloatingStatus = FOLDING;

    /**
     * 子view之間間距
     */
    private static int sm_nSpacing;

    /**
     * 子所有Image的邊長
     */
    private static int sm_nSideLength;

    /**
     * 當前FAB Service是否存在
     */
    private static boolean sm_isServiceAlive = false;

    /**
     * 當前FAB 是否顯示
     */
    private static boolean sm_isFloatingShowing = false;

    /**
     * FAB 位於上半部還是下半部
     */
    private static boolean sm_isUp = false;

    /**
     * Getter And Setter
     */
    public static boolean isServiceAlive() {
        return sm_isServiceAlive;
    }

    public static void setServiceAlive(boolean isServiceAlive) {
        FABManager.sm_isServiceAlive = isServiceAlive;
    }


    //=============================================================
    /**
     * 最後的X座標
     */
    //=============================================================
    public static int getLastLocationX() {
        return sm_nLastLocationX;
    }

    public static void setLastLocationX(int lastX) {
        FABManager.sm_nLastLocationX = lastX;
    }


    //=============================================================
    /**
     * 最後的Y座標
     */
    //=============================================================
    public static int getLastLocationY() {
        return sm_nLastLocationY;
    }

    public static void setLastLocationY(int lastY) {
        FABManager.sm_nLastLocationY = lastY;
    }


    //=============================================================
    /**
     * 當前狀態
     */
    //=============================================================
    public static int getFloatingStatus() {
        return sm_nFloatingStatus;
    }

    public static void setFloatingStatus(int floatingStatus) {
        FABManager.sm_nFloatingStatus = floatingStatus;
    }


    //=============================================================
    /**
     * 是否正在顯示
     */
    //=============================================================
    public static boolean isFloatingShowing() {
        return sm_isFloatingShowing;
    }

    public static void setFloatingShowing(boolean isFloatingShowing) {
        FABManager.sm_isFloatingShowing = isFloatingShowing;
    }


    //=============================================================
    /**
     * set && get  WindowManager
     */
    //=============================================================
    public static void setWindowManager(WindowManager manager) {
        sm_windowManager = manager;
    }

    public static WindowManager getWindowManager() {
        return sm_windowManager;
    }


    //=============================================================
    /**
     * set && get  FloatingLayoutParams
     */
    //=============================================================
    public static void setFloatingLayoutParams(WindowManager.LayoutParams params) {
        sm_floatingLayoutParams = params;
    }

    public static WindowManager.LayoutParams getFloatingLayoutParams() {
        return sm_floatingLayoutParams;
    }


    //=============================================================
    /**
     * set && get  Delete LayoutParams
     */
    //=============================================================
    public static WindowManager.LayoutParams getDeleteLayoutParams() {
        return sm_deleteLayoutParams;
    }

    public static void setDeleteLayoutParams(WindowManager.LayoutParams deleteLayoutParams) {
        FABManager.sm_deleteLayoutParams = deleteLayoutParams;
    }


    //=============================================================
    /**
     * set && get ConfigurationModel
     */
    //=============================================================
    public static FloatingConfigurationModel getConfigurationModel() {
        return sm_configurationModel;
    }

    public static void setConfigurationModel(FloatingConfigurationModel model) {
        sm_configurationModel = model;
    }


    //=============================================================
    /**
     * set && get Screen Invalid Margin
     */
    //=============================================================
    public static int getScreenInvalidMargin() {
        return sm_nScreenInvalidMargin;
    }

    public static void setScreenInvalidMargin(int screenInvalidMargin) {
        FABManager.sm_nScreenInvalidMargin = screenInvalidMargin;
    }


    //=============================================================
    /**
     * set && get Spacing
     */
    //=============================================================
    public static int getSpacing() {
        return sm_nSpacing;
    }

    public static void setSpacing(int spacing) {
        sm_nSpacing = spacing;
    }


    //=============================================================
    /**
     * set && get Side Length
     */
    //=============================================================
    public static int getSideLength() {
        return sm_nSideLength;
    }

    public static void setSideLength(int imageLength) {
        FABManager.sm_nSideLength = imageLength;
    }


    //=============================================================
    /**
     * set && get FromExternalInterface
     */
    //=============================================================
    public static boolean getFromExternalInterface() {
        return sm_fromExternalInterface;
    }

    public static void setFromExternalInterface(boolean m_nRestoreDisplay) {
        FABManager.sm_fromExternalInterface = m_nRestoreDisplay;
    }


    //=============================================================
    /**
     * set && get ChildLayoutParams
     */
    //=============================================================
    public static WindowManager.LayoutParams getChildLayoutParams() {
        return sm_childLayoutParams;
    }

    public static void setChildLayoutParams(WindowManager.LayoutParams childLayoutParams) {
        FABManager.sm_childLayoutParams = childLayoutParams;
    }


    //=============================================================
    /**
     * isUp
     */
    //=============================================================
    public static boolean isUp() {
        return sm_isUp;
    }

    public static void setUp(boolean isUp) {
        FABManager.sm_isUp = isUp;
    }



    //=============================================================
    /**
     * Close Floating Button
     */
    //=============================================================
    public static void closeFloatingButton(Context context, CloseFloatingButtonCallback callback) {

        Intent startIntent = new Intent(FAB_CLOSE_ACTION);
        startIntent.setPackage(DeviceUtils.getPackageName());
        context.sendBroadcast(startIntent);

        callback.onSuccess();
    }
}
