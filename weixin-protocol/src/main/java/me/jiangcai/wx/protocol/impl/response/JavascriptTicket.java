package me.jiangcai.wx.protocol.impl.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author CJ
 */
@Data
public class JavascriptTicket {

    @JsonProperty("errcode")
    private int code;
    @JsonProperty("errmsg")
    private String message;

    @JsonProperty("ticket")
    private String token;
    @JsonProperty("expires_in")
    private int time;

}
