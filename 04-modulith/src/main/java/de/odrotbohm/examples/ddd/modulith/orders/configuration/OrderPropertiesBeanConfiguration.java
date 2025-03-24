package de.odrotbohm.examples.ddd.modulith.orders.configuration;

import de.odrotbohm.examples.ddd.modulith.orders.core.domain.service.OrderProperties;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OrderPropertiesConfiguration.class)
public class OrderPropertiesBeanConfiguration {
    @Bean
    public OrderProperties orderPropertiesBean(OrderPropertiesConfiguration orderPropertiesConfiguration){
        OrderProperties orderProperties = new OrderProperties();
        orderProperties.setOrderSystem(orderPropertiesConfiguration.getOrderSystem());
        return orderProperties;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
//                .setPropertyCondition(Conditions.isNotNull())
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setSkipNullEnabled(false)
                .setImplicitMappingEnabled(true);
        return modelMapper;
    }
}
