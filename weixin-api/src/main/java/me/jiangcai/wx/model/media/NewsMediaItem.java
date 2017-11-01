package me.jiangcai.wx.model.media;

import lombok.Data;
import me.jiangcai.wx.message.support.NewsArticle;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author CJ
 */
@Data
public class NewsMediaItem extends MediaItem {
    private Map<String, Object> content;


    @SuppressWarnings("unchecked")
    public List<NewsArticle> getNews() {
        List<Map<String, Object>> originList = (List<Map<String, Object>>) content.get("news_item");
        return originList.stream()
                .map(map -> new NewsArticle(
                        map.get("title").toString()
                        , null, null
                        , map.get("url").toString()
                ))
                .collect(Collectors.toList());
    }

}
