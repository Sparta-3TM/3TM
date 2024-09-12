package com.sparta3tm.choreserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(
        scanBasePackages = {
                "com.sparta3tm.choreserver",
                "com.sparta3tm.common"
        }
)
@EnableScheduling
public class ChoreServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChoreServerApplication.class, args);
    }

}
