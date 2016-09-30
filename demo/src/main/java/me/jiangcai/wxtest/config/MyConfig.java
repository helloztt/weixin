package me.jiangcai.wxtest.config;

import me.jiangcai.wx.MessageReply;
import me.jiangcai.wx.PublicAccountSupplier;
import me.jiangcai.wx.WeixinSpringConfig;
import me.jiangcai.wx.classic.ClassicMessageReply;
import me.jiangcai.wx.model.PublicAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collections;

/**
 * @author CJ
 */
@Configuration
@Import(WeixinSpringConfig.class)
@ComponentScan("me.jiangcai.wxtest.controller")
public class MyConfig {

    @Bean
    public PublicAccountSupplier publicAccountSupplier() {
        return () -> {
            PublicAccount publicAccount = new PublicAccount();
            publicAccount.setAppID("wx59b0162cdf0967af");
            publicAccount.setAppSecret("ffcf655fce7c4175bbddae7b594c4e27");
            publicAccount.setInterfaceURL("http://wxtest.jiangcai.me/wxtest/");
            publicAccount.setInterfaceToken("jiangcai");
            return Collections.singletonList(publicAccount);
        };
    }

    @Bean
    public MessageReply messageReply() {
        return new ClassicMessageReply();
    }

}
