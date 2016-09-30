package me.jiangcai.wx;

import me.jiangcai.wx.couple.WeixinRequestHandlerMapping;
import me.jiangcai.wx.couple.debug.Debug;
import me.jiangcai.wx.model.PublicAccount;
import me.jiangcai.wx.protocol.Protocol;
import me.jiangcai.wx.protocol.converter.MessageConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * 出于必要,应该提供一个{@link PublicAccountSupplier 公众号提供者}
 *
 * @author CJ
 */
@SuppressWarnings("WeakerAccess")
@Configuration
@ComponentScan("me.jiangcai.wx.couple")
@EnableWebMvc
public class WeixinSpringConfig extends WebMvcConfigurerAdapter {

    private static final Log log = LogFactory.getLog(WeixinSpringConfig.class);

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(new MessageConverter());
    }

    @Autowired
    private WeixinRequestHandlerMapping weixinRequestHandlerMapping;
    @Autowired(required = false)
    private Debug debug;
    @Autowired
    private Environment environment;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        super.addInterceptors(registry);
//        if (debug != null && Debug.work(environment))
//            registry.addInterceptor(debug);
//    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(0, new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterType() == PublicAccount.class;
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer
                    , NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
                return weixinRequestHandlerMapping.currentPublicAccount();
            }
        });
        argumentResolvers.add(0, new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterType() == Protocol.class;
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer
                    , NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
                PublicAccount publicAccount = weixinRequestHandlerMapping.currentPublicAccount();
                if (publicAccount == null)
                    return null;
                return Protocol.forAccount(publicAccount);
            }
        });
    }
}
