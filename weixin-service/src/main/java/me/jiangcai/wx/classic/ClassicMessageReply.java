package me.jiangcai.wx.classic;

import me.jiangcai.wx.MessageReply;
import me.jiangcai.wx.message.EventMessage;
import me.jiangcai.wx.message.Message;
import me.jiangcai.wx.message.NewsMessage;
import me.jiangcai.wx.message.TextMessage;
import me.jiangcai.wx.message.support.NewsArticle;
import me.jiangcai.wx.model.PublicAccount;

/**
 * @author CJ
 */
public class ClassicMessageReply implements MessageReply {
    @Override
    public boolean focus(PublicAccount account, Message message) {
        return true;
    }

    @Override
    public Message reply(PublicAccount account, Message message) {
        switch (message.getType()) {
            case text:
                TextMessage message1 = new TextMessage();
                if (message instanceof TextMessage)
                    message1.setContent("回复" + ((TextMessage) message).getContent());
                else
                    message1.setContent("回复" + message.toString());
                return message1;
            case event:
                EventMessage eventMessage = (EventMessage) message;
                switch (eventMessage.getEvent()) {
                    case subscribe:
                        // 图文  百度 然后随便弄2个
                        return new NewsMessage(
                                new NewsArticle("百度", "你说呢", "http://h.hiphotos.baidu.com/image/pic/item/6609c93d70cf3bc768863fecd300baa1cd112a28.jpg", "http://www.baidu.com")
                                , new NewsArticle("糗事百科", "你说呢", "http://h.hiphotos.baidu.com/image/pic/item/6609c93d70cf3bc768863fecd300baa1cd112a28.jpg", "http://www.qiushibaike.com")
                        );
                }
                return new TextMessage("再贱!");
        }

        return null;

    }
}
