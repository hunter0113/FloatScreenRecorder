package com.evan.floatscreenrecorder.record.activity;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import static com.evan.floatscreenrecorder.common.constant.Constants.LOOP_RECORD;
import static com.evan.floatscreenrecorder.common.constant.Constants.NOTIFICATIONS;
import static com.evan.floatscreenrecorder.common.constant.Constants.PERMISSION_LIST;
import static com.evan.floatscreenrecorder.common.constant.Constants.PERMISSION_TYPE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.evan.floatscreenrecorder.common.constant.Constants;
import com.evan.floatscreenrecorder.common.constant.PermissionType;
import com.evan.floatscreenrecorder.common.util.PermissionUtils;
import com.evan.floatscreenrecorder.record.manager.RecordingManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 2021/7/8.
 * <p>
 * Description： Get Permission
 */

public class PermissionActivity extends Activity {

    private final List<PermissionType> permissionList = new ArrayList<>();
    private AlertDialog permissionDialog;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Intent intent = getIntent();
        String[] strings = intent.getStringArrayExtra(PERMISSION_LIST);

        for (String string : strings) {
            permissionList.add(PermissionType.fromString(string));
        }

        type = intent.getStringExtra(PERMISSION_TYPE);


        if (!PermissionUtils.checkPermission(this, PermissionUtils.selectPermission(permissionList), type)) {
            return;
        }

        // All permissions have been passed //
        permissionSuccessHandle(type);

    }


    @Override
    protected void onStart() {
        super.onStart();

        if (null == permissionDialog) {
            return;
        }

        if (permissionDialog.isShowing()) {
            permissionDialog.dismiss();
        }

        if (!PermissionUtils.checkPermission(this, PermissionUtils.selectPermission(permissionList), type)) {
            return;
        }

        // All permissions have been passed //
        permissionSuccessHandle(type);
    }


    @Override
    protected void onDestroy() {
        if (permissionDialog != null) {
            permissionDialog.dismiss();
        }
        super.onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (type.equals(NOTIFICATIONS)) {
            // 通知無自定義視窗供跳轉
            finish();
            overridePendingTransition(0, 0);
            return;
        }

        setRecordPermissionDialog();

        int permissionCount = 0;
        boolean shouldShowRequest = false;

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PERMISSION_GRANTED) {
                permissionCount++;
                continue;
            }

            boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
            if (showRequestPermission) {
                shouldShowRequest = true;
            }
        }


        if (permissionCount == grantResults.length && grantResults.length != 0) {
            // All permissions have been passed //
            permissionSuccessHandle(type);
            return;

        } else if (!permissionDialog.isShowing() && !shouldShowRequest) {
            permissionDialog.show();
            return;
        }

        permissionErrorHandle(type);
    }


    //=============================================================
    /**
     * Set Permission Dialog
     */
    //=============================================================
    private void setRecordPermissionDialog() {
        permissionDialog = new AlertDialog.Builder(this).setTitle("需要權限").setMessage("需要錄影權限").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionErrorHandle(type);
            }
        }).setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + PermissionActivity.this.getPackageName()));
                PermissionActivity.this.startActivity(intent);
                // 回來時會觸發onStart，關閉permissionDialog，並重新確認一次權限。 //
            }
        }).setCancelable(false).create();
    }


    //=============================================================
    /**
     * Permission Success Handle
     */
    //=============================================================
    private void permissionSuccessHandle(String type) {
        if (type.equals(LOOP_RECORD)) {
            goToActivity(this);
        }

        finish();
        overridePendingTransition(0, 0);
    }


    //=============================================================
    /**
     * Permission Error Handle
     */
    //=============================================================
    private void permissionErrorHandle(String type) {
        if (type.equals(LOOP_RECORD) && null != RecordingManager.getRecordingStatusCallback()) {
            RecordingManager.getRecordingStatusCallback().onError(Constants.RECORD_USER_REFUSE_MSG);
        }

        finish();
        overridePendingTransition(0, 0);
    }


    //=============================================================
    /**
     * Go To Activity
     */
    //=============================================================
    private static void goToActivity(Activity activity) {
        Intent intent = new Intent(activity, RecordingActivity.class);
        activity.startActivity(intent);
    }

}
