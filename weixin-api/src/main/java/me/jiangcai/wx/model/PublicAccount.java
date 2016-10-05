package me.jiangcai.wx.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * 微信公众帐号
 *
 * @author CJ
 */
@Data
public class PublicAccount {

    // 基本
    private String appID;
    private String appSecret;
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
}