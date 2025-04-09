package com.evan.floatscreenrecorder.common.util;

import static android.os.Environment.DIRECTORY_DCIM;

import static com.evan.floatscreenrecorder.common.util.StrUtil.EMPTY;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;


import com.evan.floatscreenrecorder.record.manager.RecordingManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileUtil {

    /**
     * 刪除文件
     */
    public static void deleteSDFile(String path) {
        deleteSDFile(path, false);
    }

    private static void deleteSDFile(String path, boolean deleteParent) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        File file = new File(path);
        if (!file.exists()) {
            //文件不存在
            return;
        }

        deleteFile(file, deleteParent);
    }

    /**
     * 刪除文件
     */
    private static boolean deleteFile(File file, boolean deleteParent) {
        boolean flag = false;
        if (file.isDirectory()) {
            //是文件夾
            File[] files = file.listFiles();
            if (null == files || files.length == 0) return false;

            for (File value : files) {
                flag = deleteFile(value, true);
                if (!flag) {
                    return false;
                }
            }
            if (deleteParent) {
                flag = file.delete();
            }
        } else {
            flag = file.delete();
        }
        return flag;
    }


    /**
     * 添加到媒體庫
     */
    public static boolean fileScanVideo(Context context, String videoPath, int videoWidth, int videoHeight) {

        File file = new File(videoPath);
        if (file.exists()) {
            Log.e("Evan", "檔案存在");

            long size = file.length();
            String fileName = file.getName();
            long dateTaken = System.currentTimeMillis();

            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.TITLE, fileName); // 標題
            values.put(MediaStore.Video.Media.WIDTH, videoWidth); // 影片寬
            values.put(MediaStore.Video.Media.HEIGHT, videoHeight); // 影片高
            values.put(MediaStore.Video.Media.SIZE, size); // 影片大小;
            values.put(MediaStore.Video.Media.DISPLAY_NAME, fileName);// 文件名
            values.put(MediaStore.Video.Media.DATE_MODIFIED, dateTaken / 1000);// 修改時間
            values.put(MediaStore.Video.Media.DATE_ADDED, dateTaken / 1000); // 添加時間
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                values.put(MediaStore.Video.Media.DATE_TAKEN, dateTaken); // 插入時間

                //Android 11以上 添加到系統圖庫
                if(null == context){
                    Log.e("Evan", "context null");
                }else {
                    Log.e("Evan", "context 非 null");
                }


                ContentResolver resolver = context.getContentResolver();
                Uri collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

                Uri uriSavedVideo = resolver.insert(collection, values);
                try (ParcelFileDescriptor pfd = resolver.openFileDescriptor(uriSavedVideo, "w");
                     FileOutputStream out = new FileOutputStream(pfd.getFileDescriptor());
                     FileInputStream in = new FileInputStream(videoPath)) {

                    byte[] buf = new byte[8192];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }

                    // Set Uri For Share
                    RecordingManager.setManagerFinalFileUri(uriSavedVideo);

                    values.clear();
                    values.put(MediaStore.Video.Media.IS_PENDING, 0);
                    resolver.update(uriSavedVideo, values, null, null);
                } catch (IOException e) {
                    Log.e("Evan", "IOException " + e );
                }

            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                //Android 10 添加到系統圖庫
                values.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/");
                values.put(MediaStore.Video.Media.IS_PENDING, 1);
                ContentResolver resolver = context.getContentResolver();
                Uri collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                Uri uriSavedVideo = resolver.insert(collection, values);

                try (ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uriSavedVideo, "w");
                     FileOutputStream out = new FileOutputStream(pfd.getFileDescriptor());
                     FileInputStream in = new FileInputStream(videoPath)) {

                    byte[] buf = new byte[8192];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }

                    // Set Uri For Share
                    RecordingManager.setManagerFinalFileUri(uriSavedVideo);

                    values.clear();
                    values.put(MediaStore.Video.Media.IS_PENDING, 0);
                    context.getContentResolver().update(uriSavedVideo, values, null, null);

                } catch (IOException e) {
                }

            } else {
                File dirName = Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM);
                File storedFile = new File(dirName, file.getName());

                try (FileInputStream fis = new FileInputStream(file);
                     BufferedInputStream bis = new BufferedInputStream(fis);
                     FileOutputStream fos = new FileOutputStream(storedFile)) {

                    storedFile.createNewFile();
                    byte[] data = new byte[(int) file.length()];
                    int bytesRead = bis.read(data, 0, data.length);
                    if (bytesRead == -1) {
                        // Handle end of stream
                        LogUtil.Debug.d(FileUtil.class, "Reached end of stream");
                    } else if (bytesRead != data.length) {
                        // Handle partial read
                        LogUtil.Debug.d(FileUtil.class, "Read only " + bytesRead + " bytes, expected " + data.length + " bytes");
                    }
                    fos.write(data);
                    fos.flush();
                } catch (IOException e) {

                }

                MediaScannerConnection.scanFile(context, new String[]{storedFile.getAbsolutePath()}, null, null);

                // Set Uri For Share
                if (null != RecordingManager.getShareVideoCallback()) {
                    ContentResolver resolver = context.getContentResolver();
                    Uri uriSavedVideo = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                    RecordingManager.setManagerFinalFileUri(uriSavedVideo);
                    return true;
                }

                RecordingManager.setManagerFinalFileUri(Uri.parse(storedFile.getAbsolutePath()));
            }
            return true;
        }
        return false;
    }


    /**
     * 儲存位置
     */
    public static String getSaveDirectory(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return context.getExternalFilesDir(DIRECTORY_DCIM).getAbsolutePath();
        }

        return EMPTY;
    }


    /**
     * 剩餘空間檢查
     */
    public static boolean CheckStorageSpaceEnough(Context context) {
        if (!getSaveDirectory(context).equals(EMPTY)) {
            File file = new File(getSaveDirectory(context));
            return 200 < file.getFreeSpace() / 1024 / 1024;
        }
        return false;
    }
}
