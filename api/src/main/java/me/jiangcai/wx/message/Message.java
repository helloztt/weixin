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

/**
 * @author CJ
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
@JacksonXmlRootElement(localName = "xml")
public abstract class Message {

    /**
     * 相关公众账号
     */
    @JsonIgnore
    private PublicAccount account;

    protected Message(MessageType type) {
        this.type = type;
    }

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
    @JacksonXmlCData
    @JsonProperty("MsgType")
    private final MessageType type;


}
