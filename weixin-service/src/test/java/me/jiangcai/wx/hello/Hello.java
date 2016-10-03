package me.jiangcai.wx.hello;

import me.jiangcai.wx.SingleAccountTest;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * @author CJ
 */
public class Hello extends SingleAccountTest {

    @Test
    public void coupleIn() throws Exception {
        mockMvc.perform(get("/?signature=1f9a617a5593c0f5be9875d76223e57992eda1d3&echostr=1189703700494924542&timestamp=1473647356&nonce=1135788657"))
                .andExpect(content().string("1189703700494924542"))
                .andDo(print())
        ;

    }

//    @Test
//    public void logPost() throws Exception {
//
//        mockMvc.perform(post("/" + UUID.randomUUID().toString())
//                .content(UUID.randomUUID().toString())
//        )
//        ;
//    }
}
