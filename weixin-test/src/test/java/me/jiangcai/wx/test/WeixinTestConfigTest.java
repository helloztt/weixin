package me.jiangcai.wx.test;

import me.jiangcai.lib.test.SpringWebTest;
import me.jiangcai.wx.SingleAccountSpringConfig;
import me.jiangcai.wx.web.SimpleMVCConfig;
import me.jiangcai.wx.web.WeixinWebSpringConfig;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author CJ
 */
@WebAppConfiguration
@ContextConfiguration(classes = {SingleAccountSpringConfig.class, SimpleMVCConfig.class, WeixinWebSpringConfig.class, WeixinTestConfig.class})
public class WeixinTestConfigTest extends SpringWebTest {

    @Test
    public void id() throws Exception {
        mockMvc.perform(get("/openIdTest"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/userInfo"))
                .andExpect(status().isOk());
    }

}