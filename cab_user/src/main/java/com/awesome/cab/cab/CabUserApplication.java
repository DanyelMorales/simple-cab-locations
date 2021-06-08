package com.awesome.cab.cab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.awesome.cab.commons", "com.awesome.cab.cab.*"})
public class CabUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(CabUserApplication.class, args);
    }

}
