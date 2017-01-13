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
import lombok.SneakyThrows;
import lombok.ToString;
import me.jiangcai.wx.converter.LocalDateTimeDeserializer;
import me.jiangcai.wx.converter.LocalDateTimeSerializer;
import me.jiangcai.wx.model.PublicAccount;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CJ
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
@JacksonXmlRootElement(localName = "xml")
public abstract class Message implements Cloneable {

    @JacksonXmlCData
    @JsonProperty("MsgType")
    private final MessageType type;
    /**
     * 相关公众账号
     */
    @JsonIgnore
    private PublicAccount account;
    @JsonProperty(value = "MsgID")
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
     */
    public Map<String, Object> sendTo() {
        Map<String, Object> toPost = new HashMap<>();
        toPost.put("touser", getTo());
        toPost.put("msgtype", type.name());
        putMessageContent(toPost);
        return toPost;
    }

    protected abstract void putMessageContent(Map<String, Object> data);

    /**
     * @param message
     * @return true 当前消息内容和参数一致
     */
    public abstract boolean sameContent(Message message);

    @Override
    @SneakyThrows(CloneNotSupportedException.class)
    public Message clone() {
        return (Message) super.clone();
    }
}
