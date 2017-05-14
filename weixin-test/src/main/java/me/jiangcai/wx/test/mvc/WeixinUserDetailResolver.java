package me.jiangcai.wx.test.mvc;

import me.jiangcai.wx.couple.WeixinRequestHandlerMapping;
import me.jiangcai.wx.model.PublicAccount;
import me.jiangcai.wx.model.WeixinUserDetail;
import me.jiangcai.wx.standard.entity.StandardWeixinUser;
import me.jiangcai.wx.standard.repository.StandardWeixinUserRepository;
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
public class WeixinUserDetailResolver extends me.jiangcai.wx.web.mvc.WeixinUserDetailResolver {

    private static final String DetailKey = "m.j.w.detail";
    private final WeixinUserMocker mocker;
    @Autowired
    private WeixinRequestHandlerMapping mapping;
    @Autowired(required = false)
    private StandardWeixinUserRepository standardWeixinUserRepository;

    public WeixinUserDetailResolver(WeixinUserMocker mocker) {
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
            WeixinUserDetail detail = (WeixinUserDetail) session.getAttribute(DetailKey + account.getAppID());
            if (detail != null) {
                String openId = (String) session.getAttribute(SK_Prefix_OpenID + account.getAppID());
                if (!StringUtils.isEmpty(openId))
                    detail.setOpenId(openId);
                else
                    session.setAttribute(SK_Prefix_OpenID + account.getAppID(), detail.getOpenId());
                return saveDetail(detail);
            }
//            if (!StringUtils.isEmpty(openId)) {
//                WeixinUserDetail endValue = weixinUserService.userInfo(account, openId, WeixinUserDetail.class, webRequest);
//                if (endValue != null)
//                    return endValue;
//            }
        }
        final WeixinUserDetail weixinUserDetail = mocker.mockUser(mavContainer, webRequest);
        if (session != null) {
            session.setAttribute(DetailKey + account.getAppID(), weixinUserDetail);
            String openId = (String) session.getAttribute(SK_Prefix_OpenID + account.getAppID());
            if (!StringUtils.isEmpty(openId))
                weixinUserDetail.setOpenId(openId);
            else
                session.setAttribute(SK_Prefix_OpenID + account.getAppID(), weixinUserDetail.getOpenId());
        }
        return saveDetail(weixinUserDetail);
    }

    private Object saveDetail(WeixinUserDetail detail) {
        // 如果采用了标准方案 则应当自动储存
        if (standardWeixinUserRepository == null)
            return detail;
        StandardWeixinUser user = standardWeixinUserRepository.findByOpenId(detail.getOpenId());
        if (user != null)
            return user;
        user = new StandardWeixinUser();
        WeixinUserDetail.Copy(user, detail);
        return standardWeixinUserRepository.save(user);
    }
}
