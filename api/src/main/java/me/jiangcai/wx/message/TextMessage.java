package me.jiangcai.wx.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author CJ
 */
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@ToString(callSuper = true)
public class TextMessage extends Message {

    public TextMessage(String content) {
        super(MessageType.text);
        setContent(content);
    }

    public TextMessage() {
        super(MessageType.text);
    }

    @JacksonXmlCData
    @JsonProperty("Content")
    private String content;
}
