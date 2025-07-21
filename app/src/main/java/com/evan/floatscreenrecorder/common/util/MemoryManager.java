package com.evan.floatscreenrecorder.common.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 記憶體管理工具類
 * 負責管理 Bitmap 和其他資源的回收，防止記憶體洩漏
 */
public class MemoryManager {
    
    private static final String TAG = "MemoryManager";
    
    // 使用 WeakReference 來管理 Bitmap，避免記憶體洩漏
    private static final List<WeakReference<Bitmap>> managedBitmaps = new ArrayList<>();
    
    /**
     * 安全回收 Bitmap
     * @param bitmap 要回收的 Bitmap
     */
    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            try {
                bitmap.recycle();
                Log.d(TAG, "Bitmap 已回收");
            } catch (Exception e) {
                Log.e(TAG, "回收 Bitmap 時發生錯誤", e);
            }
        }
    }
    
    /**
     * 安全回收 BitmapDrawable
     * @param bitmapDrawable 要回收的 BitmapDrawable
     */
    public static void recycleBitmapDrawable(BitmapDrawable bitmapDrawable) {
        if (bitmapDrawable != null) {
            Bitmap bitmap = bitmapDrawable.getBitmap();
            recycleBitmap(bitmap);
        }
    }
    
    /**
     * 安全回收 Drawable
     * @param drawable 要回收的 Drawable
     */
    public static void recycleDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            recycleBitmapDrawable((BitmapDrawable) drawable);
        }
    }
    
    /**
     * 管理 Bitmap（使用 WeakReference）
     * @param bitmap 要管理的 Bitmap
     */
    public static void manageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            managedBitmaps.add(new WeakReference<>(bitmap));
            Log.d(TAG, "Bitmap 已加入管理列表");
        }
    }
    
    /**
     * 清理所有管理的 Bitmap
     */
    public static void cleanupManagedBitmaps() {
        List<WeakReference<Bitmap>> toRemove = new ArrayList<>();
        
        for (WeakReference<Bitmap> ref : managedBitmaps) {
            Bitmap bitmap = ref.get();
            if (bitmap != null) {
                recycleBitmap(bitmap);
                toRemove.add(ref);
            }
        }
        
        managedBitmaps.removeAll(toRemove);
        Log.d(TAG, "已清理 " + toRemove.size() + " 個 Bitmap");
    }
    
    /**
     * 清理所有資源
     * 在 Activity 或 Service 銷毀時調用
     */
    public static void cleanup() {
        cleanupManagedBitmaps();
        System.gc(); // 建議垃圾回收
        Log.d(TAG, "記憶體清理完成");
    }
    
    /**
     * 獲取當前管理的 Bitmap 數量
     * @return Bitmap 數量
     */
    public static int getManagedBitmapCount() {
        int count = 0;
        for (WeakReference<Bitmap> ref : managedBitmaps) {
            if (ref.get() != null) {
                count++;
            }
        }
        return count;
    }
} 