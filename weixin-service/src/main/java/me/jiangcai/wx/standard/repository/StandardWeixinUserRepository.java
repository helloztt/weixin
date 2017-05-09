package me.jiangcai.wx.standard.repository;

import me.jiangcai.wx.standard.entity.StandardWeixinUser;
import me.jiangcai.wx.standard.entity.support.AppIdOpenID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author CJ
 */
public interface StandardWeixinUserRepository extends JpaRepository<StandardWeixinUser, AppIdOpenID>
        , JpaSpecificationExecutor<StandardWeixinUser> {

    /**
     * @param id unionId
     * @return null or 用户
     */
    StandardWeixinUser findByUnionId(String id);

    /**
     * 只在单独公众号平台下才可使用该接口
     *
     * @param openId openId
     * @return null or 用户
     */
    StandardWeixinUser findByOpenId(String openId);

}
