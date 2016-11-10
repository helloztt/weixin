package me.jiangcai.wx.model.message;

import java.awt.*;
import java.text.MessageFormat;

/**
 * 模板消息的参数格式
 *
 * @author CJ
 */
public interface TemplateMessageParameter {
    String getName();

    void setName(String name);

    Color getDefaultColor();

    void setDefaultColor(Color defaultColor);

    MessageFormat getFormat();

    String getPattern();

    void setPattern(String pattern);
}
