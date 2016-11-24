package me.jiangcai.wx.protocol;

import me.jiangcai.wx.PublicAccountSupplier;
import me.jiangcai.wx.SingleAccountSpringConfig;
import me.jiangcai.wx.message.TextMessage;
import me.jiangcai.wx.model.Menu;
import me.jiangcai.wx.model.MenuType;
import me.jiangcai.wx.model.MyWeixinUserDetail;
import me.jiangcai.wx.model.SceneCode;
import me.jiangcai.wx.model.Template;
import me.jiangcai.wx.model.WeixinUserDetail;
import me.jiangcai.wx.model.message.SimpleTemplateMessageParameter;
import me.jiangcai.wx.model.message.TemplateMessageLocate;
import me.jiangcai.wx.model.message.TemplateMessageParameter;
import me.jiangcai.wx.model.message.TemplateMessageStyle;
import me.jiangcai.wx.model.message.TemplateParameterAdjust;
import me.jiangcai.wx.protocol.impl.handler.WeixinResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
@ContextConfiguration(classes = SingleAccountSpringConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProtocolTest {

    Protocol protocol;
    @Autowired
    private PublicAccountSupplier supplier;
    private Random random = new Random();

    @Before
    public void init() {
//        protocol = Protocol.forAccount(new me.jiangcai.wx.DebugPublicAccount());
        protocol = Protocol.forAccount(supplier.findByHost(null));
    }

    // 临时测试
//    @Test
    public void temp() throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet test = new HttpGet("https://api.weixin.qq.com/sns/userinfo?&access_token=1XxIVR2GBztecC5HmbRx22r98I5q9nY4_HGbxOGJ5NCXddgQ04iZIZJlr-n1Vtm5sYEDSZgoaPS1pjkBD-tE5icEdBRtDzJJIcYQi2cJPkM&openid=oiKvNt0neOAB8ddS0OzM_7QXQDZw&lang=zh_CN");

            WeixinUserDetail detail = client.execute(test, new WeixinResponseHandler<>(WeixinUserDetail.class));
            System.out.println(detail);
        }
    }

    @Test
    public void qrCode() {
        SceneCode code = protocol.createQRCode(1, 60);
        System.out.println(code);

        MyWeixinUserDetail detail = protocol.userDetail("oiKvNt7Z-pzBTkhDZTCc5DU4ilHs");
        System.out.println(detail);
    }

    @Test
    public void send() {
        TextMessage textMessage = new TextMessage();
        textMessage.setContent(UUID.randomUUID().toString() + "中文");
        textMessage.setTo("oPFwRxCG65Tn2xw_FaK2IXV21uBc");
        protocol.send(textMessage);
    }

    /**
     * 实际公众号的模板
     */
    @Test
    @Ignore
    public void realTemplate() {
        // 款爷二手
        protocol.sendTemplate("oPFwRxCG65Tn2xw_FaK2IXV21uBc"
                , new TemplateMessageStyle() {
                    String templateId = "lHGV3WXyfgp-xq467eaaQ4OhrSLgeI9BkdAGztqUYJg";

                    @Override
                    public Collection<? extends TemplateMessageParameter> parameterStyles() {
                        return Arrays.asList(
                                new SimpleTemplateMessageParameter("first", "您好，我们已收到您的订单，将尽快发货")
                                , new SimpleTemplateMessageParameter("order", "IIIIDDDD")
                                , new SimpleTemplateMessageParameter("productName", "吴迪")
                                , new SimpleTemplateMessageParameter("time", "今年今日")
                                , new SimpleTemplateMessageParameter("result", "成功")
                                , new SimpleTemplateMessageParameter("remark", "更多商品请访问网上商城，好品牌，有分期，欢迎下次光临！")
                        );
                    }

                    @Override
                    public String getTemplateIdShort() {
                        return "TM00253";
                    }

                    @Override
                    public String getTemplateTitle() {
                        return "订购成功通知";
                    }

                    @Override
                    public String getIndustryId() {
                        return null;
                    }

                    @Override
                    public String getTemplateId() {
                        return templateId;
                    }

                    @Override
                    public void setTemplateId(String templateId) {
                        System.err.println("!!!!!!! -> " + templateId);
                        this.templateId = templateId;
                    }
                }, null, null, "1");
    }

    @Test
    public void templateMessage() {
        // 前提是存在一个业务逻辑
        // 然后 发送模板消息
        SimpleTemplateMessageParameter messageParameter1 = new SimpleTemplateMessageParameter();
        messageParameter1.setName("p1");
        messageParameter1.setPattern("嘿嘿{0}嘿嘿");
        SimpleTemplateMessageParameter messageParameter2 = new SimpleTemplateMessageParameter();
        messageParameter2.setName("p2");
        messageParameter2.setPattern("哈哈{1}哈哈");

        Template template = protocol.getTemplate(new TemplateMessageLocate() {
            @Override
            public String getTemplateIdShort() {
                return null;
            }

            @Override
            public String getTemplateTitle() {
                return null;
            }

            @Override
            public String getIndustryId() {
                return null;
            }

            @Override
            public String getTemplateId() {
                return "YoWOhKTShg9oCJmWT_41A45OgmcdstHHlZiPdFiSOGI";
            }

            @Override
            public void setTemplateId(String templateId) {

            }
        });

        System.out.println(template);
        assertThat(template.parameters())
                .contains("p1", "p2");

        protocol.sendTemplate("oiKvNt0neOAB8ddS0OzM_7QXQDZw", new TemplateMessageStyle() {
            @Override
            public String getTemplateIdShort() {
                return null;
            }

            @Override
            public String getTemplateTitle() {
                return null;
            }

            @Override
            public String getIndustryId() {
                return null;
            }

            @Override
            public String getTemplateId() {
                return "YoWOhKTShg9oCJmWT_41A45OgmcdstHHlZiPdFiSOGI";
            }

            @Override
            public void setTemplateId(String templateId) {

            }

            @Override
            public Collection<? extends TemplateMessageParameter> parameterStyles() {
                return Arrays.asList(messageParameter1, messageParameter2);
            }
        }, "http://www.baidu.com", new TemplateParameterAdjust() {
            @Override
            public Color color(TemplateMessageParameter parameter, Object[] arguments) {
                return random.nextBoolean() ? Color.magenta : Color.cyan;
            }
        }, "Foo", "Bar");

//        protocol.sendTemplate("oiKvNt0neOAB8ddS0OzM_7QXQDZw", new TemplateMessageStyle() {
//            @Override
//            public String getTemplateIdShort() {
//                return "abcdefg";
//            }
//
//            @Override
//            public String getTemplateTitle() {
//                return null;
//            }
//
//            @Override
//            public String getIndustryId() {
//                return null;
//            }
//
//            @Override
//            public String getTemplateId() {
//                return null;
//            }
//
//            @Override
//            public void setTemplateId(String templateId) {
//
//            }
//
//            @Override
//            public Collection<TemplateMessageParameter> parameterStyles() {
//                return null;
//            }
//        }, null, null, "a");

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
        menu4.setName("2中文");
        menu4.setData("http://wxtest.jiangcai.me/wxtest/js.html");

        protocol.createMenu(new Menu[]{menu1, menu2, menu4});
    }

    private Menu randomMenu(boolean allowSub) {
        Menu menu = new Menu();
        menu.setName(UUID.randomUUID().toString().substring(0, 4) + "中");
        if (random.nextBoolean() && allowSub) {
            int size = random.nextInt(5) + 1;
            Menu[] subs = new Menu[size];
            for (int i = 0; i < size; i++) {
                subs[i] = randomMenu(false);
            }
            menu.setSubs(subs);
        } else {
            menu.setType(MenuType.values()[1 + random.nextInt(MenuType.values().length - 1)]);
            while (menu.getType().getDataName().equalsIgnoreCase("media_id"))
                menu.setType(MenuType.values()[1 + random.nextInt(MenuType.values().length - 1)]);
            //  media_id 这个比较麻烦!
            menu.setData("http://www.baidu.com");
        }
        return menu;
    }

}