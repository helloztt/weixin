package me.jiangcai.wx.model.pay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author helloztt
 */
@Data
public class OrderInfoForJs {
    @JsonProperty("appId")
    private String appId;
    @JsonProperty("timeStamp")
    private String timeStamp;
    @JsonProperty("nonceStr")
    private String nonceStr;
    @JsonProperty("package")
    private String prepayId;
    @JsonProperty("signType")
    private String signType;
    @JsonProperty("paySign")
    private String paySign;

}
