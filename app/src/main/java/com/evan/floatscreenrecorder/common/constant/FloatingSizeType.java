package com.evan.floatscreenrecorder.common.constant;

/**
 * Created by Evan on 2021/12/6.
 * <p>
 * Description：
 */
public enum FloatingSizeType {


    LARGE(1),
    MEDIUM(2),
    SMALL(3),
    ;


    //=============================================================
    //=============================================================
    private int m_nFloatingSizeType;


    //=============================================================
    /**
     * construct
     */
    //=============================================================
    FloatingSizeType(int floatingSizeType){
        m_nFloatingSizeType = floatingSizeType;
    }


    //=============================================================
    /**
     * Public Method
     */
    //=============================================================
    public int getType(){
        return m_nFloatingSizeType;
    }


}
