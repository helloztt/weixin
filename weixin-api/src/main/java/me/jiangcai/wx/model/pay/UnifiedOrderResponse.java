package me.jiangcai.wx.model.pay;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author helloztt
 */
@Data
public class UnifiedOrderResponse {
    /**
     * 预支付交易会话标识 微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
     */
    private String prepayId;
    /**
     * 二维码链接 trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行扫码支付
     */
    private String codeUrl;
    /**
     * openId
     */
    private String openId;
    /**
     * 订单总金额
     */
    private BigDecimal totalFee;
    /**
     * 交易状态
     */
    private String tradeStatus;
    /**
     * 付款银行
     */
    private String bankType;
    /**
     * 微信订单号
     */
    private String transactionId;
    private String orderNumber;
    /**
     * 订单支付时间
     */
    private LocalDateTime payTime;


}
