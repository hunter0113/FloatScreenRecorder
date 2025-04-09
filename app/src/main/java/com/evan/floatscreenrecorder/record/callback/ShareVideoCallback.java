package com.evan.floatscreenrecorder.record.callback;

/**
 * Created by Evan on 2021/11/16.
 * <p>
 * Description：
 *
 */

public interface ShareVideoCallback {

    /**
     * ShareVideo Success Status
     */
    public void onFinish();

    /**
     * @param shareMsg      Error Msg
     */
    public void onError(String shareMsg);

}
