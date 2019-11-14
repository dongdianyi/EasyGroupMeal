package gzkj.easygroupmeal.utli;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    /**
     * 注意：HH表示24小时制，hh表示12小时制
     */
    //日期字符串转换Date实体
    public static Date parseServerTime(String serverTime, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINESE);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        Date date = null;
        try {
            date = sdf.parse(serverTime);
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
        return date;
    }

    //秒数转换成时分秒
    public static String convertSecToTimeString(long lSeconds) {
        long nHour = lSeconds / 3600;
        long nMin = lSeconds % 3600;
        long nSec = nMin % 60;
        nMin = nMin / 60;

        return String.format("%02d小时%02d分钟%02d秒", nHour, nMin, nSec);
    }

    //Date对象获取时间字符串
    public static String getDateStr(Date date, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);

    }

    //时间戳转换日期格式字符串
    public static String timeStamp2Date(long time, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time * 1000));
    }

    //日期格式字符串转换时间戳
    public static long date2TimeStamp(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date).getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //获取某个日期前后N天的日期

    /**
     * 获取某个日期前后N天的日期
     *
     * @param beginDate
     * @param distanceDay 前后几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @param format      日期格式，默认"yyyy-MM-dd"
     * @return
     */
    public static String getOldDateByDay(Date beginDate, int distanceDay, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat dft = new SimpleDateFormat(format);
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    //获取前后几个月的日期

    /**
     * 获取前后几个月的日期
     *
     * @param beginDate
     * @param distanceMonth
     * @param format
     * @return
     */
    public static String getOldDateByMonth(Date beginDate, int distanceMonth, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat dft = new SimpleDateFormat(format);
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.MONTH, date.get(Calendar.MONTH) + distanceMonth);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    /**
     * 根据日期字符串获取年
     */
    public static int getYear(String dateStr, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd";
        }
        Date date=null;
        Calendar calendar  = Calendar.getInstance();
        SimpleDateFormat dft = new SimpleDateFormat(format);
        try {
        // 用parse方法，可能会异常，所以要try-catch
            date = dft.parse(dateStr);
        // 获取日期实例
        // 将日历设置为指定的时间
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.get(Calendar.YEAR);
    }
    /**
     * 根据日期字符串获取年
     */
    public static int getMonth(String dateStr, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd";
        }
        Date date=null;
        Calendar calendar  = Calendar.getInstance();
        SimpleDateFormat dft = new SimpleDateFormat(format);
        try {
        // 用parse方法，可能会异常，所以要try-catch
            date = dft.parse(dateStr);
        // 将日历设置为指定的时间
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.get(Calendar.MONTH)+1;
    }
    /**
     * 根据日期字符串获取天
     */
    public static int getDay(String dateStr, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd";
        }
        Date date=null;
        Calendar calendar  = Calendar.getInstance();
        SimpleDateFormat dft = new SimpleDateFormat(format);
        try {
        // 用parse方法，可能会异常，所以要try-catch
            date = dft.parse(dateStr);
        // 获取日期实例
        // 将日历设置为指定的时间
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
