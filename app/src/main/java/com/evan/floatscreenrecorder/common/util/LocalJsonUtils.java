package com.evan.floatscreenrecorder.common.util;

import static com.evan.floatscreenrecorder.common.util.StrUtil.EMPTY;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;




public class LocalJsonUtils {


    private static String m_sJsonData = EMPTY;



    //=============================================================
    /**
     * Get Local Image(Assets Package) File Data (for floatingButton)
     * <p>
     * @param context  Context
     * @param ImageName Local Image File Name
     * @return String
     */
    //=============================================================
    private static Bitmap getLocalImage(Context context, String ImageName) {
        // Get Assets Manager //
        AssetManager assetManager = context.getAssets();

        InputStream inputStream = null;

        // Use Read Image Data //
        try {
            inputStream = assetManager.open(ImageName);
        } catch (IOException e) {
        }

        return BitmapFactory.decodeStream(inputStream);
    }


    //=============================================================
    /**
     * Get Language Json(Assets Package) File Data
     * <p>
     * @param context  Context
     * @param fileName Language Json File Name
     * @return String
     */
    //=============================================================
    private static String getLanguageJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            int data = bf.read();
            int lineCount = 0;
            while (data != -1 && lineCount < 1000) {
                char c = (char) data;
                stringBuilder.append(c);
                if (c == '\n') {
                    lineCount++;
                }
                data = bf.read();
            }
            bf.close();
        } catch (IOException e) {
        }
        return stringBuilder.toString();
    }



    //=============================================================
    /**
     * Is Image Exists
     * <p>
     * @param context  Context
     * @param fileName Local Image File Name
     * @return boolean
     */
    //=============================================================
    private static boolean isImageExists(Context context, String fileName) {
        if (null == fileName) {
            return false;
        }

        AssetManager assetManager = context.getAssets();
        try {
            String[] names = assetManager.list(EMPTY);
            for (String name : names) {
                if (name.equals(fileName.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    //=============================================================
    /**
     * Reader Local Image(Assets Package) File Data
     * <p>
     * @param context   Context
     * @param fileName  Image File
     */
    //=============================================================
    public static Bitmap readerLocalImage(Context context, String fileName) {
        if (null == fileName) {
            return null;
        }

        // Get Local Image(Assets Package) File Data //
        if (!isImageExists(context, fileName)) {
            return null;
        }

        return LocalJsonUtils.getLocalImage(context, fileName);
    }
}
