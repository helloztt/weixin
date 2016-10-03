package me.jiangcai.wx;

import me.jiangcai.wx.model.PublicAccount;
import me.jiangcai.wx.model.UserAccessResponse;
import me.jiangcai.wx.model.WeixinUser;

/**
 * 微信用户服务
 *
 * @author CJ
 */
public interface WeixinUserService {

    /**
     * 获取用户详情
     *
     * @param account 公众账号
     * @param openId  openId
     * @param clazz   期望返回的数据类型
     * @param <T>     要求类型
     * @return 数据或者null
     */
    <T> T userInfo(PublicAccount account, String openId, Class<T> clazz);

    /**
     * 更新用户token
     *
     * @param account  公众账号
     * @param response 用户token信息
     */
    void updateUserToken(PublicAccount account, UserAccessResponse response);

    /**
     * 获取用户token信息
     *
     * @param openId openId
     * @return null如果没有找到的话
     */
    WeixinUser getTokenInfo(PublicAccount account, String openId);
}
