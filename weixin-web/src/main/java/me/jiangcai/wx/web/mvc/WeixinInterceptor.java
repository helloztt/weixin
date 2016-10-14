package me.jiangcai.wx.web.mvc;

import me.jiangcai.wx.PublicAccountSupplier;
import me.jiangcai.wx.couple.WeixinRequestHandlerMapping;
import me.jiangcai.wx.model.PublicAccount;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * @author CJ
 */
@Component
public class WeixinInterceptor implements WebRequestInterceptor {

    public static final String WEIXIN_ENABLED_REQUEST_KEY = "me.jc.weixin.enabled";
    public static final String WEIXIN_ENABLED_MODEL_KEY = "inWeixin";
    private static final Log log = LogFactory.getLog(WeixinInterceptor.class);
    @Autowired
    private PublicAccountSupplier publicAccountSupplier;
    @Autowired
    private WeixinRequestHandlerMapping handlerMapping;

    @Override
    public void preHandle(WebRequest request) throws Exception {
        // 如果是一个微信访问 则根据host获取公众号
        if (isMicroMessenger(request)) {
            request.setAttribute(WEIXIN_ENABLED_REQUEST_KEY, true, RequestAttributes.SCOPE_REQUEST);
            // 是微信访问
            log.debug("comes from Weixin.");
            PublicAccount account = publicAccountSupplier.findByHost(request.getHeader("host"));
            log.debug("public account" + account);
            handlerMapping.updateCurrentAccount(account);
        } else {
            handlerMapping.updateCurrentAccount(null);
            request.setAttribute(WEIXIN_ENABLED_REQUEST_KEY, false, RequestAttributes.SCOPE_REQUEST);
        }
    }

    private boolean isMicroMessenger(WebRequest request) {
        String agent = request.getHeader("user-agent");
        return agent != null && agent.contains("MicroMessenger");
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
        if (model != null)
            model.addAttribute(WEIXIN_ENABLED_MODEL_KEY, isMicroMessenger(request));
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {

    }
}
