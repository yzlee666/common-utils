package cn.yzlee.util.string;

import java.util.Objects;

/**
 * @Author:lyz
 * @Date: 2018/3/14 16:38
 * @Desc: 字符串工具类
 **/
public class StringUtil
{
    /**
     * 判断字符串不为null且不为空字符串
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str){
        return Objects.nonNull(str)&&!Objects.equals(str.trim(),"");
    }

    /**
     *判断字符串为null或者为空字符串
     * @param str
     * @return
     */
    public static boolean isBlank(String str){
        return Objects.isNull(str)||Objects.equals(str.trim(),"");
    }

    /**
     * 字符串前补零操作
     * @param str 字符串
     * @param length 目标字符串长度
     * @return
     */
    public static String patchZeroToString(String str,int length){
        StringBuffer result = new StringBuffer("");
        int index = str.trim().length();
        if(index<length){
            for(int i = (length-index);i>0;i--){
                result.append("0");
            }
        }
        result.append(str.trim());
        return result.toString();

    }
}
