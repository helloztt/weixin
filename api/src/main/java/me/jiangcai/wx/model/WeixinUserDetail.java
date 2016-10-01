package me.jiangcai.wx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.jiangcai.wx.OpenId;

/**
 * 微信用户详情
 *
 * @author CJ
 */
@Data
public class WeixinUserDetail {

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
    private String[] privilege;
    /**
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
     */
    @JsonProperty("unionid")
    private String unionId;


}
