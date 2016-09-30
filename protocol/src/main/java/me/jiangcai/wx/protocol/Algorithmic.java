package me.jiangcai.wx.protocol;

import me.jiangcai.wx.model.PublicAccount;
import org.springframework.security.crypto.codec.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author CJ
 */
public class Algorithmic {


    /**
     * 微信公众号来源身份检查
     *
     * @param publicAccount
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static boolean interfaceCheck(PublicAccount publicAccount, String signature, String timestamp, String nonce) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        List<String> data = Arrays.asList(publicAccount.getInterfaceToken(), timestamp, nonce);
        Collections.sort(data);
        String result = String.join("", data);
        byte[] resultByte = result.getBytes("UTF-8");
        MessageDigest messageDigest = MessageDigest.getInstance("sha1");
        messageDigest.update(resultByte);
        return Arrays.equals(messageDigest.digest(), Hex.decode(signature));
    }
}
