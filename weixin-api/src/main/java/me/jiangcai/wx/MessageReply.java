package me.jiangcai.wx;

import me.jiangcai.wx.message.Message;
import me.jiangcai.wx.model.PublicAccount;

/**
 * 消息回复者;可以存在多个,我们会排除掉不关注这个消息的回复者。
 *
 * @author CJ
 */
public interface MessageReply {

    /**
     * @param account
     * @param message
     * @return true 我关注此消息
     */
    boolean focus(PublicAccount account, Message message);

    /**
     * 无需设置时间,from,to之类无所谓的属性
     *
     * @param message 发给公众号的消息
     * @return null 表示无需回复
     */
    Message reply(PublicAccount account, Message message);

}
