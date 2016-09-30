package me.jiangcai.wx.web.thymeleaf;

import org.thymeleaf.standard.processor.AbstractStandardFragmentInsertionTagProcessor;
import org.thymeleaf.standard.processor.StandardReplaceTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author CJ
 * @see StandardReplaceTagProcessor
 */
public abstract class JsProcessor extends AbstractStandardFragmentInsertionTagProcessor implements WeixinProcessor {

    public JsProcessor() {
        super(TemplateMode.HTML, "wx", "js", 100, true);
    }
}
