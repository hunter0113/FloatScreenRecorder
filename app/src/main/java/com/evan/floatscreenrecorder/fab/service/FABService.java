package com.evan.floatscreenrecorder.fab.service;


import static com.evan.floatscreenrecorder.common.constant.Constants.*;
import static com.evan.floatscreenrecorder.fab.manager.FABManager.getSideLength;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;


import com.evan.floatscreenrecorder.common.manager.CallbackManager;
import com.evan.floatscreenrecorder.EvanSDK;
import com.evan.floatscreenrecorder.R;
import com.evan.floatscreenrecorder.common.constant.Constants;
import com.evan.floatscreenrecorder.common.constant.RecordErrorType;
import com.evan.floatscreenrecorder.common.util.CircleBitmap;
import com.evan.floatscreenrecorder.common.util.ClickUtil;
import com.evan.floatscreenrecorder.common.util.DeviceUtils;
import com.evan.floatscreenrecorder.common.util.LocalJsonUtils;
import com.evan.floatscreenrecorder.common.util.ToastUtil;
import com.evan.floatscreenrecorder.fab.FABActionHandler;
import com.evan.floatscreenrecorder.fab.broadcastReceiver.FloatingButtonReceiver;
import com.evan.floatscreenrecorder.fab.manager.FABManager;
import com.evan.floatscreenrecorder.fab.manager.FABScreenManager;
import com.evan.floatscreenrecorder.fab.model.FloatingConfigurationModel;
import com.evan.floatscreenrecorder.provider.SDKProvider;
import com.evan.floatscreenrecorder.record.callback.RecordingStatusCallback;
import com.evan.floatscreenrecorder.record.callback.ShareVideoCallback;
import com.evan.floatscreenrecorder.record.manager.RecordingManager;

import java.util.ArrayList;

/**
 * Created by Evan on 2021/12/9.
 * <p>
 * Description：
 * 懸浮視窗參數設定與初始化, 按鈕點擊Callback事件
 */
public class FABService extends Service {


    /**
     * childImageViews
     */
    private ArrayList<ImageView> childImageViews;

    /**
     * Side Length
     */
    private static final int LARGE = 60;
    private static final int MEDIUM = 50;
    private static final int SMALL = 40;


    /**
     * Screen Invalid Margin
     */
    private static final int MARGIN = 20;


    /**
     * Broadcast Receiver
     */
    private FloatingButtonReceiver receiver;

    /**
     * Redisplay Show  當GameClient無傳入callback時使用，暫時隱藏FAB，等執行完分享或客服後重新顯示，以避免懸浮視窗出現在兩者上層。
     */
    private boolean redisplayShow = false;


    /**
     * FAB All ImageView
     */
    private ImageView optionImageView;
    private ImageView deleteImageView;
    private ImageView recordImageView;
    private ImageView shareImageView;
    private ImageView customerServiceImageView;


    /**
     * Assets ImageView
     */
    private Bitmap startRecordImageBmp;
    private Bitmap cancelRecordImageBmp;
    private Bitmap shareImageBmp;
    private Bitmap customerServiceImageBmp;
    private Bitmap optionImageBmp;
    private Bitmap deleteImageBmp;


    /**
     * TYPE
     */
    private static final int RECORD_TYPE = 0;
    private static final int SHARE_TYPE = 1;
    private static final int CUSTOMER_TYPE = 2;


    /**
     * Model
     */
    private FloatingConfigurationModel sm_configModel;


    @Override
    public void onCreate() {
        initReceiver();
        initParameter();
        super.onCreate();
    }


    //=============================================================
    /**
     * Init Receiver
     */
    //=============================================================
    private void initReceiver(){
        receiver = new FloatingButtonReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(FAB_CLOSE_ACTION);
        filter.addAction(FAB_HIDE_ACTION);
        filter.addAction(FAB_DISPLAY_ACTION);
        filter.addAction(FAB_RECORDING_STATUS_CALLBACK_ERROR_ACTION);
        filter.addAction(FAB_RECORDING_STATUS_CALLBACK_SUCCESS_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, filter, RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(receiver, filter);
        }
    }


    //=============================================================
    /**
     * Init Parameter
     */
    //=============================================================
    private void initParameter() {
        sm_configModel = FABManager.getConfigurationModel();
        childImageViews = new ArrayList<>();

        FABActionHandler fabActionHandler = new FABActionHandler();
        fabActionHandler.initFABLayoutParams(this);

        // set Spacing //
        FABManager.setSpacing(dpToPx(5, this));

        // set Screen Invalid Margin //
        FABManager.setScreenInvalidMargin(dpToPx(MARGIN, this));


        // =====Set According To The Parameter From Model===== //
        // set FAB Size //
        setFABSize();

        // set FAB Origin //
        if (!FABManager.getFromExternalInterface()) {
            setFABOrigin();
        } else {
            // 從背景回到前景，回到FAB最後的位置
            FABManager.getFloatingLayoutParams().x = FABManager.getLastLocationX();
            FABManager.getFloatingLayoutParams().y = FABManager.getLastLocationY();
        }

        // init ImageView //
        initOptionImageView();
        initDeleteImageView();
        initChildImageViews();
        initCustomizedImageViews();

        // set ImageView //
        fabActionHandler.setOptionImageView(optionImageView, this);
        fabActionHandler.setDeleteImageView(deleteImageView);
        fabActionHandler.setChildViewList(childImageViews);

        // init DeleteView XY //
        fabActionHandler.initOptionViewXY();
        fabActionHandler.initDeleteViewXY();
        fabActionHandler.initChildViewXY();

        // add ImageView to WindowManager //
        fabActionHandler.addDeleteView();
        fabActionHandler.addOptionView();
        fabActionHandler.addChildView();

        FABManager.setServiceAlive(true);
        FABManager.setFloatingShowing(true);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    //=============================================================
    /**
     * Set FAB Size
     */
    //=============================================================
    private static void recycleImageView(ImageView imageView) {
        if (imageView != null) {
            BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
            recycleBitmapDrawable(bd);
        }
    }


    private static void recycleBitmapDrawable(BitmapDrawable bitmapDrawable) {
        if (bitmapDrawable != null) {
            Bitmap bitmap = bitmapDrawable.getBitmap();
            recycleBitmap(bitmap);
        }
    }


    private static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    //=============================================================
    /**
     * Set FAB Size
     */
    //=============================================================
    private void setFABSize() {
        switch (sm_configModel.getSize()) {
            case LARGE:
                FABManager.setSideLength(dpToPx(LARGE, this));
                break;

            case MEDIUM:
                FABManager.setSideLength(dpToPx(MEDIUM, this));
                break;

            case SMALL:
                FABManager.setSideLength(dpToPx(SMALL, this));
                break;
        }
    }


    //=============================================================
    /**
     * Init Child ImageViews
     */
    //=============================================================
    private void initChildImageViews() {
        switch (sm_configModel.getFloatingOptionsType()) {
            case ALL_OPTIONS:
                startRecordImageBmp = LocalJsonUtils.readerLocalImage(this, sm_configModel.getStartRecordVideoImage());
                cancelRecordImageBmp = LocalJsonUtils.readerLocalImage(this, sm_configModel.getCancelRecordVideoImage());
                shareImageBmp = LocalJsonUtils.readerLocalImage(this, sm_configModel.getShareImage());
                customerServiceImageBmp = LocalJsonUtils.readerLocalImage(this, sm_configModel.getCustomerServiceImage());

                // init ImageView //
                recordImageView = initImageView();
                shareImageView = initImageView();
                customerServiceImageView = initImageView();

                setCircleImage(RECORD_TYPE);
                setCircleImage(SHARE_TYPE);
                setCircleImage(CUSTOMER_TYPE);

                setListener(RECORD_TYPE);
                setListener(SHARE_TYPE);
                setListener(CUSTOMER_TYPE);

                childImageViews.add(recordImageView);
                childImageViews.add(shareImageView);
                childImageViews.add(customerServiceImageView);

                break;

            case RECORD_VIDEO:
                startRecordImageBmp = LocalJsonUtils.readerLocalImage(this, sm_configModel.getStartRecordVideoImage());
                cancelRecordImageBmp = LocalJsonUtils.readerLocalImage(this, sm_configModel.getCancelRecordVideoImage());
                shareImageBmp = LocalJsonUtils.readerLocalImage(this, sm_configModel.getShareImage());

                recordImageView = initImageView();
                shareImageView = initImageView();

                setCircleImage(RECORD_TYPE);
                setCircleImage(SHARE_TYPE);

                setListener(RECORD_TYPE);
                setListener(SHARE_TYPE);

                childImageViews.add(recordImageView);
                childImageViews.add(shareImageView);

                break;

            case CUSTOMER_SERVICE:
                customerServiceImageBmp = LocalJsonUtils.readerLocalImage(this, sm_configModel.getCustomerServiceImage());

                customerServiceImageView = initImageView();

                setCircleImage(CUSTOMER_TYPE);

                setListener(CUSTOMER_TYPE);

                childImageViews.add(customerServiceImageView);
                break;
        }
    }


    //=============================================================
    /**
     * set FAB Origin
     */
    //=============================================================
    private void setFABOrigin() {
        switch (sm_configModel.getOrigin()) {
            case TOP_LEFT:
                FABManager.setUp(true);
                FABManager.getFloatingLayoutParams().x = dpToPx(MARGIN, this);
                FABManager.getFloatingLayoutParams().y = dpToPx(MARGIN, this);
                break;

            case TOP_RIGHT:
                FABManager.setUp(true);
                FABManager.getFloatingLayoutParams().x = FABScreenManager.getScreenWidth() - getSideLength() - dpToPx(MARGIN, this);
                FABManager.getFloatingLayoutParams().y = dpToPx(MARGIN, this);
                break;

            case BOTTOM_LEFT:
                FABManager.setUp(false);
                FABManager.getFloatingLayoutParams().x = dpToPx(MARGIN, this);
                FABManager.getFloatingLayoutParams().y = FABScreenManager.getScreenHeight() - getSideLength() - dpToPx(MARGIN, this);
                break;

            case BOTTOM_RIGHT:
                FABManager.setUp(false);
                FABManager.getFloatingLayoutParams().x = FABScreenManager.getScreenWidth() - getSideLength() - dpToPx(MARGIN, this);
                FABManager.getFloatingLayoutParams().y = FABScreenManager.getScreenHeight() - getSideLength() - dpToPx(MARGIN, this);
                break;
        }

        FABManager.setLastLocationX(FABManager.getFloatingLayoutParams().x);
        FABManager.setLastLocationY(FABManager.getFloatingLayoutParams().y);
    }


    //=============================================================
    /**
     * init Option Image View
     */
    //=============================================================
    private void initOptionImageView() {
        optionImageView = new ImageView(this);
        optionImageView.setBackground(getResources().getDrawable(R.drawable.fab_circle_imageview));

        optionImageBmp = LocalJsonUtils.readerLocalImage(this, sm_configModel.getOptionImage());
        circleImageHandle(optionImageView, optionImageBmp, DEFAULT_OPTION_IMAGE);

        optionImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }


    //=============================================================
    /**
     * init Delete Image View
     */
    //=============================================================
    private void initDeleteImageView() {
        deleteImageView = new ImageView(this);
        deleteImageView.setBackground(getResources().getDrawable(R.drawable.fab_circle_imageview));

        deleteImageBmp = LocalJsonUtils.readerLocalImage(this, sm_configModel.getDeleteImage());
        circleImageHandle(deleteImageView, deleteImageBmp, DEFAULT_DELETE_IMAGE);

        deleteImageView.setVisibility(View.INVISIBLE);
        deleteImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }


    //=============================================================
    /**
     * init Child Image Views
     */
    //=============================================================
    private void initCustomizedImageViews() {

        if (null == sm_configModel.getCustomizedChildButtonImageList() && sm_configModel.getCustomizedNumberOfChildButtons() <= 0) {
            return;
        }

        // 自定義按鈕的圖片少於按鈕數量 //
        if (sm_configModel.getCustomizedChildButtonImageList().size() < sm_configModel.getCustomizedNumberOfChildButtons() && !FABManager.getFromExternalInterface()) {
            Log.e("Evan", FAB_LOG_CHILD_IMAGE_MISSING_IMAGE);
        }

        // 設定自定義按鈕 //
        for (int i = 0; i < sm_configModel.getCustomizedNumberOfChildButtons(); i++) {
            ImageView childImageView = new ImageView(this);
            childImageView.setBackground(getResources().getDrawable(R.drawable.fab_circle_imageview));
            childImageView.setVisibility(View.INVISIBLE);
            childImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            if (i < sm_configModel.getCustomizedChildButtonImageList().size()) {
                Bitmap bitmap = LocalJsonUtils.readerLocalImage(this, sm_configModel.getCustomizedChildButtonImageList().get(i));
                circleImageHandle(childImageView, bitmap, DEFAULT_CUSTOM_IMAGE);
            } else {
                circleImageHandle(childImageView, null, DEFAULT_CUSTOM_IMAGE);
            }

            // 自定義按鈕從編號101開始 //
            int finalI = FAB_CUSTOMIZED_START_INDEX + i;
            childImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ClickUtil.isDoubleClick()) {
                        return;
                    }
                    CallbackManager.getNativeFloatingButtonCallback().onClick(finalI);
                }
            });

            childImageViews.add(childImageView);
        }
    }


    //=============================================================
    /**
     * circle Image Handle
     */
    //=============================================================
    private void circleImageHandle(ImageView imageView, Bitmap bitmap, String type) {
        if (null == imageView) {
            return;
        }

        if (null != bitmap) {
            setImage(imageView, CircleBitmap.getCircleBitMap(bitmap));
            return;
        }

        Bitmap bp;
        switch (type) {
            case DEFAULT_OPTION_IMAGE:
                bp = BitmapFactory.decodeResource(getResources(), R.mipmap.img);
                break;

            case DEFAULT_DELETE_IMAGE:
                bp = getBitmapFromVectorDrawable(this, R.drawable.ic_fab_delete);
                break;

            case DEFAULT_START_IMAGE:
                bp = getBitmapFromVectorDrawable(this, R.drawable.ic_fab_start_record);
                break;

            case DEFAULT_CANCEL_IMAGE:
                bp = getBitmapFromVectorDrawable(this, R.drawable.ic_fab_cancel_record);
                break;

            case DEFAULT_SHARE_IMAGE:
                bp = getBitmapFromVectorDrawable(this, R.drawable.ic_fab_share);
                break;

            case DEFAULT_CUSTOMER_SERVICE_IMAGE:
                bp = getBitmapFromVectorDrawable(this, R.drawable.ic_fab_customer_service);
                break;

            case DEFAULT_CUSTOM_IMAGE:
                bp = getBitmapFromVectorDrawable(this, R.drawable.ic_fab_default);
                break;

            default:
                bp = getBitmapFromVectorDrawable(this, R.drawable.ic_fab_default);
                break;
        }

        setImage(imageView, CircleBitmap.getCircleBitMap(bp));
    }


    //=============================================================
    /**
     * ImageView Set Image
     */
    //=============================================================
    private void setImage(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }


    //=============================================================
    /**
     * dp To px
     */
    //=============================================================
    private static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


    //=============================================================
    /**
     * Responsible For Receiving Closing
     */
    //=============================================================
    public void fabClose() {
        if (!FABManager.isServiceAlive()) {
            return;
        }

        if (FABManager.isFloatingShowing()) {
            removeAllView();
        }

        if (!SDKProvider.isFromBackground() && RecordingManager.getIsRecording()) {
            EvanSDK.setLoopRecordingStatus(FABService.this, false, null);
        }

        FABManager.setServiceAlive(false);
        FABManager.setFloatingShowing(false);
        stopSelf();
    }

    //=============================================================
    /**
     * Responsible For Displaying Actions
     */
    //=============================================================
    public void fabDisplay() {
        if (!FABManager.isServiceAlive()) {
            return;
        }

        if (FABManager.isFloatingShowing()) {
            return;
        }

        WindowManager.LayoutParams layoutParams = getLayoutParams();
        FABManager.getWindowManager().addView(optionImageView, FABManager.getFloatingLayoutParams());
        FABManager.getWindowManager().addView(deleteImageView, FABManager.getDeleteLayoutParams());

        for (int i = 0; i < childImageViews.size(); i++) {
            layoutParams.x = FABManager.getChildLayoutParams().x;
            layoutParams.y = FABManager.getChildLayoutParams().y;

            if (FABManager.isUp()) {
                layoutParams.y = layoutParams.y - (childImageViews.size() - 1 - i) * (getSideLength() + FABManager.getSpacing());
            } else {
                layoutParams.y = layoutParams.y + (childImageViews.size() - 1 - i) * (getSideLength() + FABManager.getSpacing());
            }
            FABManager.getWindowManager().addView(childImageViews.get(i), layoutParams);
        }

        FABManager.setFloatingShowing(true);
    }



    //=============================================================
    /**
     * Responsible For Receiving Hiding Actions
     */
    //=============================================================
    public void fabHide() {
        if (!FABManager.isServiceAlive()) {
            return;
        }

        if (!FABManager.isFloatingShowing()) {
            return;
        }

        removeAllView();
        FABManager.setFloatingShowing(false);
    }



    //=============================================================
    /**
     * Responsible For Recording Success
     */
    //=============================================================
    public void recordingStatusSuccess(){
        // Service已經銷毀則不往下執行。 //
        if (null == recordImageView) {
            return;
        }

        if (!RecordingManager.getIsRecording()) {
            // 牽涉到換圖，需在UI Thread
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    circleImageHandle(recordImageView, startRecordImageBmp, DEFAULT_START_IMAGE);

                    if (null != sm_configModel.getRecordingStatusCallback()) {
                        sm_configModel.getRecordingStatusCallback().onSuccess(false);
                        return;
                    }

                    showToast("取消錄影");
                }
            });
            return;
        }

        circleImageHandle(recordImageView, cancelRecordImageBmp, DEFAULT_CANCEL_IMAGE);

        if (null != sm_configModel.getRecordingStatusCallback()) {
            sm_configModel.getRecordingStatusCallback().onSuccess(true);
            return;
        }

        showToast("開始錄影");
    }


    //=============================================================
    /**
     * Responsible For Recording Error
     */
    //=============================================================
    public void recordingStatusError(Intent intent){
        // Service已經銷毀則不往下執行。 //
        if (null == recordImageView) {
            return;
        }

        String recordMsg = intent.getStringExtra("recordMsg");

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                circleImageHandle(recordImageView, startRecordImageBmp, DEFAULT_START_IMAGE);
            }
        });

        if (null != sm_configModel.getRecordingStatusCallback()) {
            sm_configModel.getRecordingStatusCallback().onError(recordMsg);
            return;
        }

        switch (recordMsg) {
            case RECORD_NOTIFICATION_CLOSE_MSG:
                showToast("取消錄影");
                return;

            case RECORD_USER_REFUSE_MSG:
                return;

            case RECORD_DEVICE_NOT_SUPPORT_MSG:
                showToast("裝置不支援");
                return;

            case RECORD_NO_STORAGE_MSG:
                showToast("裝置空間不足");
                return;

            case RECORD_VERSION_NOT_SUPPORT_MSG:
                showToast("Android 版本過低");
                return;

            default:
                showToast(recordMsg);
        }
    }


    //=============================================================
    /**
     * Init ImageView
     */
    //=============================================================
    private ImageView initImageView() {
        ImageView imageView = new ImageView(this);
        imageView.setBackground(getResources().getDrawable(R.drawable.fab_circle_imageview));
        imageView.setVisibility(View.INVISIBLE);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }


    //=============================================================
    /**
     * Set Circle Image
     */
    //=============================================================
    private void setCircleImage(int type) {
        switch (type) {
            case RECORD_TYPE:
                if (!RecordingManager.getIsRecording()) {
                    circleImageHandle(recordImageView, startRecordImageBmp, DEFAULT_START_IMAGE);
                } else {
                    circleImageHandle(recordImageView, cancelRecordImageBmp, DEFAULT_CANCEL_IMAGE);
                }
                break;

            case SHARE_TYPE:
                circleImageHandle(shareImageView, shareImageBmp, DEFAULT_SHARE_IMAGE);
                break;

            case CUSTOMER_TYPE:
                circleImageHandle(customerServiceImageView, customerServiceImageBmp, DEFAULT_CUSTOMER_SERVICE_IMAGE);
                break;
        }
    }


    //=============================================================
    /**
     * Get Bitmap From VectorDrawable
     */
    //=============================================================
    private Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        // 此 import androidx.core.content.ContextCompat; 資源引用與FB Library androidx.core 衝突故改以下寫法
        Drawable drawable;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            drawable = getResources().getDrawable(drawableId, context.getTheme());
        else
            drawable = getResources().getDrawable(drawableId);

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    //=============================================================
    /**
     * Set Listener
     */
    //=============================================================
    private void setListener(int type) {
        switch (type) {
            case RECORD_TYPE:
                recordImageView.setOnClickListener(sm_startListener);
                break;

            case SHARE_TYPE:
                shareImageView.setOnClickListener(sm_shareListener);
                break;
        }
    }




    //=============================================================
    /**
     * Get LayoutParams For FAB_DISPLAY_ACTION
     */
    //=============================================================
    private WindowManager.LayoutParams getLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        layoutParams.format = PixelFormat.TRANSPARENT;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

        layoutParams.width = getSideLength();
        layoutParams.height = getSideLength();

        return layoutParams;
    }


    //=============================================================
    /**
     * Remove All View
     */
    //=============================================================
    private void removeAllView() {
        for (ImageView imageView : childImageViews) {
            FABManager.getWindowManager().removeView(imageView);
        }
        FABManager.getWindowManager().removeView(optionImageView);
        FABManager.getWindowManager().removeView(deleteImageView);
    }


    //=============================================================
    /**
     * Start Listener
     */
    //=============================================================
    private final View.OnClickListener sm_startListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (FABActionHandler.isAnimation()) {
                return;
            }

            if (ClickUtil.isDoubleClick()) {
                return;
            }

            if (!RecordingManager.getIsRecording()) {
                EvanSDK.setLoopRecordingStatus(FABService.this, true, recordingStatusCallback);
                return;
            }

            EvanSDK.setLoopRecordingStatus(FABService.this, false, recordingStatusCallback);
        }
    };


    //=============================================================
    /**
     * Share Listener
     */
    //=============================================================
    private final View.OnClickListener sm_shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (FABActionHandler.isAnimation()) {
                return;
            }

            if (ClickUtil.isDoubleClick()) {
                return;
            }

            if (null != sm_configModel.getShareVideoCallback()) {
                if (!RecordingManager.getIsRecording()) {
                    circleImageHandle(recordImageView, startRecordImageBmp, DEFAULT_START_IMAGE);
                    sm_configModel.getShareVideoCallback().onError(RECORD_NOT_TURNED_ON_MSG);
                    return;
                }

                circleImageHandle(recordImageView, startRecordImageBmp, DEFAULT_START_IMAGE);
                EvanSDK.shareRecordVideo(FABService.this, shareVideoCallback);
                return;
            }

            if (!RecordingManager.getIsRecording()) {
                showToast("尚未啟動錄影");
                return;
            }

            if (FABManager.isFloatingShowing()) {
                Intent startIntent = new Intent(FAB_HIDE_ACTION);
                startIntent.setPackage(DeviceUtils.getPackageName());
                FABService.this.sendBroadcast(startIntent);
                redisplayShow = true;
            }

            showToast("生成影片中");
            circleImageHandle(recordImageView, startRecordImageBmp, DEFAULT_START_IMAGE);
            EvanSDK.shareRecordVideo(FABService.this, shareVideoCallback);
        }
    };


    //=============================================================
    /**
     * Recording Status Callback
     */
    //=============================================================
    private final RecordingStatusCallback recordingStatusCallback = new RecordingStatusCallback() {
        @Override
        public void onSuccess(boolean status) {
            Intent startIntent = new Intent(FAB_RECORDING_STATUS_CALLBACK_SUCCESS_ACTION);
            startIntent.setPackage(DeviceUtils.getPackageName());
            getApplication().sendBroadcast(startIntent);
        }

        @Override
        public void onError(String recordMsg) {

            Intent startIntent = new Intent(FAB_RECORDING_STATUS_CALLBACK_ERROR_ACTION);
            startIntent.setPackage(DeviceUtils.getPackageName());
            startIntent.putExtra("recordMsg",recordMsg);
            getApplication().sendBroadcast(startIntent);
        }
    };


    //=============================================================
    /**
     * ShareVideo Callback
     */
    //=============================================================
    private final ShareVideoCallback shareVideoCallback = new ShareVideoCallback() {
        @Override
        public void onFinish() {
            if (null != sm_configModel.getShareVideoCallback()) {
                sm_configModel.getShareVideoCallback().onFinish();
                return;
            }

            if (!redisplayShow) {
                return;
            }

            Intent startIntent = new Intent(FAB_DISPLAY_ACTION);
            startIntent.setPackage(DeviceUtils.getPackageName());
            FABService.this.sendBroadcast(startIntent);
            redisplayShow = false;
        }

        @Override
        public void onError(String shareMsg) {

            if (null != sm_configModel.getShareVideoCallback()) {
                sm_configModel.getShareVideoCallback().onError(shareMsg);
                return;
            }

            if (!redisplayShow) {
                return;
            }

            Intent startIntent = new Intent(FAB_DISPLAY_ACTION);
            startIntent.setPackage(DeviceUtils.getPackageName());
            FABService.this.sendBroadcast(startIntent);
            redisplayShow = false;


            if (shareMsg.equals(Constants.RECORD_ERROR_MSG + "(" + RecordErrorType.DURATION_SHORT.getType() + ").")) {
                showToast("錄製時間過短");
                return;
            }

            if (shareMsg.equals(RECORD_NOT_TURNED_ON_MSG)) {
                showToast("尚未開啟錄影");
                return;
            }

            showToast("影片生成失敗");
        }
    };


    //=============================================================
    /**
     * show Toast
     */
    //=============================================================
    private void showToast(String msg) {
        ToastUtil.show(msg);
    }


    @Override
    public void onDestroy() {
        if (FABManager.isFloatingShowing()) {
            removeAllView();
            FABManager.setServiceAlive(false);
        }

        recycleBitmap(startRecordImageBmp);
        recycleBitmap(cancelRecordImageBmp);
        recycleBitmap(shareImageBmp);
        recycleBitmap(customerServiceImageBmp);
        recycleBitmap(optionImageBmp);
        recycleBitmap(deleteImageBmp);

        recycleImageView(optionImageView);
        recycleImageView(deleteImageView);
        recycleImageView(recordImageView);
        recycleImageView(shareImageView);
        recycleImageView(customerServiceImageView);

        optionImageView = null;
        deleteImageView = null;
        recordImageView = null;
        shareImageView = null;
        customerServiceImageView = null;

        unregisterReceiver(receiver);
        super.onDestroy();
    }

}
