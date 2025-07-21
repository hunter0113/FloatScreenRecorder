package com.evan.floatscreenrecorder.common.manager;

import com.evan.floatscreenrecorder.common.exception.RecordingException;

/**
 * 統一錯誤處理介面
 * 提供標準化的錯誤處理機制
 */
public interface ErrorHandler {
    
    /**
     * 處理錄製相關錯誤
     * @param exception 錄製異常
     */
    void onRecordingError(RecordingException exception);
    
    /**
     * 處理權限相關錯誤
     * @param exception 權限異常
     */
    void onPermissionError(RecordingException exception);
    
    /**
     * 處理一般錯誤
     * @param errorMessage 錯誤訊息
     */
    void onGeneralError(String errorMessage);
    
    /**
     * 處理未知錯誤
     * @param throwable 異常對象
     */
    void onUnknownError(Throwable throwable);
} 