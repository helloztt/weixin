package me.jiangcai.wx.protocol;

import org.apache.http.NameValuePair;

import java.awt.*;

/**
 * 模板参数
 *
 * @author CJ
 */
public interface TemplateParameter extends NameValuePair {

    /**
     * @return 这个参数的颜色
     */
    Color getColor();
}
