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
 * 可监控临时场景事件发生
 *
 * @author CJ
 */
public abstract class TempSceneReply implements MessageReply {

    private static final Log log = LogFactory.getLog(TempSceneReply.class);

    @Override
    public boolean focus(PublicAccount account, Message message) {
        if (message instanceof EventMessage) {
            EventMessage event = (EventMessage) message;
            if (event.getEvent() == WeixinEvent.SCAN) {
                log.debug("抓取场景事件：" + event.getKey());
                happen(account, message, NumberUtils.parseNumber(event.getKey(), Integer.class));
                return true;
            }
            if (event.getEvent() == WeixinEvent.subscribe) {
                if (!StringUtils.isEmpty(event.getKey())) {
                    if (event.getKey().startsWith("qrscene_")) {
                        final String substring = event.getKey().substring("qrscene_".length());
                        log.debug("抓取场景事件：" + substring);
                        happen(account, message, NumberUtils.parseNumber(substring, Integer.class));
                        return true;
                    }

                }
            }
        }
        return false;
    }

    /**
     * 发生了临时场景
     *
     * @param account 公众号
     * @param message 消息
     * @param sceneId 场景id
     */
    public abstract void happen(PublicAccount account, Message message, int sceneId);

    @Override
    public Message reply(PublicAccount account, Message message) {
        return null;
    }
}
