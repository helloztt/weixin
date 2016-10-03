package me.jiangcai.wx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 微信信息模板
 *
 * @author CJ
 */
@Data
public class Template {
    @JsonProperty("template_id")
    private String id;
    private String title;
    private String primary_industry;
    private String deputy_industry;
    private String content;
    private String example;
}
