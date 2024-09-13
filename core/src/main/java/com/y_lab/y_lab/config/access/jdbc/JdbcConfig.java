package com.y_lab.y_lab.config.access.jdbc;

import com.y_lab.y_lab.repository.audit.AuditRepository;
import com.y_lab.y_lab.repository.audit.JdbcAuditRepository;
import com.y_lab.y_lab.repository.car.CarRepository;
import com.y_lab.y_lab.repository.car.JdbcCarRepository;
import com.y_lab.y_lab.repository.order.JdbcOrderRepository;
import com.y_lab.y_lab.repository.order.OrderRepository;
import com.y_lab.y_lab.repository.user.JdbcUserRepository;
import com.y_lab.y_lab.repository.user.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(prefix = "app.type.database", name = "access_type", havingValue = "jdbc")
public class JdbcConfig {
    @Bean
    public AuditRepository jdbcAuditRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcAuditRepository(jdbcTemplate);
    }

    @Bean
    public CarRepository jdbcCarRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcCarRepository(jdbcTemplate);
    }

    @Bean
    public OrderRepository jdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcOrderRepository(jdbcTemplate);
    }

    @Bean
    public UserRepository jdbcUserRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcUserRepository(jdbcTemplate);
    }
}
