package m.jiangcai.wx;

import me.jiangcai.payment.PaymentConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author helloztt
 */
@Configuration
@Import(PaymentConfig.class)
@ComponentScan("me.jiangcai.wx.pay.service")
public class WeixinPayConfig {
}
