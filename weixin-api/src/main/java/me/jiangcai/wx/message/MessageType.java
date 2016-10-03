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
    event;

    public Class<? extends Message> messageClass() {
        if (this == text)
            return TextMessage.class;
        if (this == event)
            return EventMessage.class;
        if (this == news)
            return NewsMessage.class;
        throw new IllegalStateException("unsupported for " + this);
    }
}
