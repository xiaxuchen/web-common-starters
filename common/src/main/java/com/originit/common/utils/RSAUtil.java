package com.originit.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author changzhichen
 * @date 2020-04-02 19:36
 */
@Slf4j
public class RSAUtil {
    /**
     * 随机生成密钥对，返回公钥、私钥
     * 
     * @author changzhichen
     * @date 2020-12-16 09:11 
     * @return java.util.Map<java.lang.String,java.lang.Object> 
     **/
    public static Map<String, Object> genKeyPair() {
        try {
            final Map<String, Object> keyMap = new HashMap<>();
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(1024, new SecureRandom());
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 得到私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 得到公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
            // 得到私钥字符串
            String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
            // 将公钥和私钥保存到Map
            //0表示公钥
            keyMap.put("publicKey", publicKeyString);
            //1表示私钥
            keyMap.put("privateKey", privateKeyString);
            log.info("-----publicKey = " + publicKeyString);
            log.info("-----privateKey = " + privateKeyString);
            return keyMap;
        } catch (NoSuchAlgorithmException e) {
            log.error("生成RSA密钥对异常", e);
        }
        return null;
    }

    /**
     * 加密
     *
     * @param message   需要加密的字符串
     * @param publicKey 公钥
     * @return 加密后的字符串
     */
    public static String encrypt(final String message, final String publicKey) {
        //base64编码的公钥
        try {
            final byte[] decoded = Base64.decodeBase64(publicKey);
            final RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            //RSA加密
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            final byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            //字符串长度
            final int len = bytes.length;
            int offset = 0;//偏移量
            int i = 0;//所分的段数
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();

            while (len > offset) {
                byte[] cache;
                if (len - offset > 117) {
                    cache = cipher.doFinal(bytes, offset, 117);
                } else {
                    cache = cipher.doFinal(bytes, offset, len - offset);
                }
                bos.write(cache);
                i++;
                offset = 117 * i;
            }
            bos.close();

            final String encryptMessage = Base64.encodeBase64String(bos.toByteArray());
            return encryptMessage.replaceAll("[\r\n]", "");
        } catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException
                | IllegalBlockSizeException | BadPaddingException | IOException e) {
            log.error("使用公钥对数据加密异常", e);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param message    需要解密的密文
     * @param privateKey 私钥
     * @return 解密后的字符串
     */
    public static String decrypt(String message, final String privateKey) {
        try {
            log.info("message1 = " + message);
            if (message.contains(" ")) {
                log.info("解码前的字符串包含空格");
                message = message.replaceAll(" ", "+");
            }
            log.info("message2 = " + message);
            //base64编码的私钥
            final byte[] decoded = Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            //64位解码加密后的字符串
            final byte[] inputByte = Base64.decodeBase64(message.getBytes(StandardCharsets.UTF_8));

            final int len = inputByte.length;//密文
            int offset = 0;//偏移量
            int i = 0;//段数
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while (len - offset > 0) {
                byte[] cache;
                if (len - offset > 128) {
                    cache = cipher.doFinal(inputByte, offset, 128);
                } else {
                    cache = cipher.doFinal(inputByte, offset, len - offset);
                }
                bos.write(cache);
                i++;
                offset = 128 * i;
            }
            bos.close();

            return new String(bos.toByteArray(), StandardCharsets.UTF_8);
        } catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException
                | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | IOException e) {
            log.error("使用私钥对数据解密异常", e);
        }
        return null;
    }

    public static void main(String[] args) {
        Map<String, Object> map = genKeyPair();
        assert map != null;
        String publicKey = (String) map.get("publicKey");
        String privateKey = (String) map.get("privateKey");

        String content = "xxcisbest";
                //  "http://localhost:8080/a.jpg";

        String encryptContent = encrypt(content, publicKey);
        System.out.println("加密后的字符串：\n" + encryptContent);

        assert encryptContent != null;
        String decryptContent = decrypt(encryptContent, privateKey);
        System.out.println("解密后的字符串：\n" + decryptContent);
    }
}
