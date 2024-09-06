package com.sparta3tm.companyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CompanyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompanyServerApplication.class, args);
    }

}
