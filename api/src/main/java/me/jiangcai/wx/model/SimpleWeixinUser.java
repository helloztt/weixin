package me.jiangcai.wx.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 一个实现的示范,并没有实际价值
 *
 * @author CJ
 */
@Data
public class SimpleWeixinUser implements WeixinUser {

    private String openId;
    private String accessToken;
    private LocalDateTime accessTimeToExpire;
    private String refreshToken;
    private String tokenScopes;


}
