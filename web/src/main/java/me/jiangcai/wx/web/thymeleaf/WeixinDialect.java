package me.jiangcai.wx.web.thymeleaf;

import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.Set;

/**
 * @author CJ
 */
public class WeixinDialect implements IDialect,IProcessorDialect {

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
        return null;
    }
}
