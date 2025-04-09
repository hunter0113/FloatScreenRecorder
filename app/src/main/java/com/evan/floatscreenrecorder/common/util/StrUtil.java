package com.evan.floatscreenrecorder.common.util;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Bruce on 2020/7/22.
 * <p>
 * Description：
 * String Util
 */

public class StrUtil {

    /**
     * Symbol
     */
    public static final String EMPTY = "";
    public static final String COMMA = ",";
    public static final String DOT = ".";
    public static final String SEMICOLON = ";";
    public static final String COLON = ":";
    public static final String DASH = "-";
    public static final String EQUAL = "=";
    public static final String QUOTE = "\"";
    public static final String SINGLE_QUOTE = "'";
    public static final String UNDER_LINE = "_";
    public static final String AT = "@";
    public static final String SLASH = "/";
    public static final String LEFT_BRACKET = "[";
    public static final String RIGHT_BRACKET = "]";
    public static final String LEFT_S_BRACKET = "(";
    public static final String RIGHT_S_BRACKET = ")";
    public static final String LEFT_CURLY_BRACKET = "{";
    public static final String RIGHT_CURLY_BRACKET = "}";
    public static final String BRACKETS = "[]";
    public static final String CURLY_BRACKETS = "{}";
    public static final String WRAP = "\n";
    public static final String QUESTION_MARK = "?";
    public static final String MINUTE_SYMBOL = "'";
    public static final String SECOND_SYMBOL = "''";
    public static final String PLUS = "+";
    public static final String HASH_TAG = "#";
    public static final String AND = "&";


    /**
     * With Space
     */
    public static final String SPACE = " ";
    public static final String DOUBLE_SPACE = "  ";
    public static final String TRIPLE_SPACE = "   ";
    public static final String TAB_SPACE = "    ";
    public static final String SPACE_DOT = " . ";
    public static final String SPACE_COMMA = " ,  ";
    public static final String SPACE_SEMICOLON = " ; ";
    public static final String SPACE_COLON = " : ";
    public static final String SPACE_DASH = " - ";
    public static final String SPACE_EQUAL = " = ";
    public static final String SPACE_QUOTE = " \" ";
    public static final String SPACE_SINGLE_QUOTE = "'";
    public static final String SPACE_UNDER_LINE = " _ ";
    public static final String SPACE_AT = " @ ";
    public static final String SPACE_SLASH = " / ";
    public static final String SPACE_LEFT_BRACKET = " [ ";
    public static final String SPACE_RIGHT_BRACKET = " ] ";
    public static final String SPACE_QUESTION_MARK = " ? ";
    public static final String SPACE_PLUS = " + ";
    public static final String LAST_SPACE_COMMA = ", ";


    /**
     * Time Format
     */
    public static final String HOUR_MINUTE_SECOND_FORMAT = "HH:mm:ss";
    public static final String HOUR_MINUTE_FORMAT = "HH:mm";
    public static final String MINUTE_SECOND_FORMAT = "mm:ss";


    /**
     * Character
     */
    public static final String UTF_8 = "UTF-8";
    public static final String STR_NULL = "null";
    public static final String ZERO = "0";
    public static final String DECIMAL_ZERO = "0.0";


    //=============================================================
    /**
     * Space Of
     * @param content   Content
     * @return String
     */
    //=============================================================
    public static String spaceOf(String content) {
        return SPACE + content + SPACE;
    }


    //=============================================================
    /**
     * Is Not Blank
     * @param content   Content
     * @return bool
     */
    //=============================================================
    public static boolean isNotBlank(String content) {
        return !(null == content || content.isEmpty() || content.equals(SPACE));
    }


    //=============================================================
    /**
     * The integer thousandth separated by commas
     * @param number    To Micrometer Number
     * @return String
     */
    //=============================================================
    public static String toMicrometer(double number) {
        DecimalFormat df = new DecimalFormat("#,###.##");
        return df.format(number);
    }


    //=============================================================
    /**
     * Copy Text
     * @param context   Context
     * @param text      Need Copy Text
     */
    //=============================================================
    public static void copyText(Context context, String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", text);
        clipboardManager.setPrimaryClip(clipData);
    }


    //=============================================================
    /**
     * Quote Join And Wrap
     * EX: ["", ""]
     * @param stringList  StringList Data
     * @return String
     */
    //=============================================================
    public static String quoteJoinAndWrap(List<String> stringList) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < stringList.size(); i++) {
            sb.append(QUOTE).append(stringList.get(i)).append(QUOTE).append(LAST_SPACE_COMMA);
        }

        String result = sb.toString();
        result = result.substring(0, result.length() - 2);
        result = LEFT_BRACKET + result + RIGHT_BRACKET;
        return result;
    }


    //=============================================================
    /**
     * Converted to URL Parameters
     * EX: &Key=Value
     * @param map  Map Data
     * @return String
     */
    //=============================================================
    public static String toUrlParameters(Map<String, Object> map) {
        String result = EMPTY;

        try {
            List<Map.Entry<String, Object>> infoIds = new ArrayList<>(map.entrySet());

            Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {
                public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                    return (o1.getKey()).compareTo(o2.getKey());
                }
            });

            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> item : infoIds) {
                String key = item.getKey();
                Object val = item.getValue();
                sb.append(AND).append(key).append(EQUAL).append(val);
            }

            result = sb.toString();
        } catch (ClassCastException e) {
            return null;
        }
        return result;
    }
}
