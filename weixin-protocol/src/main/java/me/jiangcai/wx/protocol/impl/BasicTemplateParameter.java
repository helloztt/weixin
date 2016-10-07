package me.jiangcai.wx.protocol.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.jiangcai.wx.protocol.TemplateParameter;
import org.apache.http.message.BasicNameValuePair;

import java.awt.*;

/**
 * @author CJ
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BasicTemplateParameter extends BasicNameValuePair implements TemplateParameter {

    private Color color;


    public BasicTemplateParameter(String name, String value) {
        super(name, value);
    }
}
