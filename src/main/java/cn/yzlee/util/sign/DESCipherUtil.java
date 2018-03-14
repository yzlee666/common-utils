package cn.yzlee.util.sign;


import cn.yzlee.util.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.text.ParseException;
import java.util.Base64;

/**
 * @Author:lyz
 * @Date: 2018/1/15 17:05
 * @Desc: desc加解密工具类
 **/
public class DESCipherUtil
{
    //加密算是是des
    private static final String ALGORITHM = "DES";
    //转换格式
    private static final String TRANSFORMATION = "DES/ECB/PKCS5Padding";

    private static SecretKeySpec initKey(String password) throws NoSuchAlgorithmException{
        KeyGenerator keyGenerator=KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128, new SecureRandom(password.getBytes()));
        SecretKey secretKey=keyGenerator.generateKey();
        SecretKeySpec key=new SecretKeySpec(secretKey.getEncoded(),ALGORITHM);
        return key;
    }

    //利用8个字节64位的key给src加密
    @SuppressWarnings("unused")
    public static byte[] encrypt(String content,String key)
    {
        try {
            //加密
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            KeySpec keySpec = new DESKeySpec(key.getBytes());
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,new SecureRandom());
            byte[] enMsgBytes = cipher.doFinal(content.getBytes("UTF-8"));
            return enMsgBytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //利用8个字节64位的key给src解密
    @SuppressWarnings("unused")
    public static byte[] decrypt(byte[] encryptBytes,String key){
        try {
            Cipher deCipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeyFactory deDecretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            KeySpec deKeySpec = new DESKeySpec(key.getBytes());
            SecretKey deSecretKey = deDecretKeyFactory.generateSecret(deKeySpec);
            deCipher.init(Cipher.DECRYPT_MODE, deSecretKey,new SecureRandom());
            byte[] deMsgBytes = deCipher.doFinal(encryptBytes);
            return deMsgBytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptReturnBase64(String content,String key){
        return Base64.getEncoder().encodeToString(DESCipherUtil.encrypt(content, key));
    }

    public static String decryptBase64(String content,String key){
        try{
            return new String(DESCipherUtil.decrypt(Base64.getDecoder().decode(content),key));
        }catch (Exception e){
            logger.error("解密内容为{},失败原因为{}",content,e);
            throw new RuntimeException(e);
        }
    }

    private final static Logger logger = LoggerFactory.getLogger(DESCipherUtil.class);

    public static void main(String[] args) throws Exception{

        final String key = "zjy01gci";
        String msg = "hello world. ";
        System.out.println("加密前："+msg);
        String a = encryptReturnBase64(msg,key);
        System.out.println("加密后："+a);
        System.out.println("解密后："+decryptBase64(a,key));
    }
}
