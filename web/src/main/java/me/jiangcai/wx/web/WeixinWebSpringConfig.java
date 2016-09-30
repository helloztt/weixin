package me.jiangcai.wx.web;

import me.jiangcai.wx.WeixinSpringConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 如果要开发微信网页则需要载入该配置 ,
 *
 * 可获得thymeleaf扩展wx
 * <ul>
 *     <li>js 可在页面中载入微信JS库以及相关授权</li>
 * </ul>
 * @author CJ
 */
@Configuration
@ComponentScan("me.jiangcai.wx.web.thymeleaf")
public class WeixinWebSpringConfig extends WeixinSpringConfig {



}
