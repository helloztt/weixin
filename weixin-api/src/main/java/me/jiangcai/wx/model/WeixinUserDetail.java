package me.jiangcai.wx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.jiangcai.wx.OpenId;

import java.util.Locale;

/**
 * 微信用户详情
 * 你可以在Spring MVC Controller参数上直接引入,但必须注意同时不可以有state,code参数
 *
 * @author CJ
 */
@Data
public class WeixinUserDetail {

    private String appId;
    @JsonProperty("openid")
    @OpenId
    private String openId;
    private String nickname;
    @JsonProperty("sex")
    private Gender gender;
    private String province;
    private String city;
    private String country;
    @JsonProperty("headimgurl")
    private String headImageUrl;
    @JsonProperty("language")
    private Locale locale;
    private String[] privilege;
    /**
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
     */
    @JsonProperty("unionid")
    private String unionId;

    /**
     * 将属性从src复制到dist
     *
     * @param dist dist
     * @param src  src
     */
    public static void Copy(WeixinUserDetail dist, WeixinUserDetail src) {
        dist.setAppId(src.getAppId());
        dist.setProvince(src.getProvince());
        dist.setCity(src.getCity());
        dist.setCountry(src.getCountry());
        dist.setGender(src.getGender());
        dist.setHeadImageUrl(src.getHeadImageUrl());
        dist.setLocale(src.getLocale());
        dist.setNickname(src.getNickname());
        dist.setOpenId(src.getOpenId());
        dist.setPrivilege(src.getPrivilege());
        dist.setUnionId(src.getUnionId());
    }


}
