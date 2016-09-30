package me.jiangcai.wx.protocol.impl.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author CJ
 */
@Data
public class AccessToken {
    @JsonProperty("access_token")
    private String token;
    @JsonProperty("expires_in")
    private int time;
}
