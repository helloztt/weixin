package me.jiangcai.wx.message.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 图文条目
 *
 * @author CJ
 */
@Data
@AllArgsConstructor
public class NewsArticle {
    @JacksonXmlCData
    @JsonProperty("Title")
    private String title;
    @JacksonXmlCData
    @JsonProperty("Description")
    private String description;
    @JacksonXmlCData
    @JsonProperty("PicUrl")
    private String imageUrl;
    @JacksonXmlCData
    @JsonProperty("Url")
    private String url;
}
