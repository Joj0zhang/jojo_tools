package pers.jojo.tools.utils;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

/**
 * @ClassName WeChatPayUtil
 * @Description 微信支付相关方法
 * @Author 张淳
 * @Date 2020/2/16 19:58
 * @Version 1.0.0
 **/
public class WeChatPayUtil {

    /**
     * 根据Map生成签名sign,内置排序
     *
     * @param parameters
     * @param key        微信商户平台密钥
     * @return
     */
    public static String createSign(SortedMap<String, String> parameters, String key) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        //所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        //System.out.println("签名字符串:" + sb.toString());
        return string2MD5(sb.toString()).toUpperCase();
    }

    public static String string2MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    /**
     * 签名验证
     *
     * @param map
     * @param key 微信商户平台密钥
     * @return
     * @throws Exception
     */
    public boolean checkSign(Map<String, String> map, String key) throws Exception {
        String originalSign = map.get("sign");
        if (originalSign.isEmpty()) {
            return false;
        } else {
            map.remove("sign");
            SortedMap<String, String> sortedMap = new TreeMap<>();
            for (String k : map.keySet()) {
                sortedMap.put(k, map.get(k));
            }
            String newSign = createSign(sortedMap, key);
            return originalSign.equals(newSign);
        }
    }

    /**
     * 生成一个强随机数
     *
     * @return
     */
    public static String createNonceStr() {
        SecureRandom random = new SecureRandom();
        return random.nextInt() + "";
    }
}