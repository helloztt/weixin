package me.jiangcai.wx.standard.entity;

import lombok.Data;
import me.jiangcai.payment.PaymentForm;
import me.jiangcai.payment.entity.PayOrder;
import me.jiangcai.wx.model.pay.TradeType;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 微信统一的支付订单
 * @author helloztt
 */
@Data
public class WeixinPayOrder extends PayOrder {
    /**
     * 一段脚本可以引导支付
     */
    @Lob
    private String javascriptToPay;
    /**
     * 预支付交易会话标识
     */
    @Column(length = 64)
    private String prepayId;
    /**
     * 二维码链接
     */
    @Column(length = 64)
    private String codeUrl;

    /**
     * SUCCESS—支付成功
     * REFUND—转入退款
     * NOTPAY—未支付
     * CLOSED—已关闭
     * REVOKED—已撤销（刷卡支付）
     * USERPAYING--用户支付中
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     */
    @Column(length = 15)
    private String orderStatus;

    @Override
    public Class<? extends PaymentForm> getPaymentFormClass() {
        return null;
    }


}
