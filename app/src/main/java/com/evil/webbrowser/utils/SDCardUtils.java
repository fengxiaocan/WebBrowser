package com.evil.webbrowser.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

//SD卡相关的辅助类
public class SDCardUtils {
    private SDCardUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() +
               File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat            = new StatFs(filePath);
        long   availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统SD存储路径
     */
    public static String getSdRootPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }


    /**
     * 判断sd卡是否存在
     */
    public static boolean sdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
    }

    /**
     * 获取sd卡根目录
     */
    public static File getSdRoot() {
        File sdDir = Environment.getExternalStorageDirectory();//获取根目录
        return sdDir;
    }

    /**
     * 获取保存目录
     */
    public static File getSaveDir(Context context, String name) {
        File saveDir;
        if (sdCardExist()) {
            saveDir = new File(Environment.getDataDirectory(),
                    context.getPackageName() + "/" + name);//获取根目录
        } else {
            saveDir = new File(context.getFilesDir(), name);
        }
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        return saveDir;
    }


    /**
     * 获取保存目录
     */
    public static File getSdSaveDir(String name) {
        if (sdCardExist()) {
            File saveDir = new File(getSdRoot(), name);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            return saveDir;
        }
        throw new NullPointerException("SD卡不存在");
    }

}
