package com.mybatis.plug;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.mybatis.plug"})
@MapperScan(basePackages = {"com.mybatis.plug.dao.mapper"})
public class MybatisServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisServerApplication.class, args);
    }

}
