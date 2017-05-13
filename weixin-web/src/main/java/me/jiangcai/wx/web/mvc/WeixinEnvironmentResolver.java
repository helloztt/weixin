package me.jiangcai.wx.web.mvc;

import me.jiangcai.wx.web.WeixinEnvironment;
import me.jiangcai.wx.web.WeixinWebSpringConfig;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CJ
 */
@Component
public class WeixinEnvironmentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(WeixinEnvironment.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer
            , NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        boolean result = WeixinWebSpringConfig.isWeixinRequest(webRequest.getNativeResponse(HttpServletRequest.class));
        // 如果是
        if (parameter.getParameterType() == String.class)
            return result ? "true" : "false";
        return result;
    }
}
