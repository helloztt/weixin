package me.jiangcai.wx.protocol;

import me.jiangcai.wx.model.Menu;
import me.jiangcai.wx.model.PublicAccount;
import me.jiangcai.wx.protocol.exception.ProtocolException;
import me.jiangcai.wx.protocol.impl.ProtocolCallback;
import org.springframework.cglib.proxy.Enhancer;

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
}
