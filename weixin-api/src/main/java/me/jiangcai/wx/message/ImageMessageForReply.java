package me.jiangcai.wx.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @author CJ
 */
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@ToString(callSuper = true)
public class ImageMessageForReply extends Message {

    @JsonProperty("Image")
    private Image image;

    public ImageMessageForReply(ImageMessage origin) {
        super(MessageType.image);
        setAccount(origin.getAccount());
        setFrom(origin.getFrom());
        setTo(origin.getTo());
        setTime(origin.getTime());
        image = new Image();
        image.mediaId = origin.getMediaId();
    }

    @Override
    protected void putMessageContent(Map<String, Object> data) {

    }

    @Override
    public boolean sameContent(Message message) {
        return false;
    }

    @Data
    private static class Image {
        @JacksonXmlCData
        @JsonProperty("MediaId")
        private String mediaId;
    }
}
