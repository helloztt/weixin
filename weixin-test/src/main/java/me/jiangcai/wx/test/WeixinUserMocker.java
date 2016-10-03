package me.jiangcai.wx.test;

import me.jiangcai.wx.model.WeixinUserDetail;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 模拟用户信息
 *
 * @author CJ
 */
public interface WeixinUserMocker {

    /**
     * @param mavContainer
     * @param webRequest
     * @return 必须非空, 如果想模拟获取微信用户失败请以异常改变流程
     */
    WeixinUserDetail mockUser(ModelAndViewContainer mavContainer, NativeWebRequest webRequest);
}
