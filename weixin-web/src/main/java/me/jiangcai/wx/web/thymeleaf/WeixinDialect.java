package me.jiangcai.wx.web.thymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.HashSet;
import java.util.Set;

/**
 * @author CJ
 */
@Component
public class WeixinDialect implements IDialect, IProcessorDialect {

    private final Set<IProcessor> processors;

    @Autowired
    public WeixinDialect(Set<WeixinProcessor> weixinProcessors) {
        processors = new HashSet<>();
        processors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, getPrefix()));
        weixinProcessors.forEach(processors::add);
    }

    static boolean Support(ITemplateContext context) {
        if (context instanceof IWebContext) {
            IWebContext web = (IWebContext) context;
            final String header = web.getRequest().getHeader("user-agent");
            return header != null && header.contains("MicroMessenger");
        }
        return true;
    }

    @Override
    public String getName() {
        return "Weixin Dialect";
    }

    @Override
    public String getPrefix() {
        return "wx";
    }

    @Override
    public int getDialectProcessorPrecedence() {
        return 0;
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return processors;
    }
}
