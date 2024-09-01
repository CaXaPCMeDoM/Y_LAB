package com.y_lab.y_lab.config;

import com.y_lab.y_lab.repository.order.JdbcOrderRepository;
import com.y_lab.y_lab.repository.order.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class OrderConfig {
    @Bean
    public OrderRepository orderRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcOrderRepository(jdbcTemplate);
    }
}
