package cn.yzlee.util.string;

import java.util.Random;

/**
 * @Author:lyz
 * @Date: 2017/12/20 13:38
 * @Desc: 随机数生成器
 **/
public class RandomGenerator
{
    /**
     * 获取一定长度的随机字符串
     * @param length 指定的长度
     * @return 一定长度的随机字符串
     */
    public static String getRandomSringByLength(final int length){
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i< length;i++){
            int number = random.nextInt(base.length());
            buffer.append(base.charAt(number));
        }
        return buffer.toString();
    }
    /**
     * 获取一定长度的随机数字字符串
     * @param length 指定字符串长度
     * @return 一定长度的随机字符串
     */
    public static String getRandomNumberStringByLength(final int length){
        String base = "1234567890";
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i< length;i++){
            int number = random.nextInt(base.length());
            buffer.append(base.charAt(number));
        }
        return buffer.toString();
    }
}
