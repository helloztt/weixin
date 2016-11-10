package me.jiangcai.wx.model.message;

import java.awt.*;
import java.text.MessageFormat;

/**
 * 模板消息参数
 *
 * @author CJ
 */
public class SimpleTemplateMessageParameter implements TemplateMessageParameter {
    /**
     *
     */
    private String name;
    /**
     * #173177
     */
    private Color defaultColor;

    private String pattern;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Color getDefaultColor() {
        return defaultColor;
    }

    @Override
    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    @Override
    public MessageFormat getFormat() {
        return new MessageFormat(pattern);
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
