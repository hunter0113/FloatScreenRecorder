package com.evan.floatscreenrecorder.common.constant;

/**
 * Created by Evan on 2021/11/24.
 * <p>
 * Description：
 */
public enum PermissionType {


    CAMERA("CAMERA"),
    READ_CALENDAR("READ_CALENDAR"),
    WRITE_CALENDAR("WRITE_CALENDAR"),
    READ_CONTACTS("READ_CONTACTS"),
    WRITE_CONTACTS("WRITE_CONTACTS"),
    GET_ACCOUNTS("GET_ACCOUNTS"),
    ACCESS_FINE_LOCATION("ACCESS_FINE_LOCATION"),
    ACCESS_COARSE_LOCATION("ACCESS_COARSE_LOCATION"),
    RECORD_AUDIO("RECORD_AUDIO"),
    READ_PHONE_STATE("READ_PHONE_STATE"),
    CALL_PHONE("CALL_PHONE"),
    READ_CALL_LOG("READ_CALL_LOG"),
    WRITE_CALL_LOG("WRITE_CALL_LOG"),
    ADD_VOICE_MAIL("ADD_VOICE_MAIL"),
    USE_SIP("USE_SIP"),
    PROCESS_OUTGOING_CALLS("PROCESS_OUTGOING_CALLS"),
    BODY_SENSORS("BODY_SENSORS"),
    SEND_SMS("SEND_SMS"),
    RECEIVE_SMS("RECEIVE_SMS"),
    READ_SMS("READ_SMS"),
    RECEIVE_WAP_PUSH("RECEIVE_WAP_PUSH"),
    RECEIVE_MMS("RECEIVE_MMS"),
    READ_EXTERNAL_STORAGE("READ_EXTERNAL_STORAGE"),
    WRITE_EXTERNAL_STORAGE("WRITE_EXTERNAL_STORAGE"),
    READ_MEDIA_VIDEO("READ_MEDIA_VIDEO"),
    POST_NOTIFICATIONS("POST_NOTIFICATIONS")
    ;


    //=============================================================
    //=============================================================
    private String m_sPermissionType;


    //=============================================================
    /**
     * construct
     */
    //=============================================================
    PermissionType(String permissionType) {
        m_sPermissionType = permissionType;
    }


    //=============================================================
    /**
     * Public Method
     */
    //=============================================================
    public String getType(){
        return m_sPermissionType;
    }


    //=============================================================
    /**
     * String to PermissionType
     */
    //=============================================================
    public static PermissionType fromString(String text) {
        for (PermissionType permissionType : PermissionType.values()) {
            if (permissionType.m_sPermissionType.equalsIgnoreCase(text)) {
                return permissionType;
            }
        }
        return null;
    }
}
