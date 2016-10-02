package me.jiangcai.wx.web.mvc;

import me.jiangcai.wx.PublicAccountSupplier;
import me.jiangcai.wx.model.PublicAccount;
import me.jiangcai.wx.model.Template;
import me.jiangcai.wx.protocol.Protocol;
import me.jiangcai.wx.protocol.impl.BasicTemplateParameter;
import me.jiangcai.wx.web.WebTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author CJ
 */
public class OpenIdArgumentResolverTest extends WebTest {

    @Autowired
    private PublicAccountSupplier publicAccountSupplier;

    @Test
    public void go() throws Exception {
        // 首先伪装成一个微信的环境啦

        driver.get("http://wxtest.jiangcai.me/openIdTest");

        System.out.println(driver.getPageSource());
        System.out.println(driver.getCurrentUrl());

        final PublicAccount account = publicAccountSupplier.findByHost(null);
        Protocol.forAccount(account).findTemplate(new java.util.function.Predicate<Template>() {
            @Override
            public boolean test(Template template) {
                return template.getTitle().equals("测试模板");
            }
        }).ifPresent(template -> {
            Protocol.forAccount(account).sendTemplate("oiKvNt0neOAB8ddS0OzM_7QXQDZw",
                    template.getId(), driver.getCurrentUrl(), new BasicTemplateParameter("url", driver.getCurrentUrl()));
        });


//        mockMvc.perform(getWeixin("/openIdTest")
//        )
//                .andDo(print())
//                .andExpect(status().isOk());
    }

}