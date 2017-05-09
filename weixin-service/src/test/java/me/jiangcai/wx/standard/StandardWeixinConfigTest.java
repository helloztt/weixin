package me.jiangcai.wx.standard;

import me.jiangcai.lib.test.SpringWebTest;
import me.jiangcai.wx.WeixinUserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author CJ
 */
@ContextConfiguration(classes = StandardWeixinConfigTest.Config.class)
public class StandardWeixinConfigTest extends SpringWebTest {

    @Autowired
    private WeixinUserService weixinUserService;

    @Test
    public void gg() {
        System.out.println(weixinUserService);
    }

    @EnableTransactionManagement(mode = AdviceMode.PROXY)
    @EnableAspectJAutoProxy
    @ImportResource("classpath:/datasource_local.xml")
    @Import(StandardWeixinConfig.class)
    static class Config {
    }

}