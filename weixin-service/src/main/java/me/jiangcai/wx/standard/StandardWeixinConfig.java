package me.jiangcai.wx.standard;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 标准流程可直接使用该配置
 *
 * @author CJ
 */
@Configuration
@EnableJpaRepositories("me.jiangcai.wx.standard.repository")
@ComponentScan("me.jiangcai.wx.standard.service")
public class StandardWeixinConfig {
}
