package pers.jojo.tools.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @ClassName DateUtil
 * @Description
 * @Author 张淳
 * @Date 2019/12/19 14:40
 * @Version 1.0.0
 **/
public class DateUtil {
    /**
     * 判断闰年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        Calendar cal = Calendar.getInstance();
        return ((GregorianCalendar) cal).isLeapYear(year);
    }

    /**
     * 当前周数
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 取得指定时间后的时间
     *
     * @param date   基准时间
     * @param field  字段名称 Calendar.xxx
     * @param amount 数量,支持负数
     * @return
     */
    public static Date addDate(Date date, int field, int amount) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

}