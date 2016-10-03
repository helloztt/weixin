package me.jiangcai.wx.test;

import me.jiangcai.wx.model.Gender;
import me.jiangcai.wx.model.WeixinUserDetail;
import me.jiangcai.wx.web.mvc.OpenIdArgumentResolver;
import me.jiangcai.wx.web.mvc.WeixinUserDetailResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Locale;
import java.util.Random;
import java.util.UUID;

/**
 * 需要载入该类
 *
 * @author CJ
 */
@Configuration
public class WeixinTestConfig {

    @Autowired(required = false)
    private WeixinUserMocker mocker;

    @Bean
    @Primary
    public OpenIdArgumentResolver openIdArgumentResolver() {
        return new me.jiangcai.wx.test.mvc.OpenIdArgumentResolver(toMock());
    }

    @Bean
    @Primary
    public WeixinUserDetailResolver weixinUserDetailResolver() {
        return new me.jiangcai.wx.test.mvc.WeixinUserDetailResolver(toMock());
    }

    private WeixinUserMocker toMock() {
        if (mocker != null)
            return mocker;
        return (mavContainer, webRequest) -> {
            Random random = new Random();
            WeixinUserDetail detail = new WeixinUserDetail();
            detail.setOpenId(UUID.randomUUID().toString().replace("-", ""));
            detail.setCity("神州");
            detail.setCountry("夷洲");
            detail.setGender(Gender.values()[random.nextInt(Gender.values().length)]);
            detail.setHeadImageUrl("http://p.ishowx.com/uploads/allimg/160928/486-16092Q45357-50.jpg");
            detail.setLocale(Locale.CHINA);
            detail.setNickname(UUID.randomUUID().toString());
            detail.setPrivilege(new String[0]);
            detail.setProvince("牛神");
            return detail;
        };
    }

}
