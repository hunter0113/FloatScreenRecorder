package com.evan.floatscreenrecorder.record.activity;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.Nullable;


import com.evan.floatscreenrecorder.common.constant.RecordingConstants;
import com.evan.floatscreenrecorder.common.util.FileUtil;
import com.evan.floatscreenrecorder.record.manager.RecordingManager;
import com.evan.floatscreenrecorder.record.service.RecordingService;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Evan on 2021/11/18.
 * <p>
 * Description： For Video Share
 */

public class ShareVideoActivity extends Activity {

    private final static int SHARE_REQUEST_CODE = RecordingConstants.SHARE_REQUEST_CODE;
    private final static int SHARE_REQUEST_BROADCAST_RECEIVER_CODE = 10;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        sendIntent.putExtra(Intent.EXTRA_STREAM, RecordingManager.getManagerFinalFileUri());
        sendIntent.setType("video/mp4");

        startActivityForResult(sendIntent, SHARE_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != SHARE_REQUEST_CODE) {
            return;
        }

        // 小於Android 10 以下 需刪除分享用的Uri檔案(即初始檔案)，僅留下複製後的檔案。
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    RecordingService.deleteFinalFile();

                    ContentResolver resolver = getContentResolver();
                    String str = getFilePathFromContentUri(RecordingManager.getManagerFinalFileUri(), resolver);
                    File file = new File(str);
                    FileUtil.deleteSDFile(file.getAbsolutePath());
                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
                }
            });
        }


        RecordingManager.safelyCallShareVideoFinish();
        finish();
        overridePendingTransition(RecordingConstants.TRANSITION_NO_ANIMATION, RecordingConstants.TRANSITION_NO_ANIMATION);
    }


    //=============================================================
    /**
     * Get File Path From ContentUri
     */
    //=============================================================
    private String getFilePathFromContentUri(Uri selectedVideoUri, ContentResolver contentResolver) {

        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();

        return filePath;

    }
}
