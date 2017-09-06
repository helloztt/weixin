package me.jiangcai.wx.classic;

import me.jiangcai.wx.MessageReply;
import me.jiangcai.wx.message.EventMessage;
import me.jiangcai.wx.message.Message;
import me.jiangcai.wx.message.support.WeixinEvent;
import me.jiangcai.wx.model.PublicAccount;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

/**
 * 可监控二维码被扫场景事件发生
 *
 * @author CJ
 * @see me.jiangcai.wx.protocol.Protocol#createQRCode(int, Integer)
 * @see me.jiangcai.wx.protocol.Protocol#createQRCode(String, Integer)
 */
public abstract class TempSceneReply implements MessageReply {

    private static final Log log = LogFactory.getLog(TempSceneReply.class);

    @Override
    public boolean focus(PublicAccount account, Message message) {
        if (message instanceof EventMessage) {
            EventMessage event = (EventMessage) message;
            if (event.getEvent() == WeixinEvent.SCAN) {
                log.debug("抓取场景事件：" + event.getKey());
                try {
                    happen(account, message, NumberUtils.parseNumber(event.getKey(), Integer.class));
                } catch (NumberFormatException ex) {
                    happen(account, message, event.getKey());
                }
                return true;
            }
            if (event.getEvent() == WeixinEvent.subscribe) {
                if (!StringUtils.isEmpty(event.getKey())) {
                    if (event.getKey().startsWith("qrscene_")) {
                        final String substring = event.getKey().substring("qrscene_".length());
                        log.debug("抓取场景事件：" + substring);
                        try {
                            happen(account, message, NumberUtils.parseNumber(substring, Integer.class));
                        } catch (NumberFormatException ex) {
                            happen(account, message, substring);
                        }
                        return true;
                    }

                }
            }
        }
        return false;
    }

    /**
     * 发生了扫码场景id的事件
     *
     * @param account 公众号
     * @param message 消息
     * @param sceneId 场景id
     * @see me.jiangcai.wx.protocol.Protocol#createQRCode(int, Integer)
     */
    public abstract void happen(PublicAccount account, Message message, int sceneId);

    /**
     * 发生了扫码字符场景的事件
     *
     * @param account  公众号
     * @param message  消息
     * @param sceneStr 场景str
     * @see me.jiangcai.wx.protocol.Protocol#createQRCode(String, Integer)
     */
    public abstract void happen(PublicAccount account, Message message, String sceneStr);

    @Override
    public Message reply(PublicAccount account, Message message) {
        return null;
    }
}
