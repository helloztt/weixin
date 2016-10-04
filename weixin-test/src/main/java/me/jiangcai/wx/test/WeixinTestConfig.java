package me.jiangcai.wx.test;

import me.jiangcai.wx.web.mvc.OpenIdArgumentResolver;
import me.jiangcai.wx.web.mvc.WeixinUserDetailResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
        return (mavContainer, webRequest) -> WeixinUserMocker.randomWeixinUserDetail();
    }

}
