package com.evan.floatscreenrecorder.record.callback;

/**
 * Created by Evan on 2021/11/16.
 * <p>
 * Description：
 */

public interface GetRecordingStatusCallback {

    /**
     * Get Recording Status Success
     * @param state         Recording State
     */
    public void onSuccess(boolean state);

}
