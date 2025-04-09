package com.evan.floatscreenrecorder.record.callback;


/**
 * Created by Evan on 2021/11/16.
 * <p>
 * Description： 因應common的 FloatingConfigurationModel需要此interface，將此從user permit搬到common
 */
public interface RecordingStatusCallback {

    /**
     * Set Recording Status Success
     * @param state         Recording State
     */
    public void onSuccess(boolean state);

    /**
     * @param recordMsg     Error Msg
     */
    public void onError(String recordMsg);

}
