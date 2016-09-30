package me.jiangcai.wx.couple.debug;

import org.springframework.core.env.Environment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author CJ
 */
public class DebugFilter extends AbstractRequestLoggingFilter {

    private Boolean ableToWork;
    private Debug debug;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // able to work?
        if (ableToWork == null) {
            final WebApplicationContext webApplicationContext = WebApplicationContextUtils.findWebApplicationContext(this.getServletContext());
            Environment environment = webApplicationContext.getBean(Environment.class);
            debug = webApplicationContext.getBean(Debug.class);
            ableToWork = Debug.work(environment);
        }

        if (ableToWork) {
            DebugRequest request1 = new DebugRequest(request);
            try {
                filterChain.doFilter(request1, response);
            } finally {
                try {
                    debug.postHandle(request1, response, null, null);
                } catch (Exception e) {
                    throw new ServletException(e);
                }
            }
        } else
            filterChain.doFilter(request, response);
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {

    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
    }
}
