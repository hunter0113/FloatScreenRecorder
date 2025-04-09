package com.evan.floatscreenrecorder.common.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.evan.floatscreenrecorder.common.constant.PermissionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 2020/6/28.
 * Description：
 * Permission Util
 */

public class PermissionUtils {
    private static final int Permission_Request_Code = 312;

    /**
     * 獲取錄屏的權限，錄音，文件讀寫
     */

    public static boolean checkPermission(Activity activity, List<String> permissionList, String type) {

        if (Build.VERSION.SDK_INT >= 23) {
            //checkPermission 為所有權限相加 有許可的權限為0 其他為-1
            int checkPermission = 0;

            for (String item : permissionList) {
                checkPermission = checkPermission + ContextCompat.checkSelfPermission(activity, item);
            }

            String[] strArray = permissionList.toArray(new String[0]);

            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, strArray, Permission_Request_Code);
                return false;

            }
            return true;
        }
        // 不須權限
        return true;
    }


    /**
     * 根據請求返回對應權限
     *
     * @param list
     * @return
     */

    public static List<String> selectPermission(List<PermissionType> list) {
        List<String> permissionList = new ArrayList<>();
        for (PermissionType requestCode : list) {
            switch (requestCode) {

                // 相機
                case CAMERA:
                    permissionList.add(Manifest.permission.CAMERA);
                    break;

                // 日歷
                case READ_CALENDAR:
                    permissionList.add(Manifest.permission.READ_CALENDAR);
                    break;

                case WRITE_CALENDAR:
                    permissionList.add(Manifest.permission.WRITE_CALENDAR);
                    break;

                // 聯繫人
                case READ_CONTACTS:
                    permissionList.add(Manifest.permission.READ_CONTACTS);
                    break;

                case WRITE_CONTACTS:
                    permissionList.add(Manifest.permission.WRITE_CONTACTS);
                    break;

                case GET_ACCOUNTS:
                    permissionList.add(Manifest.permission.GET_ACCOUNTS);
                    break;

                // 位置
                case ACCESS_FINE_LOCATION:
                    permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                    break;

                case ACCESS_COARSE_LOCATION:
                    permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                    break;

                // 麥克風
                case RECORD_AUDIO:
                    permissionList.add(Manifest.permission.RECORD_AUDIO);
                    break;

                // 手機
                case READ_PHONE_STATE:
                    permissionList.add(Manifest.permission.READ_PHONE_STATE);
                    break;

                case CALL_PHONE:
                    permissionList.add(Manifest.permission.CALL_PHONE);
                    break;

                case READ_CALL_LOG:
                    permissionList.add(Manifest.permission.READ_CALL_LOG);
                    break;

                case WRITE_CALL_LOG:
                    permissionList.add(Manifest.permission.WRITE_CALL_LOG);
                    break;

                case ADD_VOICE_MAIL:
                    permissionList.add(Manifest.permission.ADD_VOICEMAIL);
                    break;

                case USE_SIP:
                    permissionList.add(Manifest.permission.USE_SIP);
                    break;

                case PROCESS_OUTGOING_CALLS:
                    permissionList.add(Manifest.permission.PROCESS_OUTGOING_CALLS);
                    break;

                // 傳感器
                case BODY_SENSORS:
                    permissionList.add(Manifest.permission.BODY_SENSORS);
                    break;

                // 短信
                case SEND_SMS:
                    permissionList.add(Manifest.permission.SEND_SMS);
                    break;

                case RECEIVE_SMS:
                    permissionList.add(Manifest.permission.RECEIVE_SMS);
                    break;

                case READ_SMS:
                    permissionList.add(Manifest.permission.READ_SMS);
                    break;

                case RECEIVE_WAP_PUSH:
                    permissionList.add(Manifest.permission.RECEIVE_WAP_PUSH);
                    break;

                case RECEIVE_MMS:
                    permissionList.add(Manifest.permission.RECEIVE_MMS);
                    break;

                // 儲存空間
                case READ_EXTERNAL_STORAGE:
                    permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    break;

                case WRITE_EXTERNAL_STORAGE:
                    permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    break;

                case READ_MEDIA_VIDEO:
                    permissionList.add(Manifest.permission.READ_MEDIA_VIDEO);
                    break;

                case POST_NOTIFICATIONS:
                    permissionList.add(Manifest.permission.POST_NOTIFICATIONS);
                    break;

                default:
                    break;
            }
        }
        return permissionList;
    }

}
