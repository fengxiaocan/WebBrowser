package com.evil.webbrowser.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * sharedPreferences快速保存数据/获取数据
 */
public class SpUtils {
    private SpUtils() {}

    private static SharedPreferences getSharedP(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AppUtils.getAppName(context), Context.MODE_PRIVATE);
        return sp;
    }

    /**
     * 保存String类型的信息
     */
    public static void save(Context context, String key, String value) {
        SharedPreferences sp = getSharedP(context);
        // 获取编辑器
        Editor edit = sp.edit();
        // 写入数据
        edit.putString(key, value);
        // 提交数据
        edit.commit();
        sp = null;
    }

    /**
     * 保存String类型的信息
     */
    public static void save(Context context, String... list) {
        if (list == null) {
            return;
        }
        SharedPreferences sp = getSharedP(context);
        // 获取编辑器
        Editor edit = sp.edit();
        for (int i = 0; i < list.length; i += 2) {
            // 写入数据
            edit.putString(list[i], list[i + 1]);
        }
        // 提交数据
        edit.commit();
        sp = null;
    }

    /**
     * 获取String类型的数据信息
     */
    public static String getInfo(Context context, String key, String defValue) {
        SharedPreferences sp    = getSharedP(context);
        String            value = sp.getString(key, defValue);
        sp = null;
        return value;
    }

    /**
     * 保存boolean类型的置
     */
    public static void save(Context context, String key, boolean value) {
        SharedPreferences sp = getSharedP(context);
        // 获取编辑器
        Editor edit = sp.edit();
        // 写入数据
        edit.putBoolean(key, value);
        // 提交数据
        edit.commit();
        sp = null;
    }

    /**
     * 获取boolean的值
     */
    public static boolean getInfo(Context context, String key, boolean defValue) {
        SharedPreferences sp    = getSharedP(context);
        boolean           value = sp.getBoolean(key, defValue);
        sp = null;
        return value;
    }

    /**
     * 获取int类型的值
     */
    public static void save(Context context, String key, int value) {
        SharedPreferences sp = getSharedP(context);
        // 获取编辑器
        Editor edit = sp.edit();
        // 写入数据
        edit.putInt(key, value);
        // 提交数据
        edit.commit();
        sp = null;
    }

    /**
     * 获取int类型的值
     */
    public static int getInfo(Context context, String key, int defValue) {
        SharedPreferences sp    = getSharedP(context);
        int               value = sp.getInt(key, defValue);
        sp = null;
        return value;
    }
}
