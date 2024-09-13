package com.y_lab.y_lab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import y_lab.EnableLoggable;

@SpringBootApplication
@EnableLoggable
public class YLabApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(YLabApplication.class, args);
    }
}
