package com.evan.floatscreenrecorder.fab.model;

import static com.evan.floatscreenrecorder.common.constant.FloatingOptionsType.ALL_OPTIONS;
import static com.evan.floatscreenrecorder.common.constant.FloatingOriginType.BOTTOM_RIGHT;
import static com.evan.floatscreenrecorder.common.constant.FloatingSizeType.MEDIUM;
import static com.evan.floatscreenrecorder.common.util.StrUtil.EMPTY;

import com.evan.floatscreenrecorder.common.constant.FloatingOriginType;
import com.evan.floatscreenrecorder.common.constant.FloatingSizeType;
import com.evan.floatscreenrecorder.record.callback.RecordingStatusCallback;
import com.evan.floatscreenrecorder.common.constant.FloatingOptionsType;

import java.util.ArrayList;


/**
 * Created by Evan on 2021/12/6.
 * <p>
 * Description：
 */

public class FloatingConfigurationModel {


    /**
     * {
     *     "FloatingOptionsType": ALL_OPTIONS,
     *     "CustomizedNumberOfChildButtons": 2,
     *     "IsAutomaticWelt": true,
     *     "Origin": BOTTOM_RIGHT,
     *     "Size": MEDIUM,
     *     "NonUseAlpha": 0.5,
     *     "StartRecordVideoImage": "<Image Path>",
     *     "CancelRecordVideoImage": "<Image Path>",
     *     "ShareImage": "<Image Path>",
     *     "CustomerServiceImage": "<Image Path>",
     *     "OptionImage": "<Image Path>",
     *     "DeleteImage": "<Image Path>",
     *     "CloseAlert": "close",
     *     "CustomizedChildButtonImageList": [
     *               { "<Image Path>" },
     *               { "<Image Path>" }
     *           ]
     *
     *     "RecordingStatusCallback": recordingStatusCallback,
     *     "CustomerServiceCallback": customerServiceCallback
     * }
     */

    private FloatingOptionsType FloatingOptionsType = ALL_OPTIONS;
    private int CustomizedNumberOfChildButtons = 0;
    private boolean IsAutomaticWelt = false;
    private FloatingOriginType Origin = BOTTOM_RIGHT;
    private FloatingSizeType Size = MEDIUM;
    private double NonUseAlpha = 0.5;
    private String StartRecordVideoImage;
    private String CancelRecordVideoImage;
    private String ShareImage;
    private String CustomerServiceImage;
    private String OptionImage;
    private String DeleteImage;
    private ArrayList<String> CustomizedChildButtonImageList;
    private String CloseAlert = EMPTY;

    private RecordingStatusCallback RecordingStatusCallback;
    private com.evan.floatscreenrecorder.record.callback.ShareVideoCallback ShareVideoCallback;


    public com.evan.floatscreenrecorder.common.constant.FloatingOptionsType getFloatingOptionsType() {
        return FloatingOptionsType;
    }

    public void setFloatingOptionsType(com.evan.floatscreenrecorder.common.constant.FloatingOptionsType floatingOptionsType) {
        this.FloatingOptionsType = floatingOptionsType;
    }

    public String getStartRecordVideoImage() {
        return StartRecordVideoImage;
    }

    public void setStartRecordVideoImage(String startRecordVideoImage) {
        this.StartRecordVideoImage = startRecordVideoImage;
    }

    public String getCancelRecordVideoImage() {
        return CancelRecordVideoImage;
    }

    public void setCancelRecordVideoImage(String cancelRecordVideoImage) {
        this.CancelRecordVideoImage = cancelRecordVideoImage;
    }

    public String getShareImage() {
        return ShareImage;
    }

    public void setShareImage(String shareImage) {
        this.ShareImage = shareImage;
    }

    public String getCustomerServiceImage() {
        return CustomerServiceImage;
    }

    public void setCustomerServiceImage(String customerServiceImage) {
        this.CustomerServiceImage = customerServiceImage;
    }

    public int getCustomizedNumberOfChildButtons() {
        if (CustomizedNumberOfChildButtons < 0) {
            return 0;
        }
        return CustomizedNumberOfChildButtons;
    }

    public void setCustomizedNumberOfChildButtons(int customizedNumberOfChildButtons) {
        this.CustomizedNumberOfChildButtons = customizedNumberOfChildButtons;
    }

    public boolean getAutomaticWelt() {
        return IsAutomaticWelt;
    }

    public void setAutomaticWelt(boolean automaticWelt) {
        IsAutomaticWelt = automaticWelt;
    }

    public FloatingOriginType getOrigin() {
        return Origin;
    }

    public void setOrigin(FloatingOriginType origin) {
        this.Origin = origin;
    }

    public FloatingSizeType getSize() {
        return Size;
    }

    public void setSize(FloatingSizeType size) {
        this.Size = size;
    }

    public double getNonUseAlpha() {
        if (0 <= NonUseAlpha && NonUseAlpha <= 1) {
            return NonUseAlpha;
        }
        return 0.5;
    }

    public void setNonUseAlpha(double nonUseAlpha) {
        this.NonUseAlpha = nonUseAlpha;
    }

    public String getDeleteImage() {
        return DeleteImage;
    }

    public void setDeleteImage(String deleteImage) {
        this.DeleteImage = deleteImage;
    }

    public String getOptionImage() {
        return OptionImage;
    }

    public void setOptionImage(String optionImage) {
        this.OptionImage = optionImage;
    }

    public ArrayList<String> getCustomizedChildButtonImageList() {
        if (null == CustomizedChildButtonImageList){
            return new ArrayList<>();
        }
        return CustomizedChildButtonImageList;
    }

    public void setCustomizedChildButtonImageList(ArrayList<String> customizedChildButtonImageList) {
        this.CustomizedChildButtonImageList = customizedChildButtonImageList;
    }

    public String getCloseAlert() {
        return CloseAlert;
    }

    public void setCloseAlert(String closeAlert) {
        CloseAlert = closeAlert;
    }
    public RecordingStatusCallback getRecordingStatusCallback() {
        return RecordingStatusCallback;
    }

    public void setRecordingStatusCallback(RecordingStatusCallback recordingStatusCallback) {
        this.RecordingStatusCallback = recordingStatusCallback;
    }

    public com.evan.floatscreenrecorder.record.callback.ShareVideoCallback getShareVideoCallback() {
        return ShareVideoCallback;
    }

    public void setShareVideoCallback(com.evan.floatscreenrecorder.record.callback.ShareVideoCallback shareVideoCallback) {
        this.ShareVideoCallback = shareVideoCallback;
    }
}
