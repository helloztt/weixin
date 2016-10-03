package me.jiangcai.wx.test.mvc;

import me.jiangcai.wx.test.WeixinUserMocker;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author CJ
 */
public class OpenIdArgumentResolver extends me.jiangcai.wx.web.mvc.OpenIdArgumentResolver {


    private final WeixinUserMocker mocker;

    public OpenIdArgumentResolver(WeixinUserMocker mocker) {
        super();
        this.mocker = mocker;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return mocker.mockUser(mavContainer, webRequest).getOpenId();
    }
}
