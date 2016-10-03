package me.jiangcai.wx.test.mvc;

import me.jiangcai.wx.test.WeixinUserMocker;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author CJ
 */
public class WeixinUserDetailResolver extends me.jiangcai.wx.web.mvc.WeixinUserDetailResolver {

    private final WeixinUserMocker mocker;

    public WeixinUserDetailResolver(WeixinUserMocker mocker) {
        super();
        this.mocker = mocker;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return mocker.mockUser(mavContainer, webRequest);
    }
}
