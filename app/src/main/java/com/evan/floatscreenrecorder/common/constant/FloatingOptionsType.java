package com.evan.floatscreenrecorder.common.constant;

/**
 * Created by Evan on 2021/12/6.
 * <p>
 * Description：
 */

public enum FloatingOptionsType {


    ALL_OPTIONS(0),
    RECORD_VIDEO(1),
    ;


    //=============================================================
    //=============================================================
    private int m_nFloatingOptionsType;


    //=============================================================
    /**
     * construct
     */
    //=============================================================
    FloatingOptionsType(int floatingOptionsType){
        m_nFloatingOptionsType = floatingOptionsType;
    }


    //=============================================================
    /**
     * Public Method
     */
    //=============================================================
    public int getType(){
        return m_nFloatingOptionsType;
    }


}
