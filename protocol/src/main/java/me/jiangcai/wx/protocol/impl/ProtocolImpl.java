package me.jiangcai.wx.protocol.impl;

import me.jiangcai.wx.model.Menu;
import me.jiangcai.wx.model.PublicAccount;
import me.jiangcai.wx.protocol.Protocol;
import me.jiangcai.wx.protocol.exception.ClientException;
import me.jiangcai.wx.protocol.exception.ProtocolException;
import me.jiangcai.wx.protocol.impl.handler.AccessTokenHandler;
import me.jiangcai.wx.protocol.impl.handler.VoidHandler;
import me.jiangcai.wx.protocol.impl.handler.WeixinResponseHandler;
import me.jiangcai.wx.protocol.impl.response.AccessToken;
import me.jiangcai.wx.protocol.impl.response.JavascriptTicket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.security.crypto.codec.Hex;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

/**
 * @author CJ
 */
class ProtocolImpl implements Protocol {

    private static final Log log = LogFactory.getLog(ProtocolImpl.class);

    private final PublicAccount account;
    private final CloseableHttpClient client;

    ProtocolImpl(PublicAccount account) {
        this.account = account;
        client = HttpClientBuilder.create()
                .build();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        client.close();
    }

    @Override
    public void getJavascriptTicket() throws ProtocolException {
        HttpGet tokenGet = newGet("/ticket/getticket", new BasicNameValuePair("type", "jsapi"));
        try {
            JavascriptTicket token = client.execute(tokenGet, new WeixinResponseHandler<>(JavascriptTicket.class));

            if (token.getCode() != 0)
                throw new ProtocolException(token.getMessage());

            LocalDateTime time = LocalDateTime.now();
            time.plusSeconds(token.getTime());
            account.setJavascriptTimeToExpire(time);
            account.setJavascriptTicket(token.getToken());
        } catch (IOException ex) {
            throw new ClientException(ex);
        }
    }

    @Override
    public void createMenu(Menu[] menus) throws ProtocolException {
        HttpPost postMenu = newPost("/menu/create");

        HttpEntity entity = EntityBuilder.create()
                .setText(Menu.toContent(menus))
                .build();

        postMenu.setEntity(entity);

        try {
            client.execute(postMenu, new VoidHandler());
        } catch (IOException ex) {
            throw new ClientException(ex);
        }
    }

    // http://mp.weixin.qq.com/wiki/11/74ad127cc054f6b80759c40f77ec03db.html#.E9.99.84.E5.BD.951-JS-SDK.E4.BD.BF.E7.94.A8.E6.9D.83.E9.99.90.E7.AD.BE.E5.90.8D.E7.AE.97.E6.B3.95
    @Override
    public String javascriptSign(String timestamp, String nonceStr, String url) throws ProtocolException {
        if (account.getJavascriptTicket() == null || account.getJavascriptTimeToExpire().isBefore(LocalDateTime.now())) {
            //重新获取
            Protocol.forAccount(account).getJavascriptTicket();
        }

        log.debug("JS SDK Sign using url:" + url);

        StringBuilder toSign = new StringBuilder("jsapi_ticket=");
        toSign.append(account.getJavascriptTicket()).append("&noncestr=");
        toSign.append(nonceStr).append("&timestamp=");
        toSign.append(timestamp).append("&url=");
        toSign.append(url);

        try {
            byte[] toSignBytes = toSign.toString().getBytes("UTF-8");
            MessageDigest messageDigest = MessageDigest.getInstance("sha1");
            messageDigest.update(toSignBytes);

            return new String(Hex.encode(messageDigest.digest()));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new InternalError(e);
        }
    }

    void newAccessToken() {
        HttpGet tokenGet = newTokenGet("/token?grant_type=client_credential&appid=" + account.getAppID() + "&secret=" + account.getAppSecret());

        try {
            AccessToken token = client.execute(tokenGet, new AccessTokenHandler());
            LocalDateTime time = LocalDateTime.now();
            time.plusSeconds(token.getTime());
            account.setTimeToExpire(time);
            account.setAccessToken(token.getToken());
        } catch (IOException ex) {
            throw new ClientException(ex);
        }
    }

    private HttpGet newGet(String uri, NameValuePair... parameters) {
        StringBuilder urlBuilder = buildUrl(uri, parameters);
        return new HttpGet(urlBuilder.toString());
    }

    private StringBuilder buildUrl(String uri, NameValuePair[] parameters) {
        StringBuilder urlBuilder = new StringBuilder("https://api.weixin.qq.com/cgi-bin" + uri + "?access_token="
                + account.getAccessToken());
        for (NameValuePair parameter : parameters) {
            urlBuilder.append("&").append(parameter.getName()).append("=");
            try {
                urlBuilder.append(URLEncoder.encode(parameter.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new InternalError(e);
            }
        }
        return urlBuilder;
    }

    private HttpPost newPost(String uri, NameValuePair... parameters) {
        StringBuilder urlBuilder = buildUrl(uri, parameters);
        return new HttpPost(urlBuilder.toString());
    }

    private HttpGet newTokenGet(String uriAndQuery) {
        return new HttpGet("https://api.weixin.qq.com/cgi-bin" + uriAndQuery);
    }

    PublicAccount getAccount() {
        return account;
    }
}
