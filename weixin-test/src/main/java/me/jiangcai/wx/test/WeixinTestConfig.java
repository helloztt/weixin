package me.jiangcai.wx.test;

import lombok.Getter;
import lombok.Setter;
import me.jiangcai.wx.model.WeixinUserDetail;
import me.jiangcai.wx.web.mvc.OpenIdArgumentResolver;
import me.jiangcai.wx.web.mvc.WeixinUserDetailResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 需要载入该类，可以通过调整{@link #nextDetail}来设置响应的微信用户
 *
 * @author CJ
 */
@Configuration
@Setter
@Getter
public class WeixinTestConfig {

    @Autowired(required = false)
    private WeixinUserMocker mocker;
    /**
     * 下一个微信用户
     */
    private WeixinUserDetail nextDetail;

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
            if (nextDetail != null)
                return nextDetail;
            return WeixinUserMocker.randomWeixinUserDetail();
        };
    }

}
