package me.jiangcai.wx.web.thymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
