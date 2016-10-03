package me.jiangcai.wx.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.jiangcai.wx.message.support.NewsArticle;

/**
 * @author CJ
 */
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@ToString(callSuper = true)
public class NewsMessage extends Message {

    @JsonProperty("ArticleCount")
    private final int count;
//    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlElementWrapper(localName = "Articles")
    @JacksonXmlProperty(localName = "item")
    private final NewsArticle[] articles;

    public NewsMessage(NewsArticle... articles) {
        super(MessageType.news);
        count = articles.length;
        this.articles = articles;
    }
}
