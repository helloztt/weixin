package me.jiangcai.wx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author CJ
 */
@Data
public class TemplateList {
    @JsonProperty("template_list")
    private List<Template> list;
}
