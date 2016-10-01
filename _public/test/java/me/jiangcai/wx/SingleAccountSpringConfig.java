package me.jiangcai.wx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author CJ
 */
@Configuration
public class SingleAccountSpringConfig {

    @Bean
    public PublicAccountSupplier publicAccountSupplier() {
        return new DebugPublicAccountSupplier();
    }
}
