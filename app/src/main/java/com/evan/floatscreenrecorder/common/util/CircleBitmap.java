package com.evan.floatscreenrecorder.common.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

/**
 * Created by Evan on 2021/12/22.
 * <p>
 * Description：
 */
public class CircleBitmap {


    //=============================================================
    /**
     * Convert BitMap To Circle
     */
    //=============================================================
    public static Bitmap getCircleBitMap(Bitmap bitmap) {
        Bitmap outputBp;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            outputBp = circleBitmapByShader(bitmap, bitmap.getHeight(), bitmap.getHeight() / 2);
            return outputBp;
        }

        outputBp = circleBitmapByShader(bitmap, bitmap.getWidth(), bitmap.getWidth() / 2);
        return outputBp;
    }


    //=============================================================
    /**
     * Convert BitMap To Circle Bitmap By Shader
     */
    //=============================================================
    public static Bitmap circleBitmapByShader(Bitmap bitmap, int edgeWidth, int radius) {
        float btWidth = bitmap.getWidth();
        float btHeight = bitmap.getHeight();

        float btWidthCutSite = 0; // 水平方向開始裁剪的位置 //
        float btHeightCutSite = 0; // 豎直方向開始裁剪的位置 //
        float squareWidth; // 裁剪成正方形圖片的邊長,未拉伸縮放 //

        if (btWidth > btHeight) { // 如果矩形寬度大於高度 //
            btWidthCutSite = (btWidth - btHeight) / 2f;
            squareWidth = btHeight;

        } else { // 如果矩形寬度不大於高度 //
            btHeightCutSite = (btHeight - btWidth) / 2f;
            squareWidth = btWidth;
        }

        // 設定拉伸縮放比 //
        float scale = edgeWidth * 1.0f / squareWidth;
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);

        // 將矩形圖片裁剪成正方形並拉伸縮放到控制元件大小 //
        Bitmap squareBt = Bitmap.createBitmap(bitmap, (int) btWidthCutSite, (int) btHeightCutSite, (int) squareWidth, (int) squareWidth, matrix, true);

        // 初始化繪製紋理圖 //
        BitmapShader bitmapShader = new BitmapShader(squareBt, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        // 初始化目標bitmap //
        Bitmap targetBitmap = Bitmap.createBitmap(edgeWidth, edgeWidth, Bitmap.Config.ARGB_8888);

        // 初始化目標畫布 //
        Canvas targetCanvas = new Canvas(targetBitmap);

        // 初始化畫筆  //
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);

        // 利用畫筆繪製圓形圖 //
        targetCanvas.drawRoundRect(new RectF(0, 0, edgeWidth, edgeWidth), radius, radius, paint);
        return targetBitmap;
    }
}
