package me.jiangcai.wx.web.mvc;

import me.jiangcai.wx.web.WebTest;
import org.junit.Test;

/**
 * @author CJ
 */
public class OpenIdArgumentResolverTest extends WebTest {

    @Test
    public void go() throws Exception {
        // 首先伪装成一个微信的环境啦

        driver.get("http://test.wx.com/openIdTest");

        System.out.println(driver.getPageSource());
//        mockMvc.perform(getWeixin("/openIdTest")
//        )
//                .andDo(print())
//                .andExpect(status().isOk());
    }

}