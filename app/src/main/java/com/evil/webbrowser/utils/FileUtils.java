package com.evil.webbrowser.utils;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
 *  @描述：    流操作工具类
 */
public class FileUtils {
    /**
     * 创建文件夹
     */
    public static void mkdir(File dir) {
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 创建父类文件夹
     */
    public static void mkParentDir(File file) {
        if (file != null) {
            File parentFile = file.getParentFile();
            mkdir(parentFile);
        }
    }

    /**
     * 复制文件
     *
     * @param copy 要复制的文件
     * @param goal 目标文件
     */
    public static void copy(File copy, File goal) {
        FileInputStream  is = null;
        FileOutputStream os = null;
        try {
            mkParentDir(goal);
            is = new FileInputStream(copy);
            os = new FileOutputStream(goal);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            copy(is, os);
        }
    }

    /**
     * 复制文件
     *
     * @param is 文件流
     * @param os 目标流
     */
    public static void copy(InputStream is, OutputStream os) {
        try {
            byte[] arr = new byte[8192];
            int    len;
            while ((len = is.read(arr)) != -1) {
                os.write(arr, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(os);
            close(is);
        }
    }

    /**
     * 复制文件
     *
     * @param copyFile 要复制的文件
     * @param toDir    复制到的文件夹
     */
    public static String copyFile(File copyFile, File toDir) {
        File file = new File(toDir, copyFile.getName());
        copy(copyFile, file);
        return file.getAbsolutePath();
    }

    /**
     * 关流
     */
    public static void close(Closeable clo) {
        if (clo != null) {
            try {
                clo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 统计文件大小,包括文件和文件夹
     */
    public static long getFileSize(File file) {
        if (file == null) {
            return 0;
        }
        if (file.isFile()) {
            return file.length();
        } else {
            long   length = 0;
            File[] files  = file.listFiles();
            if (files == null) {
                return 0;
            } else {
                for (File file1 : files) {
                    length += getFileSize(file1);
                }
            }
            return length;
        }
    }


    /**
     * 统计文件夹大小
     */
    private static long getDirLength(File dir) {
        if (dir == null) {
            return 0;
        }
        if (dir.isFile()) {
            return dir.length();
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return 0;
        }
        long cacheLength = 0;
        for (File file : files) {
            if (file.isFile()) {
                cacheLength += file.length();
            } else {
                cacheLength += getDirLength(file);
            }
        }
        return cacheLength;
    }


    /**
     * 遍历删除文件夹下的所有内容
     */
    private static void deleteDir(File dir) {
        if (dir == null) {
            return;
        }
        if (dir.isFile()) {
            dir.delete();
            return;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile()) {
                file.delete();
            } else {
                deleteDir(file);
                file.delete();
            }
        }
        dir.delete();
    }


    /**
     * 把字符串写入文件中
     *
     * @param text   文本
     * @param path   文件路径
     * @param append 是否为添加
     */
    public static void writeFile(String text, String path, boolean append) {
        if (StringUtils.isEmpty(text)) {
            return;
        }
        File file = new File(path);
        mkParentDir(file);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, append));
            writer.write(text);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(writer);
        }
    }
}
