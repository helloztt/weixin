package me.jiangcai.wx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信信息模板
 *
 * @author CJ
 */
@Data
public class Template {
    private final static Pattern parameterPattern = Pattern.compile("\\{\\{([0-9a-zA-Z]+)\\.DATA}}");
    @JsonProperty("template_id")
    private String id;
    private String title;
    private String primary_industry;
    private String deputy_industry;
    private String content;
    private String example;

    // 通过研读content 可以解析出参数
    public List<String> parameters() {
        Matcher matcher = parameterPattern.matcher(content);
        ArrayList<String> arrayList = new ArrayList<>();

        while (matcher.find()) {
            arrayList.add(matcher.group(1));
        }

        return arrayList;
    }
}
