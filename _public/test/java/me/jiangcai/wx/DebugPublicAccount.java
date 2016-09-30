package me.jiangcai.wx;

import me.jiangcai.wx.model.PublicAccount;

/**
 * @author CJ
 */
public class DebugPublicAccount extends PublicAccount {

    public DebugPublicAccount() {
        setAppID("wx59b0162cdf0967af");
        setAppSecret("ffcf655fce7c4175bbddae7b594c4e27");
        setInterfaceURL("http://localhost/");
        setInterfaceToken("jiangcai");
    }
}
