package me.jiangcai.wx.web;

import me.jiangcai.wx.WeixinSpringConfig;
import me.jiangcai.wx.web.mvc.OpenIdArgumentResolver;
import me.jiangcai.wx.web.mvc.WeixinEnvironmentResolver;
import me.jiangcai.wx.web.mvc.WeixinInterceptor;
import me.jiangcai.wx.web.mvc.WeixinUserDetailResolver;
import me.jiangcai.wx.web.thymeleaf.JsProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

/**
 * 如果要开发微信网页则需要载入该配置 ,
 * <p>
 * 可获得thymeleaf扩展wx
 * <ul>
 * <li>js 可在页面中载入微信JS库以及相关授权 {@link JsProcessor}</li>
 * <li>share 可在页面中定义自身分享时的信息以及相关前端回调 {@link me.jiangcai.wx.web.thymeleaf.ShareProcessor}</li>
 * </ul>
 *
 * @author CJ
 */
@Configuration
@ComponentScan("me.jiangcai.wx.web.thymeleaf")
@Import(WeixinWebSpringConfig.MVCConfig.class)
public class WeixinWebSpringConfig extends WeixinSpringConfig {

    @Autowired
    private OpenIdArgumentResolver openIdArgumentResolver;
    @Autowired
    private WeixinUserDetailResolver weixinUserDetailResolver;
    @Autowired
    private WeixinEnvironmentResolver weixinEnvironmentResolver;

    /**
     * @param request 请求
     * @return 是否为微信浏览器发起的请求
     */
    public static boolean isWeixinRequest(HttpServletRequest request) {
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            if (name.equalsIgnoreCase("user-agent")) {
                return request.getHeader(name).contains("MicroMessenger");
            }
        }
        return false;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(openIdArgumentResolver);
        argumentResolvers.add(weixinUserDetailResolver);
        argumentResolvers.add(weixinEnvironmentResolver);
    }

    @EnableWebMvc
    @ComponentScan("me.jiangcai.wx.web.mvc")
    static class MVCConfig extends WebMvcConfigurerAdapter {
        @Autowired
        private WeixinInterceptor weixinInterceptor;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            super.addInterceptors(registry);
            registry.addWebRequestInterceptor(weixinInterceptor);
        }
    }

}
