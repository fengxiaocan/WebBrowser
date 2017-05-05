package com.evil.webbrowser.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author 风小灿
 * @date 2016-6-19
 */
public class TimeUtils {
    /**
     * 格式:2016-08-20 11:11:11
     */
    public static final String DATE_TYPE1 = "yyyy-MM-dd HH:mm:ss";
    /**
     * 格式:2016-08-20 Tuesday
     */
    public static final String DATE_TYPE2 = "yyyy-MM-dd E";
    /**
     * 格式:2016-08-20
     */
    public static final String DATE_TYPE3 = "yyyy-MM-dd";

    /**
     * 格式:2016年 第181天
     */
    public static final String DATE_TYPE4  = "yyyy年 第D天";
    /**
     * 格式:12:24:36
     */
    public static final String DATE_TYPE5  = "HH:mm:ss";
    /**
     * 格式:2016年6月19日 星期日
     */
    public static final String DATE_TYPE6  = "yyyy年MM月dd日 E";
    /**
     * 格式:2015年12月23日
     */
    public static final String DATE_TYPE7  = "yyyy年MM月dd日";
    /**
     * 格式:2015年12月23日 星期日 下午
     */
    public static final String DATE_TYPE8  = "yyyy年MM月dd日 E a";
    /**
     * 格式:12:23:34 星期日 下午
     */
    public static final String DATE_TYPE9  = "HH:mm:ss E a";
    /**
     * 格式:21时12分18秒
     */
    public static final String DATE_TYPE10 = "HH时mm分ss秒";
    /**
     * 格式:20150223
     */
    public static final String DATE_TYPE11 = "yyyyMMdd";
    /**
     * 格式:20150213 211002
     */
    public static final String DATE_TYPE12 = "yyyyMMdd HHmmss";
    /**
     * 格式:2-23
     */
    public static final String DATE_TYPE13 = "MM-dd";
    /**
     * 格式:12:24
     */
    public static final String DATE_TYPE14 = "HH:mm";
    /**
     * 格式:12.20
     */
    public static final String DATE_TYPE15 = "MM.dd";

    /**
     * 格式:2016-08-20 11:11
     */
    public static final String DATE_TYPE16 = "yyyy-MM-dd HH:mm";

    /**
     * 格式:08-20  22:23
     */
    public static final String DATE_TYPE17 = "MM-dd  HH:mm";
    /**
     * 格式:16-08-20
     */
    public static final String DATE_TYPE18 = "yy-MM-dd";
    /**
     * 格式:2016-08-20 11:11:11
     */
    public static final String DATE_TYPE19 = "yyyy-MM-dd HH:mm:ss";
    /**
     * 格式:20160820-11:11:11
     */
    public static final String DATE_TYPE20 = "yyyyMMdd-HH:mm:ss";
    /**
     * 格式:20160820111111
     */
    public static final String DATE_TYPE21 = "yyyyMMddHHmmss";
    /**
     * 格式:2016_08_20_11:11:11
     */
    public static final String DATE_TYPE22 = "yyyy_MM_dd_HH:mm:ss";
    /**
     * 获取当前时间
     *
     * @return 当前时间的毫秒值
     */
    public static long getNowTime() {
        long nowTime = System.currentTimeMillis();
        return nowTime;
    }

    /**
     * 获取当前时间
     *
     * @param type 时间的格式
     *
     * @return 返回格式化后的时间
     */
    public static String getNowTime(String type) {
        long nowTime = System.currentTimeMillis();
        return formatTime(nowTime, type);
    }

    /**
     * 根据传入的格式来格式化时间
     *
     * @param date 时间 单位:毫秒
     * @param type 日期格式
     *
     * @return 传入格式类型的时间字符串
     */
    public static String formatTime(long date, String type) {
        SimpleDateFormat sdf  = new SimpleDateFormat(type);
        Date             da   = new Date(date);
        String           time = sdf.format(da);
        return time;
    }

    /**
     * 判断两个日期是否是同一天
     *
     * @param date1
     * @param date2
     *
     * @return
     */
    public static boolean isSameDate(long date1, long date2) {
        Calendar cal1 = Calendar.getInstance();
        Date     da1  = new Date(date1);
        cal1.setTime(da1);

        Calendar cal2 = Calendar.getInstance();
        Date     da2  = new Date(date2);
        cal2.setTime(da2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) ==
                                            cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) ==
                                            cal2.get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    /**
     * 传入对应的时间字符串和对应的时间格式,转化为long类型的时间毫秒值
     *
     * @param date 例如:2016-02-23
     * @param type 则选择对应的 DATE_TYPE2 格式
     */
    public static long strFormatTime(String date, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        try {
            Date parse = sdf.parse(date);
            long time  = parse.getTime();
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将时间字符串 转化为 对应格式的 时间Date
     *
     * @param strDate 要转化的时间字符串
     * @param type    时间字符串对应的格式类型
     *
     * @return Date类型的时间格式
     *
     * @throws ParseException 格式转换异常
     */
    public static Date strToDate(String strDate, String type) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(type);
        Date             parse     = formatter.parse(strDate);
        return parse;
    }

    /**
     * 把Date时间转换为指定格式的时间字符串
     *
     * @param date 要转化的时间Date
     * @param type 转化为目标格式时间类型
     *
     * @return 返回指定格式的字符串时间
     */
    public static String dateToStr(Date date, String type) {
        SimpleDateFormat formatter = new SimpleDateFormat(type);
        String           string    = formatter.format(date);
        return string;
    }

    /**
     * 得到现在小时
     *
     * @return int 类型的现在小时数
     */
    public static int getNowHour() {
        Date date  = new Date(getNowTime());
        int  hours = date.getHours();
        return hours;
    }

    /**
     * 得到现在分钟数
     *
     * @return int 类型的现在分钟数
     */
    public static int getNowMinutes() {
        Date date    = new Date(getNowTime());
        int  minutes = date.getMinutes();
        return minutes;
    }

    /**
     * 得到现在秒数
     *
     * @return int 类型的现在秒数
     */
    public static int getNowSeconds() {
        Date date    = new Date(getNowTime());
        int  seconds = date.getSeconds();
        return seconds;
    }

    /**
     * 得到现在是一个星期内的第几天
     *
     * @return int 类型的现在星期几的天数 0为星期天
     */
    public static int getNowDay() {
        Date date = new Date(getNowTime());
        int  day  = date.getDay();
        return day;
    }

    /**
     * 得到现在星期几
     *
     * @return String 类型的现在星期几的天数:星期几
     */
    public static String getNowDayStr() {
        Date   date = new Date(getNowTime());
        int    day  = date.getDay();
        String str  = "";
        switch (day) {
            case 0:
                str = "星期天";
                break;
            case 1:
                str = "星期一";
                break;
            case 2:
                str = "星期二";
                break;
            case 3:
                str = "星期三";
                break;
            case 4:
                str = "星期四";
                break;
            case 5:
                str = "星期五";
                break;
            case 6:
                str = "星期六";
                break;
        }
        return str;
    }

    /**
     * 得到现在是一个月内的第几天
     *
     * @return int 一个月内的第几天
     */
    public static int getNowDate() {
        Date date  = new Date(getNowTime());
        int  dates = date.getDate();
        return dates;
    }

    /**
     * 得到传入的时间是一个月内的第几天
     *
     * @return int 一个月内的第几天
     */
    public static int getDate(long time) {
        Date date  = new Date(time);
        int  dates = date.getDate();
        return dates;
    }

    /**
     * 得到现在是一年内第几个月
     *
     * @return int 现在的一年内第几个月 1为第一个月
     */
    public static int getNowMonth() {
        Date date  = new Date(getNowTime());
        int  month = date.getMonth() + 1;
        return month;
    }

    /**
     * 得到传入的时间是一年内第几个月
     *
     * @return int 现在的一年内第几个月 1为第一个月
     */
    public static int getMonth(long time) {
        Date date  = new Date(time);
        int  month = date.getMonth() + 1;
        return month;
    }

    /**
     * 得到现在的第几年
     *
     * @return int 现在是哪一年
     */
    public static int getNowYear() {
        Date date = new Date(getNowTime());
        int  year = date.getYear();
        year = year + 1900;
        return year;
    }

    /**
     * 格式化时间状态 比如 1239182398,返回的时间格式是 32分钟前
     */
    public static String formatTimeStatu(long time, String type) {
        long timeMillis = System.currentTimeMillis();
        long dTime      = timeMillis - time;

        if (dTime <= 0) {
            return "刚刚";
        }

        if (dTime > 0 && dTime < 60) {
            return dTime + "秒前";
        }

        dTime = dTime / 60;

        if (dTime >= 0 && dTime < 60) {
            return dTime + "分钟前";
        }

        dTime = dTime / 60;

        if (dTime >= 0 && dTime <= 24) {
            return dTime + "小时前";
        }
        dTime = dTime / 24;

        if (dTime > 0 && dTime <= 7) {
            return dTime + "天前";
        }
        dTime = dTime / 7;
        if (dTime > 0 && dTime <= 4) {
            return dTime + "周前";
        }

        return formatTime(time, type);
    }

    /**
     * 获取月日: 11.11
     */
    public static String getMonthDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int month = calendar.get(Calendar.MONTH) + 1;  //获取月;
        int date  = calendar.get(Calendar.DATE);   //获取天;
        return month + "." + date;
    }

    /**
     * 得到传入的时间是一年内第几个月
     *
     * @return int 现在的一年内第几个月一为第一个月
     */
    public static String getMonthStr(long time) {
        Date date  = new Date(time);
        int  month = date.getMonth() + 1;
        switch (month) {
            case 1:
                return "一月";
            case 2:
                return "二月";
            case 3:
                return "三月";
            case 4:
                return "四月";
            case 5:
                return "五月";
            case 6:
                return "六月";
            case 7:
                return "七月";
            case 8:
                return "八月";
            case 9:
                return "九月";
            case 10:
                return "十月";
            case 11:
                return "十一月";
            case 12:
                return "十二月";
        }
        return "一";
    }

    /**
     * 获取时长
     */
    public static String getTimeDuration(long time) {
        if (time <= 0) {
            return "0秒";
        }

        long dTime = time / 1000;

        if (dTime <= 60) {
            return dTime + "秒";
        }

        dTime = dTime / 60;
        if (dTime >= 0 && dTime < 60) {
            return dTime + "分钟" + (dTime % 60) + "秒";
        }

        dTime = dTime / 60;
        if (dTime < 24) {
            return dTime + "小时" + (dTime % 60) + "分钟";
        }

        dTime = dTime / 24;
        long dHour = dTime % 24;
        return dTime + "天" + dHour + "小时";
    }

    /**
     * 提取一个月中的最后一天
     */
    public static Date getLastDate(long day) {
        Date date           = new Date();
        long date_3_hm      = date.getTime() - 3600000 * 34 * day;
        Date date_3_hm_date = new Date(date_3_hm);
        return date_3_hm_date;
    }


    /**
     * 判断是否润年
     *
     * @param ddate
     *
     * @return
     */
    public static boolean isLeapYear(String ddate, String type) throws ParseException {

        /**
         * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
         * 3.能被4整除同时能被100整除则不是闰年
         */
        Date              d  = strToDate(ddate, type);
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(d);
        int year = gc.get(Calendar.YEAR);
        if ((year % 400) == 0) {
            return true;
        } else if ((year % 4) == 0) {
            if ((year % 100) == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    /**
     * 获取一个月的最后一天
     *
     * @param dat  时间
     * @param type 格式类型
     *
     * @return
     */
    public static String getLastDayOfMonth(String dat, String type) throws ParseException {
        String str   = dat.substring(0, 8);
        String month = dat.substring(5, 7);
        int    mon   = Integer.parseInt(month);
        if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 ||
            mon == 10 || mon == 12) {
            str += "31";
        } else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
            str += "30";
        } else {
            if (isLeapYear(dat, type)) {
                str += "29";
            } else {
                str += "28";
            }
        }
        return str;
    }


    /**
     * 产生周序列,即得到当前时间所在的年度是第几周
     *
     * @return
     */
    public static String getSeqWeek() {
        Calendar c    = Calendar.getInstance(Locale.CHINA);
        String   week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
        if (week.length() == 1) {
            week = "0" + week;
        }
        String year = Integer.toString(c.get(Calendar.YEAR));
        return year + week;
    }


    /**
     * 获取字符串日期是星期几
     *
     * @param sdate 2015年2月23日
     *
     * @return 返回是星期几的字符串
     */
    public static String getWeekStr(String sdate, String type) throws ParseException {
        Date   date = strToDate(sdate, type);
        int    week = date.getDay();
        String str  = "";
        if (week == 0) {
            str = "星期日";
        } else if (week == 1) {
            str = "星期一";
        } else if (week == 2) {
            str = "星期二";
        } else if (week == 3) {
            str = "星期三";
        } else if (week == 4) {
            str = "星期四";
        } else if (week == 5) {
            str = "星期五";
        } else if (week == 6) {
            str = "星期六";
        }
        return str;
    }

    /**
     * 两个时间之间的天数
     */
    public static long getDays(String date1, String date2, String type) {
        if (date1 == null || date1.equals("")) {
            return 0;
        }
        if (date2 == null || date2.equals("")) {
            return 0;
        }
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat(type);
        Date   date        = null;
        Date   mydate      = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }
}
