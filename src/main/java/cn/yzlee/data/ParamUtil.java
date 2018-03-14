package cn.yzlee.data;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.UnicodeBlock.*;
import static java.lang.Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A;
import static java.lang.Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B;

/**
 * @Author:lyz
 * @Date: 2018/3/14 17:49
 * @Desc: 参数验证工具类
 **/
public class ParamUtil
{
    private final static String PWD_REG = "[ `~@#$%^&*()=|{}':;',\\[\\].<>/~@#￥%……&*（）——|{}【】‘；：”“’。，、？！]|\n|\r|\t";

    private final static String MOBILE_REG = "^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9])|(16[0-9]))\\d{8}$";

    private final static String HK_MOBILE_REG = "^(5|6|8|9)\\d{7}$";

    private final static String TEL_REG = "^((0[0-9]{2}-)?([0-9]{8})|(0[0-9]{3}-)?([0-9]{7}))+(-[0-9]{1,4})?$";

    /**
     * 检查是否是中国手机号码
     * @param mobileStr
     * @return
     */
    public static boolean isChinaMobile(String mobileStr){
        return isDaLuMobile(mobileStr)||isHKMobile(mobileStr);
    }

    /**
     * 检查是否是正确的联系方式（大陆手机号或者联系电话）
     * @return
     */
    public static boolean isMobileOrTel(String str){
        return isDaLuMobile(str)||isTel(str);
    }
    /**
     * 检查是否是香港手机号码
     * @param mobileStr
     * @return
     */
    public static boolean isHKMobile(String mobileStr){
        if(Objects.isNull(mobileStr)||Objects.equals(mobileStr.trim(),"")){
            return false;
        }
        Pattern p = Pattern.compile(HK_MOBILE_REG);
        Matcher m = p.matcher(mobileStr);
        return m.matches();
    }

    /**
     * 检查是否是正确的联系电话
     * @param telStr
     * @return
     */
    public static boolean isTel(String telStr){
        if(Objects.isNull(telStr)||Objects.equals(telStr.trim(),"")){
            return false;
        }
        Pattern p = Pattern.compile(TEL_REG);
        Matcher m = p.matcher(telStr);
        return m.matches();
    }
    /**
     * 检查是否是大陆手机号码
     * @param mobileStr
     * @return
     */
    public static boolean isDaLuMobile(String mobileStr){
        if(Objects.isNull(mobileStr)||Objects.equals(mobileStr.trim(),"")){
            return false;
        }
        Pattern p = Pattern.compile(MOBILE_REG);
        return p.matcher(mobileStr).matches();

    }

    /**
     * 判断密码是否含有非法字符
     * @param str
     * @return
     */
    public static boolean isIllegalPwd(String str){
        Pattern p = Pattern.compile(PWD_REG);
        return p.matcher(str).find();
    }

    /**
     * 判断字符串是否有中文
     * @param checkStr
     * @return
     */
    public static boolean checkStringContainChinese(String checkStr){
        if(Objects.nonNull(checkStr)&&!Objects.equals(checkStr,"")){
            char[] checkChars = checkStr.toCharArray();
            for(int i = 0; i < checkChars.length; i++){
                char checkChar = checkChars[i];
                if(checkCharContainChinese(checkChar)){
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkCharContainChinese(char checkChar){
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(checkChar);
        if(CJK_UNIFIED_IDEOGRAPHS == ub || CJK_COMPATIBILITY_IDEOGRAPHS == ub || CJK_COMPATIBILITY_FORMS == ub ||
                CJK_RADICALS_SUPPLEMENT == ub || CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A == ub || CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B == ub){
            return true;
        }
        return false;
    }
}
