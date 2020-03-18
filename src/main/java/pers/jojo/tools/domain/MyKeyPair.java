package pers.jojo.tools.domain;

/**
 * @ClassName MyKeyPair
 * @Description
 * @Author 张淳
 * @Date 2020/3/18 14:54
 * @Version 1.0.0
 **/
public class MyKeyPair {

    /**
     * Base64编码后的公钥
     */
    private String publicKey;
    /**
     * Base64编码后的私钥
     */
    private String privateKey;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}