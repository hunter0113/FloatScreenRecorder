package com.evan.floatscreenrecorder.common.constant;

/**
 * 錄製相關常數
 * 統一管理錄製功能中的硬編碼值
 */
public final class RecordingConstants {
    
    // ==================== 錄製時長相關常數 ====================
    /** 預設錄製容量（秒） */
    public static final int DEFAULT_RECORD_CAPACITY_SECONDS = 30;
    /** 錄製分段時長（秒） */
    public static final int RECORDING_SEGMENT_DURATION = 30;
    /** 最大錄製時長（秒） */
    public static final int RECORDING_MAX_DURATION = 300;
    /** 最小錄製時長（秒）- 用於驗證錄製是否有效 */
    public static final int MIN_RECORD_DURATION_SECONDS = 2;
    /** 單一檔案最小錄製時長（秒） */
    public static final int MIN_SINGLE_FILE_DURATION_SECONDS = 2;
    
    // ==================== 錄影品質相關常數 ====================
    /** 1080p 視訊位元率（bps） */
    public static final int VIDEO_BITRATE_1080P = 8000000;
    /** 720p 視訊位元率（bps） */
    public static final int VIDEO_BITRATE_720P = 5000000;
    /** 視訊幀率（fps） */
    public static final int VIDEO_FRAME_RATE = 24;
    /** 視訊捕獲率（fps） */
    public static final int VIDEO_CAPTURE_RATE = 24;
    /** 音訊位元率（bps） */
    public static final int AUDIO_BITRATE = 128000;
    /** 音訊採樣率（Hz） */
    public static final int AUDIO_SAMPLE_RATE = 44100;
    
    // ==================== 時間相關常數 ====================
    /** 倒數計時間隔（毫秒） */
    public static final int COUNTDOWN_INTERVAL_MS = 1000;
    /** 從背景返回延遲（毫秒） */
    public static final int BACKGROUND_RETURN_DELAY = 500;
    /** 浮動按鈕動畫時長（毫秒） */
    public static final int FAB_ANIMATION_DURATION_MS = 500;
    
    // ==================== 浮動按鈕尺寸常數 ====================
    /** 大尺寸（dp） */
    public static final int FAB_SIZE_LARGE = 60;
    /** 中尺寸（dp） */
    public static final int FAB_SIZE_MEDIUM = 50;
    /** 小尺寸（dp） */
    public static final int FAB_SIZE_SMALL = 40;
    
    // ==================== 浮動按鈕間距和邊距 ====================
    /** 按鈕間距（dp） */
    public static final int FAB_SPACING = 5;
    /** 螢幕邊距（dp） */
    public static final int FAB_MARGIN = 20;
    /** 觸摸移動閾值（像素）- 用於判斷是點擊還是拖動 */
    public static final int FAB_TOUCH_MOVE_THRESHOLD = 5;
    /** 刪除區域中心位置比例（螢幕高度的百分比） */
    public static final double FAB_DELETE_AREA_CENTER_RATIO = 0.8;
    
    // ==================== 動畫相關常數 ====================
    /** 動畫時長（毫秒） */
    public static final int ANIMATION_DURATION = 300;
    /** 動畫延遲（毫秒） */
    public static final int ANIMATION_DELAY = 100;
    
    // ==================== 檔案相關常數 ====================
    /** 視訊檔案副檔名 */
    public static final String VIDEO_EXTENSION = ".mp4";
    /** 視訊檔案前綴 */
    public static final String VIDEO_PREFIX = "screen_record_";
    /** 日期時間格式 */
    public static final String DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss";
    /** 日期時間格式（簡化版） */
    public static final String DATE_FORMAT_SIMPLE = "yyyyMMdd_HHmmss";
    
    // ==================== 通知相關常數 ====================
    /** 通知 ID */
    public static final int NOTIFICATION_ID = 11235;
    /** 通知頻道 ID */
    public static final String NOTIFICATION_CHANNEL_ID = "notification_channel_id";
    /** 通知頻道名稱 */
    public static final String NOTIFICATION_CHANNEL_NAME = "螢幕錄製";
    
    // ==================== 權限請求碼 ====================
    /** 通用權限請求碼 */
    public static final int PERMISSION_REQUEST_CODE = 312;
    /** 浮動視窗權限請求碼 */
    public static final int FLOATING_PERMISSION_REQUEST_CODE = 0;
    /** 錄影權限請求碼 */
    public static final int RECORDING_PERMISSION_REQUEST_CODE = 1;
    /** 分享請求碼 */
    public static final int SHARE_REQUEST_CODE = 1;
    
    // ==================== 自定義按鈕相關 ====================
    /** 自定義按鈕起始索引 */
    public static final int CUSTOMIZED_BUTTON_START_INDEX = 101;
    /** 預設非使用狀態透明度 */
    public static final double DEFAULT_NON_USE_ALPHA = 0.5;
    
    // ==================== Handler 訊息類型 ====================
    /** 倒數計時訊息類型 */
    public static final int MSG_TYPE_COUNT_DOWN = 110;
    
    // ==================== 檔案時間偏移（秒） ====================
    /** 檔案 A 時間偏移（秒） */
    public static final int FILE_TIME_OFFSET_A = 0;
    /** 檔案 B 時間偏移（秒） */
    public static final int FILE_TIME_OFFSET_B = 1;
    /** 檔案 C 時間偏移（秒） */
    public static final int FILE_TIME_OFFSET_C = 2;
    /** 最終檔案時間偏移（秒） */
    public static final int FILE_TIME_OFFSET_FINAL = 3;
    
    // ==================== 初始值常數 ====================
    /** 初始錄製時長（秒） */
    public static final int INITIAL_RECORD_SECONDS = 0;
    /** Handler 立即發送延遲時間（毫秒） */
    public static final int HANDLER_SEND_IMMEDIATELY = 0;
    /** 動畫過渡無效果 */
    public static final int TRANSITION_NO_ANIMATION = 0;
    
    // ==================== 其他常數 ====================
    /** PendingIntent 請求碼 */
    public static final int PENDING_INTENT_REQUEST_CODE = 0;
    /** 螢幕中心位置計算（除以2） */
    public static final int SCREEN_CENTER_DIVISOR = 2;
    
    private RecordingConstants() {
        // 防止實例化
    }
} 