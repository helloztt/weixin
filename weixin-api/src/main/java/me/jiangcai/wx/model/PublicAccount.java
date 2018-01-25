package me.jiangcai.wx.model;

import lombok.Data;
import me.jiangcai.wx.PublicAccountSupplier;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * 微信公众帐号
 *
 * @author CJ
 */
@Data
public abstract class PublicAccount {

    // 基本
    private String appID;
    private String appSecret;
    //支付相关
    //商户号
    private String mchID;
    private String apiKey;
    //异步通知地址
    private String notifyURL;
    // 接口
    /**
     * 微信接口配置信息:URL,为了提高效率,规定该URL必须以斜杠结尾!
     */
    private String interfaceURL;
    private String interfaceToken;
    // 句柄信息
    private String accessToken;
    private LocalDateTime timeToExpire;
    //
    /**
     * jsapi_ticket
     */
    private String javascriptTicket;
    private LocalDateTime javascriptTimeToExpire;

    public Locale getLocale() {
        return Locale.CHINA;
    }

    /**
     * @return 跟自身相关的供应商
     */
    public abstract PublicAccountSupplier getSupplier();
}
