package com.kd.liftable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.kd.liftable")
public class LiftableApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiftableApplication.class, args);
    }

}
