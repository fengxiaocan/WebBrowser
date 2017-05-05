package com.evil.webbrowser.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 获取文件SHA-1
 */
public class Sha1Utils {
    /**
     * @return 返回SHA-1值的输入流
     *
     * @in 根据输入的流来获取SHA-1值
     */
    public static String getSHA1(InputStream in) {
        try {
            MessageDigest md  = MessageDigest.getInstance("SHA-1");
            byte[]        arr = new byte[1024 * 10];
            int           len;
            while ((len = in.read(arr)) != -1) {
                md.update(arr, 0, len); // 计算输入流中的SHA-1值
            }
            in.close();

            // 返回执行的结果
            StringBuffer sb     = new StringBuffer();
            byte[]       digest = md.digest();
            for (byte b : digest) {
                // 怎么把int值转成十六进制
                String hexString = Integer.toHexString(
                        0x000000ff & b); // 把byte转换成16进制
                if (hexString.length() == 1) {
                    sb.append(0).append(hexString); // 只有一位数的时候,补足两位,前面加0
                } else {
                    sb.append(hexString); // 拼接SHA-1值
                }
            }
            return sb.toString().toUpperCase(); // 返回SHA-1值并转换成大写
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * @return 返回文件的SHA-1值
     *
     * @file 要获取SHA-1值的文件
     */
    public static String getSHA1(File file) {
        try {
            InputStream in = new FileInputStream(file);
            return getSHA1(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @return 返回文件的SHA-1值
     *
     * @filePath 要获取SHA-1值的文件的文件路径
     */
    public static String getSHA1(String filePath) {
        File file = new File(filePath);
        return getSHA1(file);
    }


    /**
     * @return 返回SHA-1值
     *
     * @category 计算加密byte数组信息
     * @arr 根据byte数组来获取SHA-1值
     */
    public static String codeSHA1(byte[] arr) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            md.update(arr); // 计算byte数组中的SHA-1值

            // 返回执行的结果
            StringBuffer sb     = new StringBuffer();
            byte[]       digest = md.digest();
            for (byte b : digest) {
                // 怎么把int值转成十六进制
                String hexString = Integer.toHexString(
                        0x000000ff & b); // 把byte转换成16进制
                if (hexString.length() == 1) {
                    sb.append(0).append(hexString); // 只有一位数的时候,补足两位,前面加0
                } else {
                    sb.append(hexString); // 拼接SHA-1值
                }
            }
            return sb.toString().toUpperCase(); // 返回SHA-1值并转换成大写
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @return 返回SHA-1值
     *
     * @category 计算加密字符串信息
     * @msg 根据字符串来获取SHA-1值
     */
    public static String codeSHA1(String msg) {
        byte[] bytes = msg.getBytes();
        return codeSHA1(bytes);
    }
}
