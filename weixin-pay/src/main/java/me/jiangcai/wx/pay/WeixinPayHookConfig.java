package me.jiangcai.wx.pay;

import me.jiangcai.wx.model.WeixinPayUrl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author helloztt
 */
@Configuration
@ComponentScan("me.jiangcai.wx.pay.controller")
public class WeixinPayHookConfig {
}
