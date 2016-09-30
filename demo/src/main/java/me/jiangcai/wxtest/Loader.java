package me.jiangcai.wxtest;

import me.jiangcai.wx.couple.debug.DebugFilter;
import me.jiangcai.wxtest.config.MyConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * @author CJ
 */
public class Loader extends AbstractAnnotationConfigDispatcherServletInitializer {
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{MyConfig.class};
    }

    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[0];
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{
                new DebugFilter()
        };
    }

    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
