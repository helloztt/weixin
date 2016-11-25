package me.jiangcai.wx.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author CJ
 */
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@ToString(callSuper = true)
public class TextMessage extends Message {

    @JacksonXmlCData
    @JsonProperty("Content")
    private String content;

    public TextMessage(String content) {
        super(MessageType.text);
        setContent(content);
    }

    public TextMessage() {
        super(MessageType.text);
    }

    @Override
    protected void putMessageContent(Map<String, Object> data) {
        Map<String, Object> text = new HashMap<>();
        text.put("content", content);
        data.put("text", text);
    }

    @Override
    public boolean sameContent(Message message) {
        if (!(message instanceof TextMessage)) {
            return false;
        }
        TextMessage eventMessage = (TextMessage) message;
        return Objects.equals(content, eventMessage.content);
    }
}
