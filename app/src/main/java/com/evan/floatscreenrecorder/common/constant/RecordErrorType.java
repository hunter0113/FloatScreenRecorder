package com.evan.floatscreenrecorder.common.constant;

/**
 * Created by Evan on 2021/11/23.
 * <p>
 * Description：
 */

public enum RecordErrorType {


    DURATION_SHORT("001"),
    MERGE_ERROR("002"),
    CUT_VIDEO_ERROR("003"),
    SWITCH_FILE_ERROR("004"),
    PUBLIC_STORAGE_ERROR("005"),
    ;


    //=============================================================
    //=============================================================
    private String m_sErrorType;


    //=============================================================
    /**
     * construct
     */
    //=============================================================
    RecordErrorType(String errorType) {
        m_sErrorType = errorType;
    }


    //=============================================================
    /**
     * Public Method
     */
    //=============================================================
    public String getType() {
        return m_sErrorType;
    }

}
