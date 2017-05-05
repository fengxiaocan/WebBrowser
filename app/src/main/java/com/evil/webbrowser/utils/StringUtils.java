package com.evil.webbrowser.utils;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.Collator;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.evil.webbrowser.utils.TimeUtils.DATE_TYPE21;

/*
 *  @创建时间:  2016/9/6 14:07
 *  @描述：    字符串操作工具类
 */
public class StringUtils {

    /** 判断字符串是否为空 */
    public static boolean isEmpty(Object str) {
        if (str == null) {
            return true;
        }
        if ("".equals(str)) {
            return true;
        }
        return false;
    }

    /** 使用字符缓冲区来拼接字符串 */
    public static String join(String... s) {
        StringBuffer sb = new StringBuffer();
        for (String s1 : s) {
            sb.append(s1);
        }
        return sb.toString();
    }

    /**
     * 格式化数字数量
     */
    public static String formatNumber(long num) {
        if (num >= 0 && num < 10000) {
            return num + "";
        }

        if (num >= 10000) {
            long start = num / 10000;
            long end   = num % 10000;
            if (end < 1000) {
                return start + "万";
            } else {
                end = end / 1000;
                return start + "." + end + "万";
            }
        }
        return "0";
    }

    /**
     * 获取流编码
     */
    public static String getTextCode(String filePath) {// 转码
        try {
            File file = new File(filePath);
            return getTextCode(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取流编码
     */
    public static String getTextCode(File file) {
        try {
            FileInputStream is = new FileInputStream(file);
            return getTextCode(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取流编码
     */
    public static String getTextCode(InputStream is) {// 转码
        String text = "GBK";
        try {
            BufferedInputStream in = new BufferedInputStream(is);
            in.mark(4);
            byte[] first3bytes = new byte[3];
            in.read(first3bytes);//找到文档的前三个字节并自动判断文档类型。
            in.reset();
            if (first3bytes[0] == (byte) 0xEF &&
                    first3bytes[1] == (byte) 0xBB &&
                    first3bytes[2] == (byte) 0xBF)
            {// utf-8

                text = "utf-8";
            } else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFE) {
                text = "unicode";
            } else if (first3bytes[0] == (byte) 0xFE && first3bytes[1] == (byte) 0xFF) {
                text = "utf-16be";
            } else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFF) {
                text = "utf-16le";
            } else {
                text = "GBK";
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * 字符串转换unicode
     */
    public static String str2Unicode(String string) {
        if (string == null) {
            return string;
        }
        try {
            StringBuffer unicode = new StringBuffer();
            for (int i = 0; i < string.length(); i++) {
                // 取出每一个字符
                char c = string.charAt(i);
                // 转换为unicode
                String hexString = Integer.toHexString(c);
                unicode.append("\\u" + hexString);
            }
            return unicode.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {
        if (unicode == null) {
            return unicode;
        }
        String[] hex = unicode.split("\\\\u");
        if (hex == null) {
            return unicode;
        } else {
            StringBuffer string = new StringBuffer();
            for (int i = 0; i < hex.length; i++) {
                try {
                    // 转换出每一个代码点
                    int data = Integer.parseInt(hex[i], 16);
                    // 追加成string
                    string.append((char) data);
                } catch (Exception e) {
                    string.append(hex[i]);
                }

            }
            return string.toString();
        }
    }

    /**
     * 获取url的后缀
     */
    public static String getUrlSuffix(String url) {
        if (isEmpty(url)) {
            return "";
        } else {
            int    of  = url.lastIndexOf('.');
            String fix = url.substring(of, url.length());
            return fix;
        }
    }

    /**
     * 获取url的中的文件名
     */
    public static String getUrlFileName(String url) {
        if (isEmpty(url)) {
            return TimeUtils.getNowTime(DATE_TYPE21) + ".unknow";
        } else {
            String fix;
            try {
                int of = url.lastIndexOf('/');
                fix = url.substring(of, url.length());
            } catch (Exception e) {
                return url.hashCode() + ".unknow";
            }
            return fix;
        }
    }

    /**
     * 汉字排序
     */
    public static List<String> ChineseCharacterSort(List<String> list) {
        Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
        Collections.sort(list, com);
        return list;
    }


    /**
     * 把字符串加密
     *
     * @param code 要加密的信息
     *
     * @return 还原后的信息
     */
    public static String encode(String code) {
        int    seed  = 111; // 加密密码
        byte[] bytes = code.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] ^= seed;
        }
        String string = new String(bytes);
        return string;
    }

    /**
     * 解密加密的字符串
     *
     * @param code 要加密的信息
     *
     * @return 还原后的信息
     */
    public static String decode(String code) {
        return encode(code);
    }

    /**
     * 把字符串加密
     *
     * @param code 要加密的信息
     * @param seed 加密的密码
     *
     * @return 还原后的信息
     */
    public static String encode(String code, int seed) {
        // 加密密码
        byte[] bytes = code.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] ^= seed;
        }
        String string = new String(bytes);
        return string;
    }

    /**
     * 解密加密的字符串
     *
     * @param code 要加密的信息
     * @param seed 解密的密码
     *
     * @return 还原后的信息
     */
    public static String decode(String code, int seed) {
        return encode(code, seed);
    }


    /**
     * 返回一个高亮spannable
     *
     * @param content 文本内容
     * @param color   高亮颜色
     * @param start   起始位置
     * @param end     结束位置
     *
     * @return 高亮spannable
     */
    public static CharSequence getHighLightText(String content, int color, int start, int end) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        start = start >= 0
                ? start
                : 0;
        end = end <= content.length()
              ? end
              : content.length();
        SpannableString spannable = new SpannableString(content);
        CharacterStyle  span      = new ForegroundColorSpan(color);
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 获取链接样式的字符串，即字符串下面有下划线
     *
     * @param txt 文本
     *
     * @return 返回链接样式的字符串
     */
    public static Spanned getHtmlStyleString(String txt) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"\"><u><b>")
          .append(txt)
          .append(" </b></u></a>");
        return Html.fromHtml(sb.toString());
    }

    /**
     * 格式化文件大小，不保留末尾的0
     *
     * @param len 大小
     *
     * @return
     */
    public static String formatFileSize(long len) {
        return formatFileSize(len, false);
    }

    /**
     * 格式化文件大小，保留末尾的0，达到长度一致
     *
     * @param len      大小
     * @param keepZero 是否保留小数点
     *
     * @return
     */
    public static String formatFileSize(long len, boolean keepZero) {
        String        size;
        DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
        DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
        if (len < 1024) {
            size = String.valueOf(len + "B");
        } else if (len < 10 * 1024) {
            // [0, 10KB)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
        } else if (len < 100 * 1024) {
            // [10KB, 100KB)，保留一位小数
            size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
        } else if (len < 1024 * 1024) {
            // [100KB, 1MB)，个位四舍五入
            size = String.valueOf(len / 1024) + "KB";
        } else if (len < 10 * 1024 * 1024) {
            // [1MB, 10MB)，保留两位小数
            if (keepZero) {
                size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024 / 1024 / (float) 100)) + "MB";
            } else {
                size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100) + "MB";
            }
        } else if (len < 100 * 1024 * 1024) {
            // [10MB, 100MB)，保留一位小数
            if (keepZero) {
                size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024 / 1024 / (float) 10)) + "MB";
            } else {
                size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
            }
        } else if (len < 1024 * 1024 * 1024) {
            // [100MB, 1GB)，个位四舍五入
            size = String.valueOf(len / 1024 / 1024) + "MB";
        } else {
            // [1GB, ...)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100) + "GB";
        }
        return size;
    }

    /**
     * 获取double的后几位小数
     *
     * @param num      double值
     * @param decimals 保留几位小数
     *
     * @return 保留后的字符串
     */
    public static String getDoubleTwoDecimals(double num, int decimals) {
        String numStr = String.valueOf(num);
        int    last   = numStr.lastIndexOf("\\.");
        int    i      = last + decimals;
        if (i < numStr.length()) {
            numStr.substring(0, i);
        } else if (i == numStr.length()) {
            return numStr;
        } else {
            for (int j = 0; j < i - numStr.length(); j++) {
                numStr += "0";
            }
        }
        return numStr;
    }


    /**
     * 格式化音乐时间: 120 000 --> 02:00
     *
     * @param time
     *
     * @return
     */
    public static String formatMusicTime(long time) {
        time = time / 1000;
        String formatTime;
        if (time < 10) {
            formatTime = "00:0" + time;
            return formatTime;
        } else if (time < 60) {
            formatTime = "00:" + time;
            return formatTime;
        } else {
            long i = time / 60;
            if (i < 10) {
                formatTime = "0" + i + ":";
            } else {
                formatTime = i + ":";
            }
            long j = time % 60;
            if (j < 10) {
                formatTime += "0" + j;
            } else {
                formatTime += j;
            }

            return formatTime;
        }
    }

    /**
     * 检测手机号码
     * @param phone
     */
    public static boolean checkoutPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        if (phone.length() < 11) {
            return false;
        }
        if (!phone.startsWith("1")) {
            return false;
        }
        if (phone.startsWith("10") || phone.startsWith("11") || phone.startsWith("12")) {
            return false;
        }
        return true;
    }
}
