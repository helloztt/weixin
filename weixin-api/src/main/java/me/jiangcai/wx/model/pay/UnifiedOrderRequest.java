package me.jiangcai.wx.model.pay;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * <a href="https://github.com/wxpay/WXPay-SDK-Java">统一下单</a>
 * @author helloztt
 */
@Data
public class UnifiedOrderRequest {
    /**
     * order_no
     * 商户订单号，在商户系统内唯一，8-20位数字或字母，不允许特殊字符
     */
    private String orderNumber = UUID.randomUUID().toString().replace("-", "");
    /**
     * 微信订单号
     */
    private String transactionId;
    /**
     * 订单总金额，大于0的数字，单位是该币种的货币单位
     */
    private BigDecimal amount;
    /**
     * 购买商品的标题，最长32位
     */
    private String subject;
    /**
     * 购买商品的描述信息，最长128个字符
     */
    private String body;
    /**
     * client_ip
     * 发起支付的客户端IP
     */
    private String clientIpAddress;
    /**
     * 交易类型
     */
    private TradeType tradeType = TradeType.JSAPI;
    /**
     * 异步回调地址
     */
    private String notifyUrl;
    /**
     * 可选的订单备注，限制300个字符内
     */
    private String description;
    // time_expire	Long	false	订单失效时间，13位Unix时间戳，默认1小时，微信公众号支付对其的限制为3分钟
    // currency	String	false	三位ISO货币代码，只支持人民币cny，默认cny
    /**
     * 可选的用户自定义元数据
     */
    private Map<String, ?> metadata;
}
