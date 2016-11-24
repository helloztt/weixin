package me.jiangcai.wx.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.jiangcai.wx.converter.LocalDateTimeDeserializer;
import me.jiangcai.wx.converter.LocalDateTimeSerializer;
import me.jiangcai.wx.model.PublicAccount;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author CJ
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
@JacksonXmlRootElement(localName = "xml")
public abstract class Message {

    @JacksonXmlCData
    @JsonProperty("MsgType")
    private final MessageType type;
    /**
     * 相关公众账号
     */
    @JsonIgnore
    private PublicAccount account;
    @JsonProperty(value = "MsgId", access = JsonProperty.Access.WRITE_ONLY)
    private String id;
    @JacksonXmlCData
    @JsonProperty("ToUserName")
    private String to;
    @JacksonXmlCData
    @JsonProperty("FromUserName")
    private String from;
    @JsonProperty("CreateTime")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime time;

    protected Message(MessageType type) {
        this.type = type;
    }


    /**
     * 这条消息将作为客服消息发送,将字段填入这个Map中
     *
     * @param data 目标Map
     */
    public void sendTo(Map<String, Object> data) {

    }
}
