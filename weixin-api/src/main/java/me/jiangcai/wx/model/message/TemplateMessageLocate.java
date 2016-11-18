package me.jiangcai.wx.model.message;

/**
 * @author CJ
 */
public interface TemplateMessageLocate {
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
}
