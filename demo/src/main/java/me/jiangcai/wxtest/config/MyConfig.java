package me.jiangcai.wxtest.config;

import me.jiangcai.wx.MessageReply;
import me.jiangcai.wx.SingleAccountSpringConfig;
import me.jiangcai.wx.classic.ClassicMessageReply;
import me.jiangcai.wx.web.SimpleMVCConfig;
import me.jiangcai.wx.web.WeixinWebSpringConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author CJ
 */
@Configuration
@Import({MyConfig.Pre.class, SingleAccountSpringConfig.class, WeixinWebSpringConfig.class, SimpleMVCConfig.class})
@ComponentScan("me.jiangcai.wxtest.controller")
@EnableWebMvc
public class MyConfig {

    @Bean
    public MessageReply messageReply() {
        return new ClassicMessageReply();
    }

    @Configuration
    @PropertySource("classpath:/demo.properties")
    static class Pre {

    }

}
