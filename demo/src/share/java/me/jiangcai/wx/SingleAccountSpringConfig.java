package me.jiangcai.wx;

import me.jiangcai.wx.classics.SinglePublicAccountSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 只有一个公众账号的配置
 * 无论测试或者是demo都会用到,这里采用系统参数配置uri
 *
 * @author CJ
 */
@Configuration
public class SingleAccountSpringConfig {

    @Autowired
    private Environment environment;

    @Bean
    public PublicAccountSupplier publicAccountSupplier() {
        final DebugPublicAccount publicAccount = new DebugPublicAccount(environment.getProperty("account.url", "http://localhost/"));

        final SinglePublicAccountSupplier singlePublicAccountSupplier = new SinglePublicAccountSupplier
                (publicAccount);
        publicAccount.setSupplier(singlePublicAccountSupplier);

        singlePublicAccountSupplier.getTokens(publicAccount);

        return singlePublicAccountSupplier;
    }

    @Bean
    public DemoWeixinUserService demoWeixinUserService() {
        return new DemoWeixinUserService();
    }
}
