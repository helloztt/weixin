package me.jiangcai.wxtest;

import me.jiangcai.lib.test.SpringWebTest;
import me.jiangcai.wx.couple.debug.DebugFilter;
import me.jiangcai.wxtest.config.MyConfig;
import org.junit.Test;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;

import javax.servlet.ServletException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * @author CJ
 */
@WebAppConfiguration
@ContextConfiguration(classes = MyConfig.class)
public class LoaderTest extends SpringWebTest {

    @Override
    protected DefaultMockMvcBuilder buildMockMVC(DefaultMockMvcBuilder builder) {
        DebugFilter debugFilter = new DebugFilter();
        try {
            debugFilter.init(new MockFilterConfig(servletContext));
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return builder.addFilters(debugFilter);
    }

    @Test
    public void aaaa() throws Exception {
        mockMvc.perform(post(""));
        mockMvc.perform(get(""));

//        mockMvc.perform(get("/haha")).andDo(print());
//        mockMvc.perform(get("/?signature=1f9a617a5593c0f5be9875d76223e57992eda1d3&echostr=1189703700494924542&timestamp=1473647356&nonce=1135788657"))
//                .andExpect(content().string("1189703700494924542"));
    }

}