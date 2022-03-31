package com.wxxx.gis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wxxx.gis.mapper")
public class GisWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GisWebApplication.class, args);
    }

}
