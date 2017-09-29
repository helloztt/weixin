package me.jiangcai.wxtest.config;

import me.jiangcai.wx.MessageReply;
import me.jiangcai.wx.SingleAccountSpringConfig;
import me.jiangcai.wx.classic.ClassicMessageReply;
import me.jiangcai.wx.classic.TempSceneReply;
import me.jiangcai.wx.message.ImageMessage;
import me.jiangcai.wx.message.Message;
import me.jiangcai.wx.message.TextMessage;
import me.jiangcai.wx.model.PublicAccount;
import me.jiangcai.wx.protocol.Protocol;
import me.jiangcai.wx.web.SimpleMVCConfig;
import me.jiangcai.wx.web.WeixinWebSpringConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;

/**
 * @author CJ
 */
@Configuration
@Import({MyConfig.Pre.class, SingleAccountSpringConfig.class, WeixinWebSpringConfig.class, SimpleMVCConfig.class})
@ComponentScan("me.jiangcai.wxtest.controller")
@EnableWebMvc
public class MyConfig {

    private static final Log log = LogFactory.getLog(MyConfig.class);

    @Bean
    public MessageReply messageReply() {
        return new ClassicMessageReply();
    }

    @Bean
    public MessageReply imageReply() {
        return new MessageReply() {
            @Override
            public boolean focus(PublicAccount account, Message message) {
                return message != null && message instanceof TextMessage && ((TextMessage) message).getContent().equalsIgnoreCase("image");
            }

            @Override
            public Message reply(PublicAccount account, Message message) {
                try {
                    String id = Protocol.forAccount(account).addImage(false, new ClassPathResource("images/image.JPG").getFile());
                    ImageMessage imageMessage = new ImageMessage();
                    imageMessage.setMediaId(id);
                    return imageMessage;
                } catch (IOException e) {
                    return null;
                }
            }
        };
    }

    @Bean
    public MessageReply eventMessageReply() {
        return new TempSceneReply() {

            @Override
            public void happen(PublicAccount account, Message message, int sceneId) {
                log.debug("happen:" + sceneId);
            }

            @Override
            public void happen(PublicAccount account, Message message, String sceneStr) {
                log.debug("happen:" + sceneStr);
            }
        };
    }

    @Configuration
    @PropertySource("classpath:/demo.properties")
    static class Pre {

    }

}
