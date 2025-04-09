package com.evan.floatscreenrecorder.common.util;

import android.util.Log;


/**
 * Created by Bruce on 2020/7/23.
 * <p>
 * Description：
 * 1. common log:
 * 2. if log :
 * a. If() -> if true log it,
 * b. or() -> if false log it,
 * c. and() -> always log it.
 * <p>
 * 3. debug log -> only log on debug mode
 */

public class LogUtil {

    private static String GLOBAL_TAG = StrUtil.EMPTY;

    public static void setTag(String sTag) {
        GLOBAL_TAG = StrUtil.spaceOf(sTag);
    }


    //=============================================================
    /** Common Log */
    //=============================================================
    public static void v(Class<?> cls, String msg) {
        Log.v(GLOBAL_TAG + cls.getSimpleName() + StrUtil.SPACE, msg);
    }

    public static void d(Class<?> cls, String msg) {
        Log.d(GLOBAL_TAG + cls.getSimpleName() + StrUtil.SPACE, msg);
    }

    public static void e(Class<?> cls, String msg) {
        Log.e(GLOBAL_TAG + cls.getSimpleName() + StrUtil.SPACE, msg);
    }

    public static void i(Class<?> cls, String msg) {
        Log.i(GLOBAL_TAG + cls.getSimpleName() + StrUtil.SPACE, msg);
    }

    public static void w(Class<?> cls, String msg) {
        Log.w(GLOBAL_TAG + cls.getSimpleName() + StrUtil.SPACE, msg);
    }

    //=============================================================
    /** If Log
     * 1. if true Log it, else do nothing
     * 2. or(), if true log A, else log B
     * 3. and(), always log , can duplicated it . */
    //=============================================================
    public static class If {

        private static If sm_self = new If();
        private static String sm_sTypeTag = StrUtil.EMPTY;
        private static Class<?> sm_class = LogUtil.class;
        private static boolean sm_enable = true;

        private static final String VERBOSE_TAG = "v";
        private static final String DEBUG_TAG = "d";
        private static final String ERROR_TAG = "e";
        private static final String INFO_TAG = "i";
        private static final String WARNING_TAG = "w";


        //=============================================================
        // public method
        //=============================================================
        public static If v(boolean enable, Class<?> cls, String msg) {
            sm_class = cls;
            sm_sTypeTag = VERBOSE_TAG;

            if (enable)
                LogUtil.v(cls, msg);
            else
                sm_enable = false;
            return sm_self;
        }

        public static If d(boolean enable, Class<?> cls, String msg) {
            sm_class = cls;
            sm_sTypeTag = DEBUG_TAG;

            if (enable)
                LogUtil.d(cls, msg);
            else
                sm_enable = false;
            return sm_self;
        }

        public static If e(boolean enable, Class<?> cls, String msg) {
            sm_class = cls;
            sm_sTypeTag = ERROR_TAG;

            if (enable)
                LogUtil.e(cls, msg);
            else
                sm_enable = false;
            return sm_self;
        }

        public static If i(boolean enable, Class<?> cls, String msg) {
            sm_class = cls;
            sm_sTypeTag = INFO_TAG;

            if (enable)
                LogUtil.i(cls, msg);
            else
                sm_enable = false;
            return sm_self;
        }

        public static If w(boolean enable, Class<?> cls, String msg) {
            sm_class = cls;
            sm_sTypeTag = WARNING_TAG;

            if (enable)
                LogUtil.w(cls, msg);
            else
                sm_enable = false;
            return sm_self;
        }

        //=============================================================
        // 如果條件為 false，就 Log //
        //=============================================================
        public void or(String msg) { // 一但 or() 被執行， 而且條件為 false 時 and()裡面的所有的 code 都不執行
            if (sm_enable)
                return;

            and(msg);
            sm_enable = true;
            sm_sTypeTag = StrUtil.EMPTY;
        }

        //=============================================================
        // 無論條件 always Log, 並且可以重複呼叫。 //
        //=============================================================
        public If and(String msg) {
            switch (sm_sTypeTag) {
                case VERBOSE_TAG:
                    LogUtil.v(sm_class, msg);
                    break;
                case DEBUG_TAG:
                    LogUtil.d(sm_class, msg);
                    break;
                case ERROR_TAG:
                    LogUtil.e(sm_class, msg);
                    break;
                case INFO_TAG:
                    LogUtil.i(sm_class, msg);
                    break;
                case WARNING_TAG:
                    LogUtil.w(sm_class, msg);
                    break;
            }
            return sm_self;
        }
    }

    //=============================================================
    /** Debug
     * only log on debug mode*/
    //=============================================================
    public static class Debug {

        private static boolean isDebugMode = true;

        //=============================================================
        // public method //
        //=============================================================
        public static void v(Class<?> c, String msg) {
            If.v(isDebugMode, c, msg);
        }

        public static void d(Class<?> c, String msg) {
            If.d(isDebugMode, c, msg);
        }

        public static void e(Class<?> c, String msg) {
            If.e(isDebugMode, c, msg);
        }

        public static void i(Class<?> c, String msg) {
            If.i(isDebugMode, c, msg);
        }

        public static void w(Class<?> c, String msg) {
            If.w(isDebugMode, c, msg);
        }

        public static void Ifv(boolean enable, Class<?> c, String msg) {
            if (isDebugMode) If.v(enable, c, msg);
        }

        public static void Ifd(boolean enable, Class<?> c, String msg) {
            if (isDebugMode) If.d(enable, c, msg);
        }

        public static void Ife(boolean enable, Class<?> c, String msg) {
            if (isDebugMode) If.e(enable, c, msg);
        }

        public static void Ifi(boolean enable, Class<?> c, String msg) {
            if (isDebugMode) If.i(enable, c, msg);
        }

        public static void Ifw(boolean enable, Class<?> c, String msg) {
            if (isDebugMode) If.w(enable, c, msg);
        }
    }


    //=============================================================
    /** Check
     * to handle if else */
    //=============================================================

    public static class Check {

        private static Check sm_self = new Check();
        private static Class<?> sm_cls = null;
        private static Class<?> sm_defaultCls = LogUtil.class;
        private static String sm_sTypeTag = StrUtil.EMPTY;

        private static final String VERBOSE_TAG = "v";
        private static final String DEBUG_TAG = "d";
        private static final String ERROR_TAG = "e";
        private static final String INFO_TAG = "i";
        private static final String WARNING_TAG = "w";


        public static void setClass(Class<?> c) {
            sm_cls = c;
        }

        //=============================================================
        public static boolean v(boolean isTrue, String msg) {
            Log.v(GLOBAL_TAG + (sm_cls == null ? StrUtil.EMPTY : sm_cls.getSimpleName()), msg);
            return isTrue;
        }

        public static boolean d(boolean isTrue, String msg) {
            Log.d(GLOBAL_TAG + (sm_cls == null ? StrUtil.EMPTY : sm_cls.getSimpleName()), msg);
            return isTrue;
        }

        public static boolean e(boolean isTrue, String msg) {
            Log.e(GLOBAL_TAG + (sm_cls == null ? StrUtil.EMPTY : sm_cls.getSimpleName()), msg);
            return isTrue;
        }

        public static boolean i(boolean isTrue, String msg) {
            Log.i(GLOBAL_TAG + (sm_cls == null ? StrUtil.EMPTY : sm_cls.getSimpleName()), msg);
            return isTrue;
        }

        public static boolean w(boolean isTrue, String msg) {
            Log.w(GLOBAL_TAG + (sm_cls == null ? StrUtil.EMPTY : sm_cls.getSimpleName()), msg);
            return isTrue;
        }

        //=============================================================
        public static boolean v(boolean isTrue, Class<?> c, String msg) {
            If.v(isTrue, c, msg);
            return isTrue;
        }

        public static boolean d(boolean isTrue, Class<?> c, String msg) {
            If.d(isTrue, c, msg);
            return isTrue;
        }

        public static boolean e(boolean isTrue, Class<?> c, String msg) {
            If.e(isTrue, c, msg);
            return isTrue;
        }

        public static boolean i(boolean isTrue, Class<?> c, String msg) {
            If.i(isTrue, c, msg);
            return isTrue;
        }

        public static boolean w(boolean isTrue, Class<?> c, String msg) {
            If.w(isTrue, c, msg);
            return isTrue;
        }

        //=============================================================
        // 無論條件 always Log, 並且可以重複呼叫。 //
        //=============================================================
        public static Check and(String msg) {
            switch (sm_sTypeTag) {
                case VERBOSE_TAG:
                    LogUtil.v(sm_defaultCls, msg);
                    break;
                case DEBUG_TAG:
                    LogUtil.d(sm_defaultCls, msg);
                    break;
                case ERROR_TAG:
                    LogUtil.e(sm_defaultCls, msg);
                    break;
                case INFO_TAG:
                    LogUtil.i(sm_defaultCls, msg);
                    break;
                case WARNING_TAG:
                    LogUtil.w(sm_defaultCls, msg);
                    break;
            }
            return sm_self;
        }

    }
}
