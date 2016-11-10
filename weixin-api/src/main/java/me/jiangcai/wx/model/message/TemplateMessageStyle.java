package me.jiangcai.wx.model.message;

import java.util.Collection;

/**
 * 模板消息的样式,是一个业务方向的消息
 *
 * @author CJ
 */
public interface TemplateMessageStyle {
    /**
     * @return 模板库中模板的编号，有“TM**”和“OPENTMTM**”等形式
     */
    String getTemplateIdShort();

    /**
     * @return 模板标题
     */
    String getTemplateTitle();

    /**
     * @return 消息所属行业
     */
    String getIndustryId();

    String getTemplateId();

    void setTemplateId(String templateId);

    Collection<TemplateMessageParameter> parameterStyles();
    // 格式是这边固定的
    // 颜色也提供了默认的
    // 此处的格式也是一个模板
    // 各种情况可以改变颜色呢?
    //

}
