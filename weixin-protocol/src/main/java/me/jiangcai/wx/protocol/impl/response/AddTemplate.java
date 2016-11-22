package me.jiangcai.wx.protocol.impl.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author CJ
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddTemplate extends BaseResponse {
    @JsonProperty("template_id")
    private String templateId;
}
