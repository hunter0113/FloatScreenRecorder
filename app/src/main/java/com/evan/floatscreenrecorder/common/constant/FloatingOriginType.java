package com.evan.floatscreenrecorder.common.constant;

/**
 * Created by Evan on 2021/12/6.
 * <p>
 * Description：
 */
public enum FloatingOriginType {


    TOP_LEFT(1),
    BOTTOM_LEFT(2),
    TOP_RIGHT(3),
    BOTTOM_RIGHT(4),
    ;


    //=============================================================
    //=============================================================
    private int m_nFloatingOriginType;


    //=============================================================
    /**
     * construct
     */
    //=============================================================
    FloatingOriginType(int floatingOriginType){
        m_nFloatingOriginType = floatingOriginType;
    }


    //=============================================================
    /**
     * Public Method
     */
    //=============================================================
    public int getType(){
        return m_nFloatingOriginType;
    }


}
