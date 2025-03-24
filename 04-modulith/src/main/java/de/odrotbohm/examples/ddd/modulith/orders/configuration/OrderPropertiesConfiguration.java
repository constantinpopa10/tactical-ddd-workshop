package de.odrotbohm.examples.ddd.modulith.orders.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@ConfigurationProperties(prefix = "myapp.module")
@Data
public class OrderPropertiesConfiguration {

    /**
     * OrderSystem
     */
    private String orderSystem;
}
