package com.sparta3tm.choreserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
public class ChoreServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChoreServerApplication.class, args);
    }

}
