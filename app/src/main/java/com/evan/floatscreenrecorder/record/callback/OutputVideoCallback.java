package com.evan.floatscreenrecorder.record.callback;

/**
 * Created by Evan on 2021/11/16.
 * <p>
 * Description：
 */
public interface OutputVideoCallback {
    /**
     * @param videoPath    Video Path
     */
    public void onSuccess(String videoPath);


    /**
     * @param videoMsg Error Msg
     *
     */
    public void onError(String videoMsg);
}
