package me.jiangcai.wx.protocol.impl.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author CJ
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccessToken extends BaseResponse {
    @JsonProperty("access_token")
    private String token;
    @JsonProperty("expires_in")
    private int time;
}
