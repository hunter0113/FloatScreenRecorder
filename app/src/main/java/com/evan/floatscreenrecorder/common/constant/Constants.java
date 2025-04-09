package com.evan.floatscreenrecorder.common.constant;

/**
 * Created by Evan on 2023/11/12.
 * <p>
 * Description：
 * Constants
 */

public class Constants {

    public static final String VERSION = "Android: ";
    public static final int ALL = 1;


    /**
     * Loop Recording
     */
    public static final String RECORD_VERSION_NOT_SUPPORT_MSG = "Version requires Android 8 or above.";
    public static final String RECORD_NOT_TURNED_ON_MSG = "Loop recording has not been turned on.";
    public static final String RECORD_NO_STORAGE_MSG = "Insufficient device storage.";
    public static final String RECORD_NOTIFICATION_CLOSE_MSG = "Loop recording is closed by the user from the notification.";
    public static final String RECORD_USER_REFUSE_MSG = "The user chooses to refuse to grant permission.";
    public static final String RECORD_ERROR_MSG = "Failed to output the recorded video.";
    public static final String RECORD_DEVICE_NOT_SUPPORT_MSG = "This device does not support screen recording.";



    /**
     * FAB
     */
    public static final int FAB_CUSTOMIZED_START_INDEX = 101;

    public static final String FAB_LOG_CHILD_IMAGE_MISSING_IMAGE = "CustomizedChildButtonImageList is less than customizedNumberOfChildButtons, Some buttons will be missing images.";
    public static final String FAB_REMOVE_MESSAGE = "The user drags and removes the floating window.";

    public static final String DEFAULT_OPTION_IMAGE = "OptionImage";
    public static final String DEFAULT_DELETE_IMAGE = "DeleteImage";
    public static final String DEFAULT_START_IMAGE = "StartImage";
    public static final String DEFAULT_CANCEL_IMAGE = "CancelImage";
    public static final String DEFAULT_SHARE_IMAGE = "ShareImage";
    public static final String DEFAULT_CUSTOMER_SERVICE_IMAGE = "CustomerServiceImage";
    public static final String DEFAULT_CUSTOM_IMAGE = "CustomImage";


    /**
     * Permission
     */
    public static final String PERMISSION_LIST = "PermissionList";
    public static final String PERMISSION_TYPE = "permissionType";
    public static final String LOOP_RECORD = "LoopRecord";
    public static final String NOTIFICATIONS = "Notifications";


    /**
     * BroadcastReceiver Action Name
     */
    public final static String FAB_CLOSE_ACTION = "stop_fab_and_service";
    public final static String FAB_HIDE_ACTION = "hind_fab";
    public final static String FAB_DISPLAY_ACTION = "display_fab";
    public final static String RECORD_STOP_ACTION = "stop_record";
    public final static String FAB_RECORDING_STATUS_CALLBACK_SUCCESS_ACTION = "record_success";
    public final static String FAB_RECORDING_STATUS_CALLBACK_ERROR_ACTION = "record_error";


    /**
     * Notification
     */
    public final static String NOTIFICATION_RECODE_CHANNEL_ID = "notification_channel_id";
    public final static String NOTIFICATION_RECODE_CHANNEL_NAME = "螢幕錄製";
    public final static int NOTIFICATION_FOREGROUND_ID = 11235;
}
