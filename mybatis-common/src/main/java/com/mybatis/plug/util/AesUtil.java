package com.mybatis.plug.util;

import com.mybatis.plug.exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Objects;

/**
 * @author coco
 * @date 2020-08-05 20:36
 **/
public class AesUtil {

    private static final Logger LOG = LoggerFactory.getLogger(AesUtil.class);

    private String iv = "yjcloud@12345678";

    //使用CBC模式，需要一个向量iv，可增加加密算法的强度
    private IvParameterSpec ivParameterSpec;

    /**
     * 工作模式：CBC.
     */
    private static final String TRANSFORM_CBC_PKCS5 = "AES/CBC/PKCS5Padding";



    private AesUtil(String iv) {
        this.iv = iv;
        ivParameterSpec = new IvParameterSpec(getUTF8Bytes(iv));
    }


    public static final AesUtil createInstance(String iv){
        if (Objects.nonNull(iv)&& iv.length() == 16) {
            return new AesUtil(iv);
        } else {
            throw new CommonException("向量iv 不能为空或长度需要等16");
        }
    }



    public String encode(String data, String key) throws Exception {
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return encode(data.getBytes(StandardCharsets.UTF_8),key);
    }

    public  String encode(byte[] data, String key) throws Exception {
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return Base64.getEncoder().encodeToString(encodeBytes(data,key));
    }


    public byte[] encodeBytes(byte[] data, String key) throws Exception {
        if (key == null) {
            LOG.warn("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (key.length() != 16) {
            LOG.warn("Key长度不是16位");
            return null;
        }
        byte[] raw = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        return encode(data,skeySpec);
    }

    public  byte[] encode(byte[] data, Key key) throws Exception {
        if (key == null) {
            LOG.warn("Key为空null");
            return null;
        }
        //"算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return cipher.doFinal(data);
    }

    public byte[] decode(String data,String key){
        return decode(Base64.getDecoder().decode(data),key);
    }

    public byte[] decode(byte[] data,String key) {
        final SecretKeySpec keySpec = new SecretKeySpec(getUTF8Bytes(key),"AES");
        try {
            Cipher encipher = Cipher.getInstance(TRANSFORM_CBC_PKCS5);

            //解密模式
            encipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            //然后再AES解密
            byte[] originalBytes = encipher.doFinal(data);
            //返回字符串
            return originalBytes;
        } catch (Exception e) {
            LOG.error("AES 解密 失败 ! reason :" + e.getMessage(),e);
        }
        return null;
    }

    public String decodeBase64String(String data,String key){
        return Base64.getEncoder().encodeToString(decode(data,key));
    }


    /**生成key，作为加密和解密密钥且只有密钥相同解密加密才会成功.
     * @return
     */
/*    public static Key createKey() {

        try {
            // 生成key
            KeyGenerator keyGenerator;
            //构造密钥生成器，指定为AES算法,不区分大小写
            keyGenerator = KeyGenerator.getInstance("AES");
            //生成一个128位的随机源,根据传入的字节数组
            keyGenerator.init(128);
            //产生原始对称密钥
            SecretKey secretKey = keyGenerator.generateKey();
            //获得原始对称密钥的字节数组
            byte[] keyBytes = secretKey.getEncoded();
            // key转换,根据字节数组生成AES密钥
            Key key = new SecretKeySpec(keyBytes, "AES");
            return key;
        } catch (NoSuchAlgorithmException e) {
            LOG.error(e.getMessage(),e);
            return null;
        }

    }*/




    /**
     * 将字符串转化为UTF8格式的byte数组
     *
     * @param input the input string
     * @return UTF8 bytes
     */
    private static byte[] getUTF8Bytes(String input) {
        return input.getBytes(StandardCharsets.UTF_8);
    }

}
