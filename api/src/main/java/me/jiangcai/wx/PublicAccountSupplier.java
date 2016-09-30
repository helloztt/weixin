package me.jiangcai.wx;

import me.jiangcai.wx.model.PublicAccount;

import java.util.List;

/**
 * 在我们的业务中,需要一个可以获取支持公众号信息的供应商;我们的API中也可以提供几个示范实现。
 * @author CJ
 */
public interface PublicAccountSupplier {

    List<PublicAccount> getAccounts();

}
