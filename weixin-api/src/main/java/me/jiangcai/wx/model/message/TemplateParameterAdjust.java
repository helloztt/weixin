package me.jiangcai.wx.model.message;

import java.awt.*;

/**
 * @author CJ
 */
public interface TemplateParameterAdjust {
    /**
     * @param parameter
     * @param arguments
     * @return 指定参数的颜色
     */
    Color color(TemplateMessageParameter parameter, Object[] arguments);
}
