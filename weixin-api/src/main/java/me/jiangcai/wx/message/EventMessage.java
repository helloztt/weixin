package me.jiangcai.wx.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.jiangcai.wx.message.support.WeixinEvent;

import java.util.Map;
import java.util.Objects;

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

    @JsonProperty("MenuId")
    private String menuId;
    @JsonProperty("Status")
    private String status;

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

    @Override
    protected void putMessageContent(Map<String, Object> data) {
        throw new IllegalArgumentException("EventMessage can not be send.");
    }

    @Override
    public boolean sameContent(Message message) {
        if (!(message instanceof EventMessage)) {
            return false;
        }
        EventMessage eventMessage = (EventMessage) message;
        return Objects.equals(event, eventMessage.event)
                && Objects.equals(key, eventMessage.key)
                && Objects.equals(ticket, eventMessage.ticket)
                && Objects.equals(latitude, eventMessage.latitude)
                && Objects.equals(longitude, eventMessage.longitude)
                && Objects.equals(precision, eventMessage.precision);
    }
}
