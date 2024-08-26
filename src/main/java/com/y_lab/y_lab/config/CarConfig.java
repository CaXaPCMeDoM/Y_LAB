package com.y_lab.y_lab.config;

import com.y_lab.y_lab.repository.car.CarRepository;
import com.y_lab.y_lab.repository.car.JdbcCarRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class CarConfig {
    public CarRepository carRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcCarRepository(jdbcTemplate);
    }
}
