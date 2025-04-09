package com.evan.floatscreenrecorder.common.manager;


import com.evan.floatscreenrecorder.record.callback.NativeFloatingButtonCallback;

/**
 * Created by Evan on 2023/3/8.
 * <p>
 * Description：
 */
public class CallbackManager {


    //=============================================================
    /**
     * Construct
     */
    //=============================================================
    private static NativeFloatingButtonCallback sm_NativeFloatingButtonCallback;


    public static NativeFloatingButtonCallback getNativeFloatingButtonCallback() {
        return sm_NativeFloatingButtonCallback;
    }

    public static void setNativeFloatingButtonCallback(NativeFloatingButtonCallback nativeFloatingButtonCallback) {
        CallbackManager.sm_NativeFloatingButtonCallback = nativeFloatingButtonCallback;
    }
}
