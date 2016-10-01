package me.jiangcai.wx.web;

import me.jiangcai.lib.test.SpringWebTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author CJ
 */
@WebAppConfiguration
@ContextConfiguration(classes = {WeixinWebSpringConfig.class, me.jiangcai.wx.SingleAccountSpringConfig.class})
public abstract class BaseTest extends SpringWebTest {
}
