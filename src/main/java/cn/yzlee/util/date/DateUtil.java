package cn.yzlee.util.date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author:lyz
 * @Date: 2018/3/14 15:09
 * @Desc: 日期工具类
 **/
public class DateUtil
{
    private final static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static Date StringToDate(String value, String format){
        if(value== null || value.trim().equals("")){
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


}
