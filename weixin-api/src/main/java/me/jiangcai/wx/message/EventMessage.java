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

    public EventMessage() {
        super(MessageType.event);
    }
}
