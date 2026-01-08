package com.logistics;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.logistics.mapper")
@EnableScheduling
public class LogisticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogisticsApplication.class, args);
    }
}
