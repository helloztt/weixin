package me.jiangcai.wx.protocol.impl.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author CJ
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MediaResult extends BaseResponse {
    private String type;
    @JsonProperty("media_id")
    private String mediaId;
    @JsonProperty("created_at")
    private long createdTime;
}
