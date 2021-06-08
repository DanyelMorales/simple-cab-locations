package com.awesome.cab.cab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.awesome.cab.commons", "com.awesome.cab.cab.*"})
public class CabApplication {

    public static void main(String[] args) {
        SpringApplication.run(CabApplication.class, args);
    }

}
