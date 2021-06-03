package com.awesome.cab.cab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CabUserRunner implements ApplicationRunner {
    public static final Logger LOG =
            LoggerFactory.getLogger(CabUserRunner.class);


    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

}
