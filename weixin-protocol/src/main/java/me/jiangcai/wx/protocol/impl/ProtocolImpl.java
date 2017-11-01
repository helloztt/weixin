package me.jiangcai.wx.protocol.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jiangcai.lib.seext.ImageUtils;
import me.jiangcai.wx.TokenType;
import me.jiangcai.wx.WeixinUserService;
import me.jiangcai.wx.message.Message;
import me.jiangcai.wx.model.Menu;
import me.jiangcai.wx.model.MyWeixinUserDetail;
import me.jiangcai.wx.model.PublicAccount;
import me.jiangcai.wx.model.SceneCode;
import me.jiangcai.wx.model.Template;
import me.jiangcai.wx.model.TemplateList;
import me.jiangcai.wx.model.UserAccessResponse;
import me.jiangcai.wx.model.UserList;
import me.jiangcai.wx.model.WeixinUser;
import me.jiangcai.wx.model.WeixinUserDetail;
import me.jiangcai.wx.model.media.MediaItem;
import me.jiangcai.wx.model.media.NewsMediaItem;
import me.jiangcai.wx.model.message.TemplateMessageLocate;
import me.jiangcai.wx.model.message.TemplateMessageStyle;
import me.jiangcai.wx.model.message.TemplateParameterAdjust;
import me.jiangcai.wx.protocol.Protocol;
import me.jiangcai.wx.protocol.TemplateParameter;
import me.jiangcai.wx.protocol.exception.BadAuthAccessException;
import me.jiangcai.wx.protocol.exception.ClientException;
import me.jiangcai.wx.protocol.exception.ProtocolException;
import me.jiangcai.wx.protocol.impl.handler.AccessTokenHandler;
import me.jiangcai.wx.protocol.impl.handler.MediaItemResponseHandler;
import me.jiangcai.wx.protocol.impl.handler.VoidHandler;
import me.jiangcai.wx.protocol.impl.handler.WeixinResponseHandler;
import me.jiangcai.wx.protocol.impl.response.AccessToken;
import me.jiangcai.wx.protocol.impl.response.AddTemplate;
import me.jiangcai.wx.protocol.impl.response.CreateQRCodeResponse;
import me.jiangcai.wx.protocol.impl.response.JavascriptTicket;
import me.jiangcai.wx.protocol.impl.response.MediaResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.codec.Hex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
//    private final CloseableHttpClient client;

    ProtocolImpl(PublicAccount account) {
        this.account = account;
//        client = HttpClientBuilder.create()
//                .build();
    }

    private CloseableHttpClient requestClient() {
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder = builder.setDefaultRequestConfig(RequestConfig.custom()
                .setConnectTimeout(30000)
                .setConnectionRequestTimeout(30000)
                .setSocketTimeout(30000)
                .build());
//        if (environment.acceptsProfiles("test")) {
//            builder.setSSLHostnameVerifier(new NoopHostnameVerifier());
//        }

        return builder.build();
    }

    @Override
    public String addImage(boolean permanent, BufferedImage image, String type) throws ProtocolException {
        // 先准备素材，然后整理成数据
        final String imageType;
        if (type != null)
            imageType = type.toLowerCase(Locale.ENGLISH);
        else
            imageType = "png";

        try {
            final InputStream data = ImageUtils.scaleToLimitSize(image, imageType
                    , 2 * 1024);
            // 使用临时文件
            Path path = Files.createTempFile("imageForMedia", "." + imageType);
            try {
                Files.copy(data, path, StandardCopyOption.REPLACE_EXISTING);
                MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                        .addBinaryBody("media", path.toFile());
//                        .addBinaryBody("media", data, ContentType.APPLICATION_OCTET_STREAM, "image." + imageType);
                return addMedia(permanent, "image", builder);
            } finally {
                Files.delete(path);
            }

        } catch (IOException e) {
            throw new ProtocolException(e);
        }
    }

    private String addMedia(boolean permanent, String type, MultipartEntityBuilder builder) throws IOException {
        if (!permanent) {
//            builder =
//                    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
//                            .addTextBody("access_token", account.getAccessToken())
//                            .addTextBody("type", type);
            String url = buildUrl("/media/upload", new BasicNameValuePair("type", type));
            HttpPost post = new HttpPost(url);
            post.setEntity(builder.build());
            try (CloseableHttpClient client = requestClient()) {
                return client.execute(post, new WeixinResponseHandler<>(MediaResult.class)).getMediaId();
            }
        } else
            throw new IllegalStateException("暂不支持永久素材");
    }

    @Override
    public void getJavascriptTicket() throws ProtocolException {
        HttpGet tokenGet = newGet("/ticket/getticket", new BasicNameValuePair("type", "jsapi"));
        try {
            try (CloseableHttpClient client = requestClient()) {
                JavascriptTicket token = client.execute(tokenGet, new WeixinResponseHandler<>(JavascriptTicket.class));

                if (token.getCode() != 0)
                    throw new ProtocolException(token.getMessage());

                LocalDateTime time = LocalDateTime.now();
                time = time.plusSeconds(token.getTime());
                account.setJavascriptTimeToExpire(time);
                account.setJavascriptTicket(token.getToken());

                try {
                    account.getSupplier().updateToken(account, TokenType.javascript, token.getToken(), time);
                } catch (Throwable throwable) {
                    log.debug("update tokens", throwable);
                }
            }

        } catch (IOException ex) {
            throw new ClientException(ex);
        }
    }

    @Override
    public String redirectUrl(String url, Class clazz) {
        final String type;
        if (clazz == String.class)
            type = "snsapi_base";
        else
            type = "snsapi_userinfo";

        StringBuilder stringBuilder = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize?appid=");
        stringBuilder.append(account.getAppID());
        try {
            stringBuilder.append("&redirect_uri=").append(URLEncoder.encode(url, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new InternalError(e);
        }
        stringBuilder.append("&response_type=code&scope=")
                .append(type)
                .append("#wechat_redirect");
        //&state=123
        return stringBuilder.toString();
    }

    @Override
    public String userToken(String code, WeixinUserService weixinUserService, Object data) throws ProtocolException {
        // https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        HttpGet get = newGetUrl("https://api.weixin.qq.com/sns/oauth2/access_token?"
                , new BasicNameValuePair("appid", account.getAppID())
                , new BasicNameValuePair("secret", account.getAppSecret())
                , new BasicNameValuePair("code", code)
                , new BasicNameValuePair("grant_type", "authorization_code"));

        try {
            UserAccessResponse response = workWithUserAuth(weixinUserService, get, data);

            return response.getOpenId();
        } catch (IOException e) {
            throw new ProtocolException(e);
        }

    }

    private UserAccessResponse workWithUserAuth(WeixinUserService weixinUserService, HttpGet get, Object data) throws IOException {
        try (CloseableHttpClient client = requestClient()) {
            UserAccessResponse response = client.execute(get, new WeixinResponseHandler<>(UserAccessResponse.class));
            if (log.isDebugEnabled()) {
                log.debug("UserAccessResponse:" + response + "  scope:");
                for (String s : response.getScope()) {
                    log.debug(s);
                }
            }

            weixinUserService.updateUserToken(account, response, data);
            return response;
        }
    }

    @Override
    public WeixinUserDetail userDetail(String openId, WeixinUserService weixinUserService, Object data) throws ProtocolException {
        WeixinUser user = weixinUserService.getTokenInfo(account, openId);

        if (user == null || !user.isAbleDetail()) {
            throw new BadAuthAccessException();
        }

        try {

            try {
                return getWeixinUserDetail(openId, user);
            } catch (BadAuthAccessException ex) {
                // 刷新
                HttpGet refresh = newGetUrl("https://api.weixin.qq.com/sns/oauth2/refresh_token?"
                        , new BasicNameValuePair("appid", account.getAppID())
                        , new BasicNameValuePair("grant_type", "refresh_token")
                        , new BasicNameValuePair("refresh_token", user.getRefreshToken())
                );
                workWithUserAuth(weixinUserService, refresh, data);
                return getWeixinUserDetail(openId, weixinUserService.getTokenInfo(account, openId));
            }

        } catch (IOException ex) {
            throw new ProtocolException(ex);
        }

    }

    private WeixinUserDetail getWeixinUserDetail(String openId, WeixinUser user) throws IOException {
        HttpGet getInfo = newGetUrl("https://api.weixin.qq.com/sns/userinfo?"
                , new BasicNameValuePair("access_token", user.getAccessToken())
                , new BasicNameValuePair("openid", openId)
                , new BasicNameValuePair("lang", account.getLocale().toString())
        );
        try (CloseableHttpClient client = requestClient()) {
            final WeixinUserDetail userDetail = client.execute(getInfo, new WeixinResponseHandler<>(WeixinUserDetail.class));
            userDetail.setAppId(account.getAppID());
            return userDetail;
        }
    }

    @Override
    public Optional<Template> findTemplate(Predicate<Template> predicate) throws ProtocolException {
        HttpGet getTemplate = newGet("/template/get_all_private_template");
        try {
            try (CloseableHttpClient client = requestClient()) {
                TemplateList list = client.execute(getTemplate, new WeixinResponseHandler<>(TemplateList.class));
                return list.getList().stream().filter(predicate)
                        .findAny();
            }

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
                    Color color = templateParameter.getColor();
                    if (color == null)
                        map.put("color", "#173177");
                    else {
                        map.put("color", String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()));
                    }
                    data.put(templateParameter.getName(), map);
                });

        executeMethodWithJSONAndVoid(postMenu, toPost);
    }

    @Override
    public void sendTemplate(String openId, TemplateMessageStyle style, String url, TemplateParameterAdjust adjust
            , Object... arguments) throws ProtocolException {
        //寻找具体的模板
        getTemplate(style);

        // 准备消息
        List<TemplateParameter> templateParameterList = new ArrayList<>();

        style.parameterStyles().forEach(templateMessageParameter -> {
            templateParameterList.add(new TemplateParameter() {
                @Override
                public Color getColor() {
                    if (adjust == null)
                        return templateMessageParameter.getDefaultColor();
                    return adjust.color(templateMessageParameter, arguments);
                }

                @Override
                public String getName() {
                    return templateMessageParameter.getName();
                }

                @Override
                public String getValue() {
                    return templateMessageParameter.getFormat().format(arguments);
                }
            });
        });

        sendTemplate(openId, style.getTemplateId(), url
                , templateParameterList.toArray(new TemplateParameter[templateParameterList.size()]));
    }

    @Override
    public SceneCode createQRCode(String sceneStr, Integer seconds) throws ProtocolException {
        HttpPost create = newPost("/qrcode/create");

        // 准备数据
        HashMap<String, Object> toPost = new HashMap<>();
        if (seconds != null) {
            toPost.put("expire_seconds", seconds);
            toPost.put("action_name", "QR_STR_SCENE");
        } else {
            toPost.put("action_name", "QR_LIMIT_STR_SCENE");
        }
        HashMap<String, Object> scene = new HashMap<>();
        scene.put("scene_str", sceneStr);
        HashMap<String, Object> action = new HashMap<>();
        action.put("scene", scene);
        toPost.put("action_info", action);

        return getSceneCode(create, toPost);
    }

    private SceneCode getSceneCode(HttpPost create, HashMap<String, Object> toPost) {
        try {
            HttpEntity entity = EntityBuilder.create()
                    .setContentType(ContentType.create("application/json", "UTF-8"))
                    .setText(objectMapper.writeValueAsString(toPost))
                    .build();

            create.setEntity(entity);
            try (CloseableHttpClient client = requestClient()) {
                return client.execute(create, new WeixinResponseHandler<>(CreateQRCodeResponse.class)).toCode();
            }
        } catch (IOException ex) {
            throw new ClientException(ex);
        }
    }

    @Override
    public SceneCode createQRCode(int sceneId, Integer seconds) throws ProtocolException {
        HttpPost create = newPost("/qrcode/create");

        // 准备数据
        HashMap<String, Object> toPost = new HashMap<>();
        if (seconds != null) {
            toPost.put("expire_seconds", seconds);
            toPost.put("action_name", "QR_SCENE");
        } else {
            toPost.put("action_name", "QR_LIMIT_SCENE");
        }
        HashMap<String, Object> scene = new HashMap<>();
        scene.put("scene_id", sceneId);
        HashMap<String, Object> action = new HashMap<>();
        action.put("scene", scene);
        toPost.put("action_info", action);

        return getSceneCode(create, toPost);
    }

    @Override
    public List<String> openIdList() throws ProtocolException {
        // https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID
        // https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
        List<String> list = new ArrayList<>();
        String next = null;
        while (true) {
            HttpGet get;
            if (next == null)
                get = newGet("/user/get");
            else
                get = newGet("/user/get", new BasicNameValuePair("next_openid", next));

            try {
                try (CloseableHttpClient client = requestClient()) {
                    UserList list1 = client.execute(get, new WeixinResponseHandler<>(UserList.class));
                    if (list1.getData() == null || list1.getData().getList().isEmpty())
                        return list;
                    next = list1.getNext();
                    list.addAll(list1.getData().getList());
                }
            } catch (IOException e) {
                throw new ClientException(e);
            }
        }
    }

    @Override
    public Page<NewsMediaItem> listNewsMedia(Pageable page) throws ProtocolException {
        return listMedia(NewsMediaItem.class, "news", page);
    }

    private <T extends MediaItem> Page<T> listMedia(Class<T> clazz, String type, Pageable inputPage) {
        final Pageable page;
        if (inputPage != null) {
            page = inputPage;
        } else
            page = new PageRequest(0, 20);

        HttpPost post = newPost("/material/batchget_material");
        HashMap<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.put("offset", page.getOffset());
        data.put("count", page.getPageSize());
        try {
            try (CloseableHttpClient client = requestClient()) {
                post.setEntity(EntityBuilder.create()
                        .setText(objectMapper.writeValueAsString(data)).build());
                return client.execute(post, new MediaItemResponseHandler<>(clazz, page));
            }
        } catch (IOException e) {
            throw new ClientException(e);
        }
    }

    @Override
    public MyWeixinUserDetail userDetail(String openId) throws ProtocolException {
        HttpGet get = newGet("/user/info", new BasicNameValuePair("openid", openId),
                new BasicNameValuePair("lang", account.getLocale().toString()));
        try {
            try (CloseableHttpClient client = requestClient()) {
                final MyWeixinUserDetail userDetail = client.execute(get, new WeixinResponseHandler<>(MyWeixinUserDetail.class));
                userDetail.setAppId(account.getAppID());
                return userDetail;
            }
        } catch (IOException e) {
            throw new ClientException(e);
        }
    }

    @Override
    public Template getTemplate(TemplateMessageLocate style) throws ProtocolException {
        if (style.getTemplateId() == null) {
            Template templateMessage = findTemplate(template -> {
                return template.getTitle().equals(style.getTemplateTitle());
            }).orElse(null);

            if (templateMessage == null) {
                // 添加
                HttpPost addTemplate = newPost("/template/api_add_template");
                HashMap<String, Object> toPost = new HashMap<>();
                toPost.put("template_id_short", style.getTemplateIdShort());
                try {
                    HttpEntity entity = EntityBuilder.create()
                            .setContentType(ContentType.create("application/json", "UTF-8"))
                            .setText(objectMapper.writeValueAsString(toPost))
                            .build();

                    addTemplate.setEntity(entity);
                    try (CloseableHttpClient client = requestClient()) {
                        style.setTemplateId(client.execute(addTemplate, new WeixinResponseHandler<>(AddTemplate.class)).getTemplateId());
                    }
                } catch (IOException ex) {
                    throw new ClientException(ex);
                }
            } else {
                style.setTemplateId(templateMessage.getId());
            }
        }
        return findTemplate(template -> template.getId().equals(style.getTemplateId()))
                .orElseGet(() -> {
                    style.setTemplateId(null);
                    return getTemplate(style);
                });
    }

    @Override
    public void send(Message message) throws ProtocolException {
        // http://mp.weixin.qq.com/wiki/11/c88c270ae8935291626538f9c64bd123.html#.E5.AE.A2.E6.9C.8D.E6.8E.A5.E5.8F.A3-.E5.8F.91.E6.B6.88.E6.81.AF
        HttpPost send = newPost("/message/custom/send");

        Map<String, Object> toPost = message.sendTo();
        executeMethodWithJSONAndVoid(send, toPost);
    }

    private void executeMethodWithJSONAndVoid(HttpPost send, Map<String, Object> toPost) {
        try {
            HttpEntity entity = EntityBuilder.create()
                    .setContentType(ContentType.create("application/json", "UTF-8"))
                    .setText(objectMapper.writeValueAsString(toPost))
                    .build();
            send.setEntity(entity);
            try (CloseableHttpClient client = requestClient()) {
                client.execute(send, new VoidHandler());
            }

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
                .setContentType(ContentType.create("application/json", "UTF-8"))
                .setText(Menu.toContent(menus))
                .build();

        postMenu.setEntity(entity);

        try {
            try (CloseableHttpClient client = requestClient()) {
                client.execute(postMenu, new VoidHandler());
            }

        } catch (IOException ex) {
            throw new ClientException(ex);
        }
    }

    void newAccessToken() {
        HttpGet tokenGet = newTokenGet("/token?grant_type=client_credential&appid=" + account.getAppID() + "&secret=" + account.getAppSecret());

        try {
            try (CloseableHttpClient client = requestClient()) {
                AccessToken token = client.execute(tokenGet, new AccessTokenHandler());
                LocalDateTime time = LocalDateTime.now();
                time = time.plusSeconds(token.getTime());
                account.setTimeToExpire(time);
                account.setAccessToken(token.getToken());

                try {
                    account.getSupplier().updateToken(account, TokenType.access, token.getToken(), time);
                } catch (Throwable throwable) {
                    log.debug("update tokens", throwable);
                }
            }

        } catch (IOException ex) {
            throw new ClientException(ex);
        }
    }

    private HttpGet newGet(String uri, NameValuePair... parameters) {
        String urlBuilder = buildUrl(uri, parameters);
        return new HttpGet(urlBuilder);
    }

    private String buildUrl(String uri, NameValuePair... parameters) {
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
        log.debug("[WEIXIN]" + urlBuilder.toString());
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
