package me.jiangcai.wx.web.mvc;

import me.jiangcai.wx.model.WeixinUserDetail;
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
public class WeixinUserDetailResolver extends AbstractHandler implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == WeixinUserDetail.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer
            , NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return webAuth(webRequest, account -> {
            String openId = currentOpenId(webRequest, account);
            if (openId == null)
                return null;
            return weixinUserService.userInfo(account, openId, WeixinUserDetail.class);
        }, WeixinUserDetail.class);
    }
}
