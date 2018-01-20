package me.jiangcai.wx.protocol.exception;

/**
 * 生成订单错误
 * @author helloztt
 */
public class ErrorOrderException  extends ClientException{

    public ErrorOrderException() {
        super();
    }

    public ErrorOrderException(String message) {
        super(message);
    }
}
