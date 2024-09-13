package com.y_lab.y_lab.config;

import com.y_lab.y_lab.out.Exporter;
import com.y_lab.y_lab.out.file.FileExport;
import com.y_lab.y_lab.repository.car.CarRepository;
import com.y_lab.y_lab.repository.car.JdbcCarRepository;
import com.y_lab.y_lab.repository.order.JdbcOrderRepository;
import com.y_lab.y_lab.repository.order.OrderRepository;
import com.y_lab.y_lab.repository.user.JdbcUserRepository;
import com.y_lab.y_lab.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ApplicationConfig {
    @Bean
    public OrderRepository orderRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcOrderRepository(jdbcTemplate);
    }

    @Bean
    public CarRepository carRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcCarRepository(jdbcTemplate);
    }

    @Bean
    public Exporter exporterService(@Value("${app.audit.export.filename}") String filename) {
        return new FileExport(filename);
    }

    @Bean
    public UserRepository userRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcUserRepository(jdbcTemplate);
    }
}
