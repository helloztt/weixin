package me.jiangcai.wx.protocol;

import me.jiangcai.wx.model.Menu;
import me.jiangcai.wx.model.MenuType;
import me.jiangcai.wx.model.WeixinUserDetail;
import me.jiangcai.wx.protocol.impl.handler.WeixinResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

/**
 * @author CJ
 */
public class ProtocolTest {

    Protocol protocol;
    private Random random = new Random();

    @Before
    public void init(){
        protocol = Protocol.forAccount(new me.jiangcai.wx.DebugPublicAccount());
    }

    // 临时测试
    @Test
    public void temp() throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet test = new HttpGet("https://api.weixin.qq.com/sns/userinfo?&access_token=1XxIVR2GBztecC5HmbRx22r98I5q9nY4_HGbxOGJ5NCXddgQ04iZIZJlr-n1Vtm5sYEDSZgoaPS1pjkBD-tE5icEdBRtDzJJIcYQi2cJPkM&openid=oiKvNt0neOAB8ddS0OzM_7QXQDZw&lang=zh_CN");

            WeixinUserDetail detail = client.execute(test, new WeixinResponseHandler<>(WeixinUserDetail.class));
            System.out.println(detail);
        }
    }

    @Test
    public void createMenu() throws Exception {
        System.out.println(protocol);

        Menu menu1 = randomMenu(true);
        Menu menu2 = randomMenu(true);
        Menu menu3 = randomMenu(true);

        // 固定的也写一个呗
        Menu menu4 = new Menu();
        menu4.setType(MenuType.view);
        menu4.setName("debug1");
        menu4.setData("http://wxtest.jiangcai.me/wxtest/js.html");

        protocol.createMenu(new Menu[]{menu1, menu2, menu4});
    }

    private Menu randomMenu(boolean allowSub) {
        Menu menu = new Menu();
        menu.setName(UUID.randomUUID().toString().substring(0,4));
        if (random.nextBoolean() && allowSub){
            int size = random.nextInt(5)+1;
            Menu[] subs = new Menu[size];
            for (int i = 0; i < size; i++) {
                subs[i] = randomMenu(false);
            }
            menu.setSubs(subs);
        }else{
            menu.setType(MenuType.values()[1+random.nextInt(MenuType.values().length-1)]);
            while (menu.getType().getDataName().equalsIgnoreCase("media_id"))
                menu.setType(MenuType.values()[1+random.nextInt(MenuType.values().length-1)]);
            //  media_id 这个比较麻烦!
            menu.setData("http://www.baidu.com");
        }
        return menu;
    }

}