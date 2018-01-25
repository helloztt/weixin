package me.jiangcai.wx.protocol.event;

import lombok.Data;
import me.jiangcai.wx.model.pay.UnifiedOrderResponse;

import java.util.Map;

/**
 * @author helloztt
 */
@Data
public class OrderChangeEvent {
    private Map<String,String> data;
}
