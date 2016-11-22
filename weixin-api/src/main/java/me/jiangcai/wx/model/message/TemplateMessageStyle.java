package me.jiangcai.wx.model.message;

import java.util.Collection;

/**
 * 模板消息的样式,是一个业务方向的消息
 *
 * @author CJ
 */
public interface TemplateMessageStyle extends TemplateMessageLocate {

    Collection<? extends TemplateMessageParameter> parameterStyles();
    // 格式是这边固定的
    // 颜色也提供了默认的
    // 此处的格式也是一个模板
    // 各种情况可以改变颜色呢?
    //

}
