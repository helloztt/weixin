package me.jiangcai.wx.protocol;

import me.jiangcai.wx.WeixinUserService;
import me.jiangcai.wx.model.Menu;
import me.jiangcai.wx.model.PublicAccount;
import me.jiangcai.wx.model.Template;
import me.jiangcai.wx.model.WeixinUserDetail;
import me.jiangcai.wx.protocol.exception.ProtocolException;
import me.jiangcai.wx.protocol.impl.ProtocolCallback;
import org.springframework.cglib.proxy.Enhancer;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author CJ
 */
public interface Protocol {

    static Protocol forAccount(PublicAccount account) {
        // AOP 一下 呵呵
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{Protocol.class});
        enhancer.setCallback(new ProtocolCallback(account));
        return (Protocol) enhancer.create();
    }

    /**
     * 建立菜单
     *
     * @param menus 最多3个,必选
     * @throws ProtocolException
     */
    void createMenu(Menu[] menus) throws ProtocolException;

    /**
     * javascript签名
     *
     * @param timestamp 当前时间戳(单位需为秒)
     * @param nonceStr  随机字符串
     * @param url       当前URL完整地址
     * @return 签名
     */
    String javascriptSign(String timestamp, String nonceStr, String url) throws ProtocolException;

    /**
     * 更新{@link PublicAccount#javascriptTicket}
     */
    void getJavascriptTicket() throws ProtocolException;

    /**
     * 我要获得基本用户信息
     *
     * @param url   原url
     * @param clazz 希望获取的数据类型
     * @return 新url
     */
    String redirectUrl(String url, Class clazz);

    /**
     * 获取用户的{@link me.jiangcai.wx.model.WeixinUserDetail#openId}
     *
     * @param code              可以获取的code,用好就丢
     * @param weixinUserService 用于维护用户信息的服务
     * @return openId
     * @throws ProtocolException
     */
    String userToken(String code, WeixinUserService weixinUserService) throws ProtocolException;

    /**
     * 获取用户详情
     *
     * @param openId            openId
     * @param weixinUserService 服务
     * @return 用户详情
     * @throws me.jiangcai.wx.protocol.exception.BadAuthAccessException 刷不出可用的Token或者Scope不行
     * @throws ProtocolException
     */
    WeixinUserDetail userDetail(String openId, WeixinUserService weixinUserService) throws ProtocolException;

    /**
     * 寻找某一个消息模板
     *
     * @param predicate 过滤器
     * @return 返回任意一个符合过滤器的
     * @throws ProtocolException
     */
    Optional<Template> findTemplate(Predicate<Template> predicate) throws ProtocolException;

    /**
     * 发送消息模板
     *
     * @param openId     收件人的openId;只有关注了公众号的才可以接受到模板消息
     * @param templateId 模板id
     * @param url        这个消息相关的url
     * @param parameters 参数,参数可不需要什么.DATA
     */
    void sendTemplate(String openId, String templateId, String url, TemplateParameter... parameters) throws ProtocolException;
}
