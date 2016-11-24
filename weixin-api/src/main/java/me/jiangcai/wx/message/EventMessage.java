package me.jiangcai.wx.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.jiangcai.wx.message.support.WeixinEvent;

/**
 * @author CJ
 */
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@ToString(callSuper = true)
public class EventMessage extends Message {

    @JacksonXmlCData
    @JsonProperty("Event")
    private WeixinEvent event;
    @JacksonXmlCData
    @JsonProperty("EventKey")
    private String key;
    @JacksonXmlCData
    @JsonProperty("Ticket")
    private String ticket;

    // 分享地理信息
    @JsonProperty("Latitude")
    private double latitude;
    @JsonProperty("Longitude")
    private double longitude;
    @JsonProperty("Precision")
    private double precision;
//    <Latitude>23.137466</Latitude>
//    <Longitude>113.352425</Longitude>
//    <Precision>119.385040</Precision>

    public EventMessage() {
        super(MessageType.event);
    }
}
