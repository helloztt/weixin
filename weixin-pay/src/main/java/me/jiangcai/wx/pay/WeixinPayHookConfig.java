package me.jiangcai.wx.pay;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author helloztt
 */
@Configuration
@ComponentScan({"me.jiangcai.wx.pay.controller","me.jiangcai.wx.pay.service"})
public class WeixinPayHookConfig {
}
