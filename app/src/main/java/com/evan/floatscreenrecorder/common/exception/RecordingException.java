package com.evan.floatscreenrecorder.common.exception;

/**
 * 錄製相關異常
 * 統一處理錄製功能中的各種異常情況
 */
public class RecordingException extends Exception {
    
    private final ErrorType errorType;
    
    public enum ErrorType {
        PERMISSION_DENIED("權限被拒絕"),
        DEVICE_NOT_SUPPORTED("設備不支援"),
        STORAGE_INSUFFICIENT("儲存空間不足"),
        RECORDING_FAILED("錄製失敗"),
        FILE_OPERATION_FAILED("檔案操作失敗"),
        SERVICE_NOT_AVAILABLE("服務不可用"),
        UNKNOWN_ERROR("未知錯誤");
        
        private final String message;
        
        ErrorType(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    public RecordingException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
    
    public RecordingException(ErrorType errorType, String detailMessage) {
        super(detailMessage);
        this.errorType = errorType;
    }
    
    public RecordingException(ErrorType errorType, Throwable cause) {
        super(errorType.getMessage(), cause);
        this.errorType = errorType;
    }
    
    public RecordingException(ErrorType errorType, String detailMessage, Throwable cause) {
        super(detailMessage, cause);
        this.errorType = errorType;
    }
    
    public ErrorType getErrorType() {
        return errorType;
    }
} 