package me.jiangcai.wx.protocol.exception;

/**
 * 协议错误,按照一般的理解应该分为
 * <ul>
 * <li>客户端错误
 * <ul>
 * <li>授权错误</li>
 * <li>请求参数错误</li>
 * <li>逻辑错误</li>
 * <li>不被允许错误</li>
 * <li>连接错误</li>
 * </ul>
 * </li>
 * <li>服务端错误</li>
 * </ul>
 *
 * @author CJ
 */
public class ProtocolException extends RuntimeException {
    public ProtocolException() {
        super();
    }

    public ProtocolException(int code,String message) {
        super(message);
    }

    public ProtocolException(String message) {
        super(message);
    }

    public ProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtocolException(Throwable cause) {
        super(cause);
    }

    protected ProtocolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
