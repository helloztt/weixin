package me.jiangcai.wx.message;

/**
 * @author CJ
 */
public enum MessageType {
    text,
    /**
     * 图文
     */
    news,
    event,
    /**
     * 图片消息
     */
    image,;

    public Class<? extends Message> messageClass() {
        if (this == text)
            return TextMessage.class;
        if (this == event)
            return EventMessage.class;
        if (this == news)
            return NewsMessage.class;
        if (this == image)
            return ImageMessage.class;
        throw new IllegalStateException("unsupported for " + this);
    }
}
