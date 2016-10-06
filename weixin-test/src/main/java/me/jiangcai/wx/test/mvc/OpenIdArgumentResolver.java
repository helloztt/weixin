package me.jiangcai.wx.test.mvc;

import me.jiangcai.wx.couple.WeixinRequestHandlerMapping;
import me.jiangcai.wx.model.PublicAccount;
import me.jiangcai.wx.test.WeixinUserMocker;
import me.jiangcai.wx.web.exception.NoWeixinClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author CJ
 */
public class OpenIdArgumentResolver extends me.jiangcai.wx.web.mvc.OpenIdArgumentResolver {

    private final WeixinUserMocker mocker;
    @Autowired
    private WeixinRequestHandlerMapping mapping;

    public OpenIdArgumentResolver(WeixinUserMocker mocker) {
        super();
        this.mocker = mocker;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer
            , NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        PublicAccount account = mapping.currentPublicAccount();
        if (account == null)
            throw new NoWeixinClientException();

        HttpSession session = webRequest.getNativeRequest(HttpServletRequest.class).getSession();

        if (session != null) {
            String openId = (String) session.getAttribute(SK_Prefix_OpenID + account.getAppID());
            if (!StringUtils.isEmpty(openId)) {
                return openId;
            }
        }

        final String openId = mocker.mockUser(mavContainer, webRequest).getOpenId();
        if (session != null) {
            session.setAttribute(SK_Prefix_OpenID + account.getAppID(), openId);
        }
        return openId;
    }
}
