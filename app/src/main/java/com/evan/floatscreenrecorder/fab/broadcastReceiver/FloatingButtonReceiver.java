package com.evan.floatscreenrecorder.fab.broadcastReceiver;


import static com.evan.floatscreenrecorder.common.constant.Constants.*;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.evan.floatscreenrecorder.fab.service.FABService;


public class FloatingButtonReceiver extends BroadcastReceiver {
    private FABService FABService;

    public FloatingButtonReceiver(FABService FABService) {
        this.FABService = FABService;
    }

    public FloatingButtonReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (null == intent.getAction()) {
            return;
        }

        switch (intent.getAction()) {
            case FAB_CLOSE_ACTION:
                FABService.fabClose();
                break;

            case FAB_DISPLAY_ACTION:
                FABService.fabDisplay();
                break;

            case FAB_HIDE_ACTION:
                FABService.fabHide();
                break;

            case FAB_RECORDING_STATUS_CALLBACK_SUCCESS_ACTION:
                FABService.recordingStatusSuccess();
                break;

            case FAB_RECORDING_STATUS_CALLBACK_ERROR_ACTION:
                FABService.recordingStatusError(intent);
                break;

        }
    }
}
