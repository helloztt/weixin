package me.jiangcai.wx.protocol.event;

import lombok.Data;
import me.jiangcai.wx.model.pay.UnifiedOrderResponse;

/**
 * @author helloztt
 */
@Data
public class OrderChangeEvent {
    private UnifiedOrderResponse data;
}
