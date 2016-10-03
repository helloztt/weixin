package me.jiangcai.wx.message;

import me.jiangcai.wx.MessageReply;
import me.jiangcai.wx.SingleAccountTest;
import me.jiangcai.wx.classic.ClassicMessageReply;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 注册一个消息接收者,然后发布事件
 *
 * @author CJ
 */
@ContextConfiguration(classes = MessageTest.Config.class)
public class MessageTest extends SingleAccountTest {

    @Test
    public void text() throws Exception {
        mockMvc.perform(postWeixinMessage("/", "classpath:/message-text.json"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(postWeixinMessage("/", "classpath:/message-subscribe.json"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(postWeixinMessage("/", "classpath:/message-unsubscribe.json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    static class Config {
        @Bean
        public MessageReply messageReply() {
            return new ClassicMessageReply();
        }

    }

}
