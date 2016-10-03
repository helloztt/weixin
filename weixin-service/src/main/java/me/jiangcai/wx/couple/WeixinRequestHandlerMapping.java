package me.jiangcai.wx.couple;

import me.jiangcai.wx.PublicAccountSupplier;
import me.jiangcai.wx.couple.debug.Debug;
import me.jiangcai.wx.model.PublicAccount;
import me.jiangcai.wx.protocol.Algorithmic;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author CJ
 */
@Component
public class WeixinRequestHandlerMapping implements HandlerMapping {

    private static final Log log = LogFactory.getLog(WeixinRequestHandlerMapping.class);
    private final ThreadLocal<PublicAccount> publicAccountThreadLocal = new ThreadLocal<>();
    @Autowired(required = false)
    private PublicAccountSupplier publicAccountSupplier;
    @Autowired
    private WeixinRequestHandler weixinRequestHandler;
    @Autowired(required = false)
    private Debug debug;
    @Autowired
    private Environment environment;
    private HandlerMethod helloHandler;
    private HandlerMethod receiveHandler;

    public PublicAccount currentPublicAccount() {
        return publicAccountThreadLocal.get();
    }

    @PostConstruct
    public void init() throws NoSuchMethodException {

        for (Method method : weixinRequestHandler.getClass().getMethods()) {
            if (method.getName().equalsIgnoreCase("hello")) {
                helloHandler = new HandlerMethod(weixinRequestHandler, method);
            } else if (method.getName().equalsIgnoreCase("receive")) {
                receiveHandler = new HandlerMethod(weixinRequestHandler, method);
            }
        }
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        if (publicAccountSupplier == null) {
            throw new IllegalStateException("you should declare a PublicAccountSupplier bean in context.");
        }

        StringBuilder urlBuilder = new StringBuilder(request.getScheme());
        urlBuilder.append("://").append(request.getServerName());
        if (!((request.getServerPort() == 80 && request.getScheme().equalsIgnoreCase("http"))
                || (request.getServerPort() == 443 && request.getScheme().equalsIgnoreCase("https")))) {
            urlBuilder.append(":").append(request.getServerPort());
        }

        urlBuilder.append(request.getRequestURI());
        if (!urlBuilder.toString().endsWith("/"))
            urlBuilder.append("/");

        String url = urlBuilder.toString();

        log.debug("request URL:" + url);

        for (PublicAccount publicAccount : publicAccountSupplier.getAccounts()) {
            log.debug("account URL:" + publicAccount.getInterfaceURL());
            if (url.equals(publicAccount.getInterfaceURL())) {
                publicAccountThreadLocal.set(publicAccount);
                // 可以返回 HandlerMethod
                if (request.getMethod().equalsIgnoreCase("GET"))
                    return new HandlerExecutionChain(helloHandler, interceptors());
                // 如果是其他请求 也携带有

                // signature,timestamp,nonce,openid 发送者
                String signature = request.getParameter("signature");
                String timestamp = request.getParameter("timestamp");
                String nonce = request.getParameter("nonce");
                String openId = request.getParameter("openid");

                //
                if (!Algorithmic.interfaceCheck(publicAccount, signature, timestamp, nonce)) {
                    return null;
                }
                return new HandlerExecutionChain(receiveHandler, interceptors());
            }
        }
        return null;
    }

    private HandlerInterceptor[] interceptors() {
        if (debug != null && Debug.work(environment))
            return new HandlerInterceptor[]{debug};
        return null;
    }

    public void updateCurrentAccount(PublicAccount account) {
        publicAccountThreadLocal.set(account);
    }
}
