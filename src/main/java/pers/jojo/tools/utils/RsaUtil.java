package pers.jojo.tools.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import pers.jojo.tools.domain.MyKeyPair;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @ClassName RsaUtil
 * @Description
 * @Author 张淳
 * @Date 2020/3/18 14:31
 * @Version 1.0.0
 * https://blog.csdn.net/qy20115549/article/details/83105736
 * https://www.iteye.com/blog/yunlong167167-1997780
 **/
public class RsaUtil {

    /**
     * 生成一组新的公私钥对
     *
     * @return
     * @throws Exception
     */
    public static MyKeyPair createKeyPair() throws Exception {
        MyKeyPair myKeyPair = new MyKeyPair();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        myKeyPair.setPublicKey(Base64.encode(publicKey.getEncoded()));
        myKeyPair.setPrivateKey(Base64.encode(privateKey.getEncoded()));
        return myKeyPair;
    }

    /**
     * 用私钥对信息进行签名
     *
     * @param content           需要签名的内容
     * @param encodedPrivateKey Base64编码后的私钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static String signWithPrivateKey(String content, String encodedPrivateKey) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        //还原私钥
        byte[] decoded = Base64.decode(encodedPrivateKey);
        RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //生成签名
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initSign(privateKey);
        signature.update(content.getBytes("UTF-8"));
        byte[] signBytes = signature.sign();
        return Base64.encode(signBytes);
    }

    /**
     * 用公钥对签名进行校验
     *
     * @param content          被签名的内容（不包括签名本身）
     * @param sign             签名
     * @param encodedPublicKey Base64编码后的公钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static boolean verifyWithPublicKey(String content, String sign, String encodedPublicKey) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        //还原公钥
        byte[] decoded = Base64.decode(encodedPublicKey);
        RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //校验签名
        byte[] signed = Base64.decode(sign);
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initVerify(publicKey);
        signature.update(content.getBytes("UTF-8"));
        return signature.verify(signed);
    }

    /**
     * 用公钥对信息进行加密
     *
     * @param content          需要加密的信息
     * @param encodedPublicKey Base64编码后的公钥
     * @return
     * @throws Exception
     */
    public static String encodeWithPublicKey(String content, String encodedPublicKey) throws Exception {
        //还原公钥
        byte[] decoded = Base64.decode(encodedPublicKey);
        RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encode(cipher.doFinal(content.getBytes("UTF-8")));
    }

    /**
     * 用私钥对信息进行解密
     *
     * @param content           需要解密的信息
     * @param encodedPrivateKey Base64编码后的私钥
     * @return
     * @throws Exception
     */
    public static String decodeWithPrivateKey(String content, String encodedPrivateKey) throws Exception {
        //还原私钥
        byte[] decoded = Base64.decode(encodedPrivateKey);
        RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        byte[] inputByte = Base64.decode(content);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(inputByte));
    }
}