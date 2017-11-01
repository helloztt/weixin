package me.jiangcai.wx.model.media;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import me.jiangcai.wx.converter.LocalDateTimeDeserializer;
import me.jiangcai.wx.converter.LocalDateTimeSerializer;

import java.time.LocalDateTime;

/**
 * 素材
 *
 * @author CJ
 */
@Data
public class MediaItem {
    @JsonProperty("media_id")
    private String id;
    @JsonProperty("update_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

}
