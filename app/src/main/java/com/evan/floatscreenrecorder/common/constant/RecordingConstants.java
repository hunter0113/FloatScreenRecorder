package com.evan.floatscreenrecorder.common.constant;

/**
 * 錄製相關常數
 * 統一管理錄製功能中的硬編碼值
 */
public final class RecordingConstants {
    
    // 錄製相關常數
    public static final int RECORDING_SEGMENT_DURATION = 30; // 錄製分段時長（秒）
    public static final int RECORDING_MAX_DURATION = 300; // 最大錄製時長（秒）
    
    // 浮動按鈕尺寸常數
    public static final int FAB_SIZE_LARGE = 60; // 大尺寸（dp）
    public static final int FAB_SIZE_MEDIUM = 50; // 中尺寸（dp）
    public static final int FAB_SIZE_SMALL = 40; // 小尺寸（dp）
    
    // 浮動按鈕間距和邊距
    public static final int FAB_SPACING = 5; // 按鈕間距（dp）
    public static final int FAB_MARGIN = 20; // 螢幕邊距（dp）
    
    // 動畫相關常數
    public static final int ANIMATION_DURATION = 300; // 動畫時長（毫秒）
    public static final int ANIMATION_DELAY = 100; // 動畫延遲（毫秒）
    
    // 檔案相關常數
    public static final String VIDEO_EXTENSION = ".mp4";
    public static final String VIDEO_PREFIX = "screen_record_";
    public static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    
    // 通知相關常數
    public static final int NOTIFICATION_ID = 11235;
    public static final String NOTIFICATION_CHANNEL_ID = "notification_channel_id";
    public static final String NOTIFICATION_CHANNEL_NAME = "螢幕錄製";
    
    // 權限請求碼
    public static final int PERMISSION_REQUEST_CODE = 312;
    public static final int FLOATING_PERMISSION_REQUEST_CODE = 0;
    public static final int RECORDING_PERMISSION_REQUEST_CODE = 1;
    
    // 自定義按鈕起始索引
    public static final int CUSTOMIZED_BUTTON_START_INDEX = 101;
    
    // 延遲時間常數
    public static final int BACKGROUND_RETURN_DELAY = 500; // 從背景返回延遲（毫秒）
    
    private RecordingConstants() {
        // 防止實例化
    }
} 