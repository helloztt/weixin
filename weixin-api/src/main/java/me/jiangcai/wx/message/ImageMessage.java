package me.jiangcai.wx.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CJ
 */
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@ToString(callSuper = true)
public class ImageMessage extends Message {

    @JacksonXmlCData
    @JsonProperty("PicUrl")
    private String imageUrl;

    @JacksonXmlCData
    @JsonProperty("MediaId")
    private String mediaId;

    public ImageMessage() {
        super(MessageType.image);
    }

    @Override
    protected void putMessageContent(Map<String, Object> data) {
        Map<String, Object> image = new HashMap<>();
        image.put("MediaId", mediaId);
        data.put("Image", image);
    }

    @Override
    public boolean sameContent(Message message) {
        return false;
    }
}
