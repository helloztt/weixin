package me.jiangcai.wx.protocol.impl.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author CJ
 */
@Data
public class AddTemplate {
    @JsonProperty("template_id")
    private String templateId;
}
