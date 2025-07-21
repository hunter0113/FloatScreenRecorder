package com.evan.floatscreenrecorder.common.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.evan.floatscreenrecorder.common.constant.RecordingConstants;

/**
 * 配置管理器
 * 負責管理應用的各種配置參數
 */
public class ConfigurationManager {
    
    private static final String PREF_NAME = "FloatScreenRecorderConfig";
    private static final String KEY_FAB_SIZE = "fab_size";
    private static final String KEY_FAB_POSITION = "fab_position";
    private static final String KEY_RECORDING_QUALITY = "recording_quality";
    private static final String KEY_AUTO_SAVE = "auto_save";
    private static final String KEY_NOTIFICATION_ENABLED = "notification_enabled";
    
    private static ConfigurationManager instance;
    private final SharedPreferences preferences;
    
    private ConfigurationManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    public static synchronized ConfigurationManager getInstance(Context context) {
        if (instance == null) {
            instance = new ConfigurationManager(context.getApplicationContext());
        }
        return instance;
    }
    
    /**
     * 獲取浮動按鈕尺寸
     * @return 按鈕尺寸類型
     */
    public int getFABSize() {
        return preferences.getInt(KEY_FAB_SIZE, RecordingConstants.FAB_SIZE_MEDIUM);
    }
    
    /**
     * 設置浮動按鈕尺寸
     * @param size 按鈕尺寸
     */
    public void setFABSize(int size) {
        preferences.edit().putInt(KEY_FAB_SIZE, size).apply();
    }
    
    /**
     * 獲取浮動按鈕位置
     * @return 按鈕位置
     */
    public String getFABPosition() {
        return preferences.getString(KEY_FAB_POSITION, "BOTTOM_RIGHT");
    }
    
    /**
     * 設置浮動按鈕位置
     * @param position 按鈕位置
     */
    public void setFABPosition(String position) {
        preferences.edit().putString(KEY_FAB_POSITION, position).apply();
    }
    
    /**
     * 獲取錄製品質
     * @return 錄製品質
     */
    public int getRecordingQuality() {
        return preferences.getInt(KEY_RECORDING_QUALITY, 720); // 預設 720p
    }
    
    /**
     * 設置錄製品質
     * @param quality 錄製品質
     */
    public void setRecordingQuality(int quality) {
        preferences.edit().putInt(KEY_RECORDING_QUALITY, quality).apply();
    }
    
    /**
     * 是否自動儲存
     * @return 是否自動儲存
     */
    public boolean isAutoSave() {
        return preferences.getBoolean(KEY_AUTO_SAVE, true);
    }
    
    /**
     * 設置自動儲存
     * @param autoSave 是否自動儲存
     */
    public void setAutoSave(boolean autoSave) {
        preferences.edit().putBoolean(KEY_AUTO_SAVE, autoSave).apply();
    }
    
    /**
     * 是否啟用通知
     * @return 是否啟用通知
     */
    public boolean isNotificationEnabled() {
        return preferences.getBoolean(KEY_NOTIFICATION_ENABLED, true);
    }
    
    /**
     * 設置通知啟用狀態
     * @param enabled 是否啟用
     */
    public void setNotificationEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_NOTIFICATION_ENABLED, enabled).apply();
    }
    
    /**
     * 重置所有配置為預設值
     */
    public void resetToDefaults() {
        preferences.edit().clear().apply();
    }
    
    /**
     * 獲取所有配置的摘要
     * @return 配置摘要
     */
    public String getConfigurationSummary() {
        return String.format(
            "FAB Size: %d, Position: %s, Quality: %dp, Auto Save: %s, Notification: %s",
            getFABSize(),
            getFABPosition(),
            getRecordingQuality(),
            isAutoSave() ? "Enabled" : "Disabled",
            isNotificationEnabled() ? "Enabled" : "Disabled"
        );
    }
} 