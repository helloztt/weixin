package me.jiangcai.wx.protocol.impl.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author CJ
 */
@Data
public class UserAccessResponse {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private int time;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("openid")
    private String openId;
    private String[] scope;

}
