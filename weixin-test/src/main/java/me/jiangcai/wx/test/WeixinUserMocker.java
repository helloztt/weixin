package me.jiangcai.wx.test;

import me.jiangcai.wx.model.Gender;
import me.jiangcai.wx.model.WeixinUserDetail;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Locale;
import java.util.Random;
import java.util.UUID;

/**
 * 模拟用户信息
 *
 * @author CJ
 */
public interface WeixinUserMocker {

    static WeixinUserDetail randomWeixinUserDetail() {
        Random random = new Random();
        WeixinUserDetail detail = new WeixinUserDetail();
        detail.setOpenId(UUID.randomUUID().toString().replace("-", ""));
        detail.setCity("神州");
        detail.setCountry("夷洲");
        detail.setGender(Gender.values()[random.nextInt(Gender.values().length)]);
        detail.setHeadImageUrl("http://p.ishowx.com/uploads/allimg/160928/486-16092Q45357-50.jpg");
        detail.setLocale(Locale.CHINA);
        detail.setNickname(UUID.randomUUID().toString());
        detail.setPrivilege(new String[0]);
        detail.setProvince("牛神");
        return detail;
    }

    /**
     * @param mavContainer
     * @param webRequest
     * @return 必须非空, 如果想模拟获取微信用户失败请以异常改变流程
     */
    WeixinUserDetail mockUser(ModelAndViewContainer mavContainer, NativeWebRequest webRequest);
}
