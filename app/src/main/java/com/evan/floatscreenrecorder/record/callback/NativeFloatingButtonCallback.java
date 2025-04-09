package com.evan.floatscreenrecorder.record.callback;


/**
 * Created by Evan on 2021/12/6.
 * <p>
 * Description：
 * Native Floating Button Callback
 */

public interface NativeFloatingButtonCallback {

    /**
     * @param customizedChildButtonIndex    Child Button Index
     */
    public void onClick(int customizedChildButtonIndex);

    /**
     * @param msg                           Error Msg
     */
    public void onError(String msg);

    /**
     * @param msg                           Close Msg
     */
    public void onClose(String msg);

}
