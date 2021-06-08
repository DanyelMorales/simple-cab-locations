package com.awesome.cab.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CabRunner implements ApplicationRunner {
    public static final Logger LOG =
            LoggerFactory.getLogger(CabRunner.class);


    @Override
    public void run(ApplicationArguments args) throws Exception {

    }


}
