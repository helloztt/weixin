package me.jiangcai.wx.standard.service;

import me.jiangcai.wx.WeixinUserService;
import me.jiangcai.wx.model.PublicAccount;
import me.jiangcai.wx.model.UserAccessResponse;
import me.jiangcai.wx.model.WeixinUser;
import me.jiangcai.wx.model.WeixinUserDetail;
import me.jiangcai.wx.protocol.Protocol;
import me.jiangcai.wx.standard.entity.StandardWeixinUser;
import me.jiangcai.wx.standard.entity.support.AppIdOpenID;
import me.jiangcai.wx.standard.repository.StandardWeixinUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author CJ
 */
@Service
public class StandardWeixinUserService implements WeixinUserService {

    @Autowired
    private StandardWeixinUserRepository standardWeixinUserRepository;

    @Override
    public <T> T userInfo(PublicAccount account, String openId, Class<T> clazz, Object data) {
        // 建议版本就不保存什么了 直接使用微信返回的
        if (clazz == String.class)
            //noinspection unchecked
            return (T) openId;
        if (clazz == WeixinUserDetail.class) {
            StandardWeixinUser user = standardWeixinUserRepository.findOne(new AppIdOpenID(account.getAppID(), openId));
            // 是否需要刷新？
            if (user == null
                    || user.getLastRefreshDetailTime() == null
                    || (user.getLastRefreshDetailTime().isBefore(LocalDateTime.now().minusMonths(1)) && user.isAbleDetail())
                    ) {
                WeixinUserDetail detail = Protocol.forAccount(account).userDetail(openId, this, data);
                // 拿到详情了
                if (user == null) {
                    user = new StandardWeixinUser();
                }
                user.setLastRefreshDetailTime(LocalDateTime.now());
                WeixinUserDetail.Copy(user, detail);
                //noinspection unchecked
                return (T) standardWeixinUserRepository.save(user);
            }
            //noinspection unchecked
            return (T) user;
        }

        throw new IllegalArgumentException(("unsupported type:" + clazz));
    }

    @Override
    public void updateUserToken(PublicAccount account, UserAccessResponse response, Object data) {
// 这个必须保存的
        StandardWeixinUser user
                = standardWeixinUserRepository.findOne(new AppIdOpenID(account.getAppID(), response.getOpenId()));
        if (user == null) {
            user = new StandardWeixinUser();
            user.setAppId(account.getAppID());
            user.setOpenId(response.getOpenId());
        }
        user.setAccessToken(response.getAccessToken());
        user.setRefreshToken(response.getRefreshToken());
        user.setTokenScopes(response.getScope());

        LocalDateTime dateTime = LocalDateTime.now();
        dateTime.plusSeconds(response.getTime());
        user.setAccessTimeToExpire(dateTime);
        standardWeixinUserRepository.save(user);
    }

    @Override
    public WeixinUser getTokenInfo(PublicAccount account, String openId) {
        return standardWeixinUserRepository.findOne(new AppIdOpenID(account.getAppID(), openId));
    }
}
