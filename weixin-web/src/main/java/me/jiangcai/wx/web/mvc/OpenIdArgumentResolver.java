package me.jiangcai.wx.web.mvc;

import me.jiangcai.wx.OpenId;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author CJ
 */
@Component
public class OpenIdArgumentResolver extends AbstractHandler implements HandlerMethodArgumentResolver {

    private static final Log log = LogFactory.getLog(OpenIdArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(OpenId.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer
            , NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return webAuth(webRequest, account -> currentOpenId(webRequest, account), String.class);
    }

}
