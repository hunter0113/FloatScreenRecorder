package com.evan.floatscreenrecorder.common.manager;

import android.content.Context;
import android.util.Log;

import com.evan.floatscreenrecorder.common.exception.RecordingException;
import com.evan.floatscreenrecorder.common.util.ToastUtil;

/**
 * 默認錯誤處理器實現
 * 提供基本的錯誤處理邏輯
 */
public class DefaultErrorHandler implements ErrorHandler {
    
    private static final String TAG = "DefaultErrorHandler";
    private final Context context;
    
    public DefaultErrorHandler(Context context) {
        this.context = context;
    }
    
    @Override
    public void onRecordingError(RecordingException exception) {
        Log.e(TAG, "錄製錯誤: " + exception.getMessage(), exception);
        
        switch (exception.getErrorType()) {
            case PERMISSION_DENIED:
                ToastUtil.showToast(context, "錄製權限被拒絕，請在設定中開啟權限");
                break;
            case DEVICE_NOT_SUPPORTED:
                ToastUtil.showToast(context, "此設備不支援螢幕錄製功能");
                break;
            case STORAGE_INSUFFICIENT:
                ToastUtil.showToast(context, "儲存空間不足，請清理空間後重試");
                break;
            case RECORDING_FAILED:
                ToastUtil.showToast(context, "錄製失敗，請重試");
                break;
            case FILE_OPERATION_FAILED:
                ToastUtil.showToast(context, "檔案操作失敗，請檢查儲存權限");
                break;
            case SERVICE_NOT_AVAILABLE:
                ToastUtil.showToast(context, "錄製服務不可用，請重啟應用");
                break;
            default:
                ToastUtil.showToast(context, "錄製過程中發生錯誤");
                break;
        }
    }
    
    @Override
    public void onPermissionError(RecordingException exception) {
        Log.e(TAG, "權限錯誤: " + exception.getMessage(), exception);
        ToastUtil.showToast(context, "需要相關權限才能使用此功能");
    }
    
    @Override
    public void onGeneralError(String errorMessage) {
        Log.e(TAG, "一般錯誤: " + errorMessage);
        ToastUtil.showToast(context, errorMessage);
    }
    
    @Override
    public void onUnknownError(Throwable throwable) {
        Log.e(TAG, "未知錯誤", throwable);
        ToastUtil.showToast(context, "發生未知錯誤，請重試");
    }
} 