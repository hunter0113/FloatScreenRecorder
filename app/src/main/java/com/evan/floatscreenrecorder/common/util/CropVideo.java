package com.evan.floatscreenrecorder.common.util;

import static com.evan.floatscreenrecorder.common.util.StrUtil.EMPTY;

import android.util.Log;

import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.FFmpegKitConfig;
import com.arthenica.ffmpegkit.ReturnCode;
import com.arthenica.ffmpegkit.Session;

public class CropVideo {
    public static String cropVideo(String srcPath, String outPath, long startTimeMs, long endTimeMs) {
        Log.e("Evan", "裁剪影片");

        // 构建 FFmpeg 命令
        String command = "-i " + srcPath +
                " -ss " + startTimeMs / 1000 +
                " -t " + (endTimeMs - startTimeMs) / 1000 +
                " -c copy " +
                outPath;

        // 設定 FFmpegKit 配置
        FFmpegKitConfig.enableLogCallback(null);

        // 執行 FFmpeg 命令
        Session session = FFmpegKit.execute(command);

        // 獲取返回碼
        ReturnCode returnCode = session.getReturnCode();

        if (ReturnCode.isSuccess(returnCode)) {
            Log.e("Evan", "裁剪成功");
            return outPath;
        } else {
            Log.e("Evan", "裁剪失敗");
            return EMPTY;
        }
    }
}
