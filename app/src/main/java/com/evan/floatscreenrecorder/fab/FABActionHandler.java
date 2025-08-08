package com.evan.floatscreenrecorder.fab;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import static com.evan.floatscreenrecorder.common.constant.Constants.FAB_CLOSE_ACTION;
import static com.evan.floatscreenrecorder.common.constant.Constants.FAB_REMOVE_MESSAGE;
import static com.evan.floatscreenrecorder.fab.manager.FABManager.FOLDING;
import static com.evan.floatscreenrecorder.fab.manager.FABManager.UNFOLDING;
import static com.evan.floatscreenrecorder.fab.manager.FABManager.getChildLayoutParams;
import static com.evan.floatscreenrecorder.fab.manager.FABManager.getDeleteLayoutParams;
import static com.evan.floatscreenrecorder.fab.manager.FABManager.getFloatingLayoutParams;
import static com.evan.floatscreenrecorder.fab.manager.FABManager.getScreenInvalidMargin;
import static com.evan.floatscreenrecorder.fab.manager.FABManager.getSideLength;
import static com.evan.floatscreenrecorder.fab.manager.FABManager.getSpacing;
import static com.evan.floatscreenrecorder.fab.manager.FABManager.getWindowManager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.evan.floatscreenrecorder.common.manager.CallbackManager;
import com.evan.floatscreenrecorder.common.util.DeviceUtils;
import com.evan.floatscreenrecorder.fab.manager.FABManager;
import com.evan.floatscreenrecorder.fab.manager.FABScreenManager;

import java.util.ArrayList;


/**
 * Created by Evan on 2021/11/9.
 * <p>
 * Description：
 * 懸浮按鈕
 */

public class FABActionHandler {

    /**
     * 點下時的位置，判斷離開時動作為滑動還是點擊
     */
    private int m_nTouchStartX;
    private int m_nTouchStartY;

    /**
     * 當前是否顯示垃圾桶(是否在拖拽過程中)
     */
    private boolean m_isShowDelete = false;

    /**
     * 是否正在執行動畫
     */
    private static boolean m_isAnimation = false;

    public static boolean isAnimation() {
        return m_isAnimation;
    }

    /**
     * 動畫時長
     */
    private final int m_nDuration = 500;

    /**
     * ImageView
     */
    private ImageView optionImageView;  // 顯示的主要button
    private ImageView deleteImageView;  // 顯示的delete Image
    private ArrayList<ImageView> childImageViewList;  // 顯示的child Image

    private int beforeUnFoldingY; // 紀錄展開前的Y，收回後要退到此位置


    //=============================================================
    /**
     * init FAB LayoutParams
     */
    //=============================================================
    public void initFABLayoutParams(Context context) {

        // Set to FABManager //
        FABManager.setFloatingLayoutParams(new WindowManager.LayoutParams());
        FABManager.setDeleteLayoutParams(new WindowManager.LayoutParams());
        FABManager.setChildLayoutParams(new WindowManager.LayoutParams());

        FABManager.setWindowManager((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE));

        // Init LayoutParams //
        initLayoutParams(getFloatingLayoutParams());
        initLayoutParams(getDeleteLayoutParams());
        initLayoutParams(getChildLayoutParams());

        // Status FOLDING //
        FABManager.setFloatingStatus(FOLDING);
    }


    //=============================================================
    /**
     * init LayoutParams
     */
    //=============================================================
    private void initLayoutParams(WindowManager.LayoutParams layoutParams) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        layoutParams.format = PixelFormat.TRANSPARENT;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        // 控制是否可在畫面外 WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
    }


    //=============================================================
    /**
     * 設定 OptionView
     */
    //=============================================================
    public void initOptionViewXY() {
        getFloatingLayoutParams().width = getSideLength();
        getFloatingLayoutParams().height = getSideLength();
    }


    //=============================================================
    /**
     * 設定 deleteView 的位置
     */
    //=============================================================
    public void initDeleteViewXY() {
        getDeleteLayoutParams().width = getSideLength();
        getDeleteLayoutParams().height = getSideLength();

        // 固定中心為螢幕高*0.8，寬為中心點, deleteCenterPointX、deleteCenterPointY //
        getDeleteLayoutParams().x = FABScreenManager.getScreenWidth() / 2 - getSideLength() / 2;
        getDeleteLayoutParams().y = (int) (FABScreenManager.getScreenHeight() * 0.8) - getSideLength() / 2;
    }


    //=============================================================
    /**
     * 設定 子View 的位置
     */
    //=============================================================
    public void initChildViewXY() {
        getChildLayoutParams().width = getSideLength();
        getChildLayoutParams().height = getSideLength();
    }


    //=============================================================
    /**
     * Add FAB
     */
    //=============================================================
    public void addOptionView() {
        getWindowManager().addView(optionImageView, getFloatingLayoutParams());
    }


    //=============================================================
    /**
     * Add Delete View
     */
    //=============================================================
    public void addDeleteView() {
        getWindowManager().addView(deleteImageView, getDeleteLayoutParams());
    }


    //=============================================================
    /**
     * Add Delete View
     */
    //=============================================================
    public void addChildView() {
        if (childImageViewList.size() < 1) {
            return;
        }

        for (ImageView childImage : childImageViewList) {
            getWindowManager().addView(childImage, getChildLayoutParams());
        }
    }


    //=============================================================
    /**
     * set Option Listener And Add View
     */
    //=============================================================
    public void setOptionImageView(ImageView view, Context context) {
        optionImageView = view;
        setOptionListener(optionImageView, context);
        optionImageView.setAlpha((float) FABManager.getConfigurationModel().getNonUseAlpha());
    }


    //=============================================================
    /**
     * set Delete ImageView
     */
    //=============================================================
    public void setDeleteImageView(ImageView view) {
        deleteImageView = view;
    }


    //=============================================================
    /**
     * set Child ImageView List
     */
    //=============================================================
    public void setChildViewList(ArrayList<ImageView> list) {
        childImageViewList = list;
    }


    //=============================================================
    /**
     * 設定主按鈕的點選事件
     */
    //=============================================================
    private void setOptionListener(final View view, Context context) {
        view.setOnTouchListener(new FloatingOnTouchListener(context));
    }


    //=============================================================

    /**
     * Floating OnTouch Listener
     */
    //=============================================================
    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x, y, movedX, movedY;
        private final Context onTouchLContext;
        private final WindowManager.LayoutParams params = getFloatingLayoutParams();

        FloatingOnTouchListener(Context context) {
            onTouchLContext = context;
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    if (m_isAnimation) {
                        break;
                    }

                    optionImageView.setAlpha(1f);

                    x = (int) event.getRawX();
                    y = (int) event.getRawY();

                    m_nTouchStartX = (int) event.getRawX();
                    m_nTouchStartY = (int) event.getRawY();

                    break;


                case MotionEvent.ACTION_MOVE:
                    if (m_isAnimation) {
                        break;
                    }

                    if (FABManager.getFloatingStatus() != FOLDING) {
                        break;
                    }

                    if (outOfBorderSpacing()) {
                        break;
                    }

                    movedX = (int) event.getRawX() - x;
                    movedY = (int) event.getRawY() - y;
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();

                    params.x += movedX;
                    params.y += movedY;
                    getWindowManager().updateViewLayout(optionImageView, params);


                    FABManager.setUp(params.y < (FABScreenManager.getScreenHeight() / 2 - getSideLength() / 2));

                    if (!m_isShowDelete && (Math.abs(x - m_nTouchStartX) > 5 || Math.abs(y - m_nTouchStartY) > 5)) {
                        deleteImageView.setVisibility(VISIBLE);
                        m_isShowDelete = true;
                    }

                    break;

                case MotionEvent.ACTION_UP:
                    deleteImageView.setVisibility(INVISIBLE);
                    m_isShowDelete = false;

                    if (m_isAnimation) {
                        break;
                    }

                    // For Provider //
                    FABManager.setLastLocationX(params.x);
                    FABManager.setLastLocationY(params.y);


                    // 判斷是否刪除FAB //
                    if (isWhetherToDelete()) {
                        Intent startIntent = new Intent(FAB_CLOSE_ACTION);
                        startIntent.setPackage(DeviceUtils.getPackageName(onTouchLContext));
                        onTouchLContext.sendBroadcast(startIntent);
                        CallbackManager.getNativeFloatingButtonCallback().onClose(FAB_REMOVE_MESSAGE);
                        break;
                    }

                    // 判斷是否為點擊 //
                    if (isClick(x, y)) {
                        onClickHandle();
                        break;
                    }

                    // setAlpha //
                    optionImageView.setAlpha((float) FABManager.getConfigurationModel().getNonUseAlpha());


                    // 執行貼邊 //
                    if (!FABManager.getConfigurationModel().getAutomaticWelt()) {
                        break;
                    }
                    automaticWelt();


                    view.performClick();
                    break;

                default:
                    break;
            }
            return true;
        }
    }


    //=============================================================
    /**
     * 垂直展開
     */
    //=============================================================
    private void setVerticalTranslation(final ImageView childImage, int length) {
        m_isAnimation = true;
        beforeUnFoldingY = getChildLayoutParams().y;

        final ValueAnimator moveAni;

        if (FABManager.isUp()) {
            moveAni = ValueAnimator.ofInt(getChildLayoutParams().y, getChildLayoutParams().y + length);
        } else {
            moveAni = ValueAnimator.ofInt(getChildLayoutParams().y, getChildLayoutParams().y - length);
        }

        moveAni.setDuration(m_nDuration);
        moveAni.setInterpolator(new LinearInterpolator());

        moveAni.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                FABManager.setFloatingStatus(UNFOLDING);//展開狀態
                m_isAnimation = false;
                moveAni.removeAllUpdateListeners();
            }
        });

        moveAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                childImage.setAlpha(animation.getAnimatedFraction());

                getChildLayoutParams().y = (int) animation.getAnimatedValue();
                if (FABManager.isServiceAlive() && FABManager.isFloatingShowing()) {
                    getWindowManager().updateViewLayout(childImage, getChildLayoutParams());
                }
            }
        });

        moveAni.start();
    }


    //=============================================================
    /**
     * 垂直收回
     */
    //=============================================================
    private void setBackTranslation(final ImageView childImage, int length) {
        m_isAnimation = true;

        final ValueAnimator moveAni;

        if (FABManager.isUp()) {
            moveAni = ValueAnimator.ofInt(beforeUnFoldingY + length, beforeUnFoldingY);
        } else {
            moveAni = ValueAnimator.ofInt(beforeUnFoldingY - length, beforeUnFoldingY);
        }

        moveAni.setDuration(m_nDuration);
        moveAni.setInterpolator(new LinearInterpolator());
        moveAni.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                m_isAnimation = false;
                childImage.setVisibility(INVISIBLE);
                FABManager.setFloatingStatus(FOLDING);
                moveAni.removeAllUpdateListeners();
            }
        });


        moveAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                childImage.setAlpha(1 - animation.getAnimatedFraction());

                getChildLayoutParams().y = (int) animation.getAnimatedValue();
                if (!FABManager.isServiceAlive() || !FABManager.isFloatingShowing()) {
                    return;
                }
                getWindowManager().updateViewLayout(childImage, getChildLayoutParams());
            }
        });

        moveAni.start();
    }


    //=============================================================
    /**
     * is Whether To Delete
     */
    //=============================================================
    private boolean isWhetherToDelete() {
        return getDeleteLayoutParams().x - getSideLength() / 2 < (getFloatingLayoutParams().x)
                && (getFloatingLayoutParams().x) < getDeleteLayoutParams().x + getSideLength() / 2
                && (getFloatingLayoutParams().y) > getDeleteLayoutParams().y - getSideLength() / 2
                && (getFloatingLayoutParams().y) < getDeleteLayoutParams().y + getSideLength() / 2;

    }


    //=============================================================
    /**
     * is Click
     */
    //=============================================================
    private boolean isClick(int x, int y) {
        return Math.abs(x - m_nTouchStartX) <= 5 && Math.abs(y - m_nTouchStartY) <= 5;
    }


    //=============================================================
    /**
     * onClick Handle
     */
    //=============================================================
    private void onClickHandle() {
        int count = 1;
        if (FABManager.getFloatingStatus() == FOLDING) {// 摺疊狀態
            getChildLayoutParams().x = getFloatingLayoutParams().x;
            getChildLayoutParams().y = getFloatingLayoutParams().y;
            for (ImageView childImage : childImageViewList) {
                childImage.setVisibility(VISIBLE);
                setVerticalTranslation(childImage, count * (getSideLength() + getSpacing()));
                count++;
            }
            return;
        }

        for (ImageView childImage : childImageViewList) {
            setBackTranslation(childImage, count * (getSideLength() + getSpacing()));
            count++;
        }

        // setAlpha //
        optionImageView.setAlpha((float) FABManager.getConfigurationModel().getNonUseAlpha());
    }


    //=============================================================
    /**
     * automatic welt
     */
    //=============================================================
    private void automaticWelt() {
        m_isAnimation = true;

        ValueAnimator valueAnimator;
        if (getFloatingLayoutParams().x < FABScreenManager.getScreenWidth() / 2) {
            valueAnimator = ValueAnimator.ofInt(getFloatingLayoutParams().x, getScreenInvalidMargin());
            // For Provider
            FABManager.setLastLocationX(getScreenInvalidMargin());
        } else {
            valueAnimator = ValueAnimator.ofInt(getFloatingLayoutParams().x, FABScreenManager.getScreenWidth() - getSideLength() - getScreenInvalidMargin());
            // For Provider
            FABManager.setLastLocationX(FABScreenManager.getScreenWidth() - getSideLength() - getScreenInvalidMargin());
        }

        valueAnimator.setDuration(m_nDuration);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                m_isAnimation = false;
            }
        });

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getFloatingLayoutParams().x = (int) animation.getAnimatedValue();
                if (FABManager.isServiceAlive() && FABManager.isFloatingShowing()) {
                    getWindowManager().updateViewLayout(optionImageView, getFloatingLayoutParams());
                }
            }
        });
        valueAnimator.start();
    }


    //=============================================================
    /**
     * Out Of Border Spacing
     */
    //=============================================================
    private boolean outOfBorderSpacing() {
        if (getFloatingLayoutParams().y < getScreenInvalidMargin()) {
            getFloatingLayoutParams().y = getScreenInvalidMargin();
            return true;
        }
        if (getFloatingLayoutParams().x < getScreenInvalidMargin()) {
            getFloatingLayoutParams().x = getScreenInvalidMargin();
            return true;
        }
        if (getFloatingLayoutParams().y > FABScreenManager.getScreenHeight() - getSideLength() - getScreenInvalidMargin()) {
            getFloatingLayoutParams().y = FABScreenManager.getScreenHeight() - getSideLength() - getScreenInvalidMargin();
            return true;
        }
        if (getFloatingLayoutParams().x > FABScreenManager.getScreenWidth() - getSideLength() - getScreenInvalidMargin()) {
            getFloatingLayoutParams().x = FABScreenManager.getScreenWidth() - getSideLength() - getScreenInvalidMargin();
            return true;
        }

        return false;
    }
}