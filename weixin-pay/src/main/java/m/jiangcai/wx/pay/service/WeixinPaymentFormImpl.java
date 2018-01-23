package m.jiangcai.wx.pay.service;

import me.jiangcai.lib.ee.ServletUtils;
import me.jiangcai.payment.PayableOrder;
import me.jiangcai.payment.entity.PayOrder;
import me.jiangcai.payment.exception.SystemMaintainException;
import me.jiangcai.payment.service.PaymentGatewayService;
import me.jiangcai.wx.couple.WeixinRequestHandlerMapping;
import me.jiangcai.wx.model.pay.TradeType;
import me.jiangcai.wx.model.pay.UnifiedOrderRequest;
import me.jiangcai.wx.model.pay.UnifiedOrderResponse;
import me.jiangcai.wx.protocol.Protocol;
import me.jiangcai.wx.protocol.event.OrderChangeEvent;
import me.jiangcai.wx.standard.entity.WeixinPayOrder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

import static me.jiangcai.wx.model.pay.TradeType.*;

/**
 * @author helloztt
 */
@Service
public class WeixinPaymentFormImpl implements WeixinPaymentForm {
    private static final Log log = LogFactory.getLog(WeixinPaymentFormImpl.class);
    @Autowired
    private PaymentGatewayService paymentGatewayService;
    @Autowired
    private WeixinRequestHandlerMapping weixinRequestHandlerMapping;
    @Autowired
    private EntityManager entityManager;

    @Override
    public PayOrder newPayOrder(HttpServletRequest request, PayableOrder order, Map<String, Object> additionalParameters) throws SystemMaintainException {
        WeixinPayOrder payOrder = new WeixinPayOrder();

        UnifiedOrderRequest orderRequest = new UnifiedOrderRequest();
        orderRequest.setBody(order.getOrderBody());
        orderRequest.setAmount(order.getOrderDueAmount());
        orderRequest.setClientIpAddress(ServletUtils.clientIpAddress(request));
//        orderRequest.setClientIpAddress("192.168.1.52");

        //交易类型，默认为jsapi
        Object tradeTypeObj = additionalParameters.get("tradeType");
        TradeType tradeType = JSAPI;
        if (tradeTypeObj == null) {
        } else if (tradeTypeObj instanceof TradeType) {
            tradeType = (TradeType) tradeTypeObj;
        } else {
            tradeType = TradeType.valueOf(tradeTypeObj.toString());
        }
        orderRequest.setTradeType(tradeType);
        try {
            UnifiedOrderResponse orderResponse = Protocol.forAccount(weixinRequestHandlerMapping.currentPublicAccount()).createUnifiedOrder(orderRequest);
            switch (tradeType) {
                case JSAPI:
                    payOrder.setPrepayId(orderResponse.getPrepayId());
                    payOrder.setJavascriptToPay(javascriptForWechatPay(orderResponse.getPrepayId()));
                    break;
                case NATIVE:
                    payOrder.setCodeUrl(orderResponse.getCodeUrl());
                    break;
                default:
                    payOrder.setPrepayId(orderResponse.getPrepayId());
            }
        } catch (Exception e) {
            log.error("生成订单出错",e);
            throw new SystemMaintainException(e);
        }
        payOrder.setPlatformId(orderRequest.getOrderNumber());
        return payOrder;
    }

    @Override
    public void orderMaintain() {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<WeixinPayOrder> criteriaQuery = criteriaBuilder.createQuery(WeixinPayOrder.class);
        Root<WeixinPayOrder> root = criteriaQuery.from(WeixinPayOrder.class);
        criteriaQuery = criteriaQuery.where(
                criteriaBuilder.and(
                        // 未成功而且未失败
                        criteriaBuilder.equal(root.get("orderStatus"), "SUCCEED")
                        , PayOrder.Success(root, criteriaBuilder).not()
                        , PayOrder.Cancel(root, criteriaBuilder).not()
                )
        );
        entityManager.createQuery(criteriaQuery)
                .getResultList()
                .forEach(order -> paymentGatewayService.paySuccess(order));
    }

    @Override
    public void orderChange(OrderChangeEvent event) {
        log.debug("trade event:" + event);
        WeixinPayOrder order = paymentGatewayService.getOrder(WeixinPayOrder.class, event.getData().getOrderNumber());
        if (order == null) {
            log.warn("received trade event without system:" + event);
            return;
        }
        order.setEventTime(LocalDateTime.now());
        order.setOrderStatus(event.getData().getTradeStatus());

        if (!order.isCancel()) {
            if ("SUCCEED".equals(order.getOrderStatus())) {
                paymentGatewayService.paySuccess(order);
            } else if ("CLOSED".equals(order.getOrderStatus())) {
                paymentGatewayService.payCancel(order);
            }
        }
    }

    private String javascriptForWechatPay(String prepayId) throws SystemMaintainException {
        try {
            return Protocol.forAccount(weixinRequestHandlerMapping.currentPublicAccount()).javascriptForWechatPay(prepayId);
        } catch (Exception e) {
            log.error("生成脚本出错",e);
            throw new SystemMaintainException(e);
        }
    }
}
