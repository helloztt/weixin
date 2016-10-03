package me.jiangcai.wx;

import me.jiangcai.wx.model.PublicAccount;

import java.util.List;

/**
 * 在我们的业务中,需要一个可以获取支持公众号信息的供应商;我们的API中也可以提供几个示范实现。
 *
 * @author CJ
 */
public interface PublicAccountSupplier {

    /**
     * @return 所有支持的公众号
     */
    List<PublicAccount> getAccounts();

    /**
     * 此处并没有定义识别符具体是什么,由开发者在客户系统中厘定,原则是
     * <ul>
     * <li>识别符是识别唯一的</li>
     * <li>定义在同一个系统是不可更变的</li>
     * </ul>
     *
     * @param identifier 唯一,识别符
     * @return 非空
     * @throws IllegalArgumentException 如果传入的识别符没有找到公众号
     */
    PublicAccount findByIdentifier(String identifier);

    /**
     * 根据域名获取公众号,这个可作为可选实现,除非你的应用同时工作在微信页面中。
     *
     * @param host 域名
     * @return 非空公众号
     * @throws IllegalArgumentException 没有找到公众号
     */
    PublicAccount findByHost(String host);
}
