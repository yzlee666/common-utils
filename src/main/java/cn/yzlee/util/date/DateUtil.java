package cn.yzlee.util.date;

import cn.yzlee.util.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * @Author:lyz
 * @Date: 2018/3/14 15:09
 * @Desc: 日期工具类
 **/
public class DateUtil
{
    /**
     * 字符串按指定格式转换成日期
     * @param value 日期字符串
     * @param format 指定格式
     * @return
     */
    public static Date stringToDate(String value, String format){
        if(StringUtil.isBlank(value)){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(value);
        } catch (ParseException e) {
            logger.error("{}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期转字符串
     * @param date
     * @return
     */
    public static String dateToString(Date date){
        if(Objects.isNull(date)){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * 按指定格式将日期转成字符串
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date,String format){
        if(Objects.isNull(date)){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     *获取l1跟l2之间相差多少秒
     * @param l1
     * @param l2
     * @return
     */
    public long getTwoTimeDiffSeconds(LocalDateTime l1,LocalDateTime l2){
        return Duration.between(l1,l2).getSeconds();
    }

    public static Date getFullStartDateTime(Date time) {
        String start = DateUtil.dateToString(time, DateUtil.DATE_FORMAT) + " 00:00:00.000";
        return DateUtil.stringToDate(start, DateUtil.FULL_FORMAT_SPEPARATE);
    }

    public static Date getFullStartDateTime(String date) {
        return DateUtil.stringToDate(date + " 00:00:00.000", DateUtil.FULL_FORMAT_SPEPARATE);
    }

    public static Date getFullEndDateTime(Date time) {
        String end = DateUtil.dateToString(time, DateUtil.DATE_FORMAT) + " 23:59:59.999";
        return DateUtil.stringToDate(end, DateUtil.FULL_FORMAT_SPEPARATE);
    }

    public static Date getFullEndDateTime(String date) {
        return DateUtil.stringToDate(date + " 23:59:59.999", DateUtil.FULL_FORMAT_SPEPARATE);
    }

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String FULL_FORMAT_SPEPARATE = "yyyy-MM-dd HH:mm:ss.SSS";

    private final static Logger logger = LoggerFactory.getLogger(DateUtil.class);


}
