package me.jiangcai.wx.protocol.impl;

import me.jiangcai.wx.model.Menu;
import me.jiangcai.wx.model.PublicAccount;
import me.jiangcai.wx.protocol.Protocol;
import me.jiangcai.wx.protocol.exception.ClientException;
import me.jiangcai.wx.protocol.exception.ProtocolException;
import me.jiangcai.wx.protocol.impl.handler.AccessTokenHandler;
import me.jiangcai.wx.protocol.impl.handler.VoidHandler;
import me.jiangcai.wx.protocol.impl.response.AccessToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author CJ
 */
class ProtocolImpl implements Protocol {

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

    private HttpGet newGet(String uri) {
        return new HttpGet("https://api.weixin.qq.com/cgi-bin" + uri + "?access_token=" + account.getAccessToken());
    }

    private HttpPost newPost(String uri) {
        return new HttpPost("https://api.weixin.qq.com/cgi-bin" + uri + "?access_token=" + account.getAccessToken());
    }

    private HttpGet newTokenGet(String uriAndQuery) {
        return new HttpGet("https://api.weixin.qq.com/cgi-bin" + uriAndQuery);
    }

    PublicAccount getAccount() {
        return account;
    }
}
