package me.jiangcai.wx.protocol.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jiangcai.wx.WeixinUserService;
import me.jiangcai.wx.model.Menu;
import me.jiangcai.wx.model.PublicAccount;
import me.jiangcai.wx.model.Template;
import me.jiangcai.wx.model.TemplateList;
import me.jiangcai.wx.protocol.Protocol;
import me.jiangcai.wx.protocol.TemplateParameter;
import me.jiangcai.wx.protocol.exception.ClientException;
import me.jiangcai.wx.protocol.exception.ProtocolException;
import me.jiangcai.wx.protocol.impl.handler.AccessTokenHandler;
import me.jiangcai.wx.protocol.impl.handler.VoidHandler;
import me.jiangcai.wx.protocol.impl.handler.WeixinResponseHandler;
import me.jiangcai.wx.protocol.impl.response.AccessToken;
import me.jiangcai.wx.protocol.impl.response.JavascriptTicket;
import me.jiangcai.wx.protocol.impl.response.UserAccessResponse;
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
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author CJ
 */
class ProtocolImpl implements Protocol {

    private static final Log log = LogFactory.getLog(ProtocolImpl.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
    public String baseRedirectUrl(String url) {
//        url = "http://www.baidu.com";
//        https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx520c15f417810387
        StringBuilder stringBuilder = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx520c15f417810387");

        // &redirect_uri=https%3A%2F%2Fchong.qq.com%2Fphp%2Findex.php%3Fd%3D%26c%3DwxAdapter%26m%3DmobileDeal%26showwxpaytitle%3D1%26vb2ctag%3D4_2030_5_1194_60
        // &response_type=code&scope=snsapi_base&state=123#wechat_redirect
        try {
            stringBuilder.append("&redirect_uri=").append(URLEncoder.encode(url, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new InternalError(e);
        }
        stringBuilder.append("&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
        return stringBuilder.toString();


//        StringBuilder urlBuilder = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize?");
//        urlBuilder.append("appid=").append(account.getAppID());
//        try {
//            urlBuilder.append("&redirect_uri=").append(URLEncoder.encode(url, "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            throw new InternalError(e);
//        }
////        urlBuilder.append("&response_type=code")
////                .append("&scope=").append("snsapi_base");
////        urlBuilder.append("&state=");
////
////        urlBuilder.append("#wechat_redirect");
//
//        urlBuilder.append("&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
//
//        return urlBuilder.toString();
    }

    @Override
    public String userToken(String code, WeixinUserService weixinUserService) throws ProtocolException {
        // https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        HttpGet get = newGetUrl("https://api.weixin.qq.com/sns/oauth2/access_token?"
                , new BasicNameValuePair("appid", account.getAppID())
                , new BasicNameValuePair("secret", account.getAppSecret())
                , new BasicNameValuePair("code", code)
                , new BasicNameValuePair("grant_type", "authorization_code"));

        try {
            UserAccessResponse response = client.execute(get, new WeixinResponseHandler<>(UserAccessResponse.class));
            return response.getOpenId();
        } catch (IOException e) {
            throw new ProtocolException(e);
        }

    }

    @Override
    public Optional<Template> findTemplate(Predicate<Template> predicate) throws ProtocolException {
        HttpGet getTemplate = newGet("/template/get_all_private_template");
        try {
            TemplateList list = client.execute(getTemplate, new WeixinResponseHandler<>(TemplateList.class));
            return list.getList().stream().filter(predicate)
                    .findAny();
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
    }

    @Override
    public void sendTemplate(String openId, String templateId, String url, TemplateParameter... parameters) throws ProtocolException {
        HttpPost postMenu = newPost("/message/template/send");

        HashMap<String, Object> toPost = new HashMap<>();
        toPost.put("touser", openId);
        toPost.put("template_id", templateId);
        toPost.put("url", url);
        HashMap<String, Object> data = new HashMap<>();
        toPost.put("data", data);
        Stream.of(parameters)
                .forEach(templateParameter -> {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("value", templateParameter.getValue());
                    map.put("color", "#173177");
                    data.put(templateParameter.getName(), map);
                });


        try {
            HttpEntity entity = EntityBuilder.create()
                    .setText(objectMapper.writeValueAsString(toPost))
                    .build();

            postMenu.setEntity(entity);
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

    private HttpGet newGetUrl(String urlWith, NameValuePair... parameters) {
        return new HttpGet(buildUrlWithUrl(urlWith, parameters));
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
        String urlBuilder = buildUrl(uri, parameters);
        return new HttpGet(urlBuilder);
    }

    private String buildUrl(String uri, NameValuePair[] parameters) {
        String url = "https://api.weixin.qq.com/cgi-bin" + uri + "?access_token="
                + account.getAccessToken();
        return buildUrlWithUrl(url, parameters);
    }

    private String buildUrlWithUrl(String url, NameValuePair... parameters) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(url);
        for (NameValuePair parameter : parameters) {
            urlBuilder.append("&").append(parameter.getName()).append("=");
            try {
                urlBuilder.append(URLEncoder.encode(parameter.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new InternalError(e);
            }
        }
        return urlBuilder.toString();
    }

    private HttpPost newPost(String uri, NameValuePair... parameters) {
        String urlBuilder = buildUrl(uri, parameters);
        return new HttpPost(urlBuilder);
    }

    private HttpGet newTokenGet(String uriAndQuery) {
        return new HttpGet("https://api.weixin.qq.com/cgi-bin" + uriAndQuery);
    }

    PublicAccount getAccount() {
        return account;
    }
}
