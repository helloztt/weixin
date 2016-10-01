package me.jiangcai.wx.model;

/**
 * 微信用户
 * 并不是关注了某一个公众账号的,但肯定是公众帐号相关的
 *
 * @author CJ
 */
public interface WeixinUser {
    /**
     * @return 就相关公众号而已是唯一的
     */
    String getOpenId();

    void setOpenId(String openId);

    /**
     * @return 访问这个用户详情的token, 这个跟公众号token完全不是一个东西
     */
    String getAccessToken();

    void setAccessToken(String accessToken);

    /**
     * @return 何时这个token过期
     */
    java.time.LocalDateTime getAccessTimeToExpire();

    void setAccessTimeToExpire(java.time.LocalDateTime accessTimeToExpire);

    String getRefreshToken();

    void setRefreshToken(String refreshToken);

    /**
     * @return 这个token的使用范围
     */
    String getTokenScopes();

    void setTokenScopes(String tokenScopes);


}
