package me.jiangcai.wx.protocol.impl.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author CJ
 */
@Data
public class ErrorResponse {
    @JsonProperty("errcode")
    private int code;
    @JsonProperty("errmsg")
    private String message;
}
