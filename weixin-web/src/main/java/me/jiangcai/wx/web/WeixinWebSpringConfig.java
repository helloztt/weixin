package me.jiangcai.wx.web;

import me.jiangcai.wx.WeixinSpringConfig;
import me.jiangcai.wx.web.mvc.OpenIdArgumentResolver;
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

import java.util.List;

/**
 * 如果要开发微信网页则需要载入该配置 ,
 * <p>
 * 可获得thymeleaf扩展wx
 * <ul>
 * <li>js 可在页面中载入微信JS库以及相关授权 {@link JsProcessor}</li>
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

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(openIdArgumentResolver);
        argumentResolvers.add(weixinUserDetailResolver);
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
