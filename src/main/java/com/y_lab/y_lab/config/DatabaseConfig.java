package com.y_lab.y_lab.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@PropertySource("classpath:application.properties")
public class DatabaseConfig {

    @Bean
    public DataSource dataSource(@Value("${spring.datasource.url}") String url,
                                 @Value("${spring.datasource.username}") String username,
                                 @Value("${spring.datasource.password}") String password,
                                 @Value("${spring.datasource.name}") String databaseName) {
        if (url != null && databaseName != null) {
            List<String> tmp = Arrays.stream(url.split("/")).toList();
            if (!tmp.get(tmp.size() - 1).equals(databaseName)) {
                url += "/" + databaseName;
            }
        }
        DriverManagerDataSource managerDataSource = new DriverManagerDataSource();
        managerDataSource.setUrl(url);
        managerDataSource.setUsername(username);
        managerDataSource.setPassword(password);
        return managerDataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
