package com.awesome.cab.cab;

import com.awesome.cab.cab.model.Cab;
import com.awesome.cab.cab.model.CabHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CabRunner implements ApplicationRunner {
    public static final Logger LOG =
            LoggerFactory.getLogger(CabRunner.class);

    @Autowired
    private CabHelper cabHelper;

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

    @KafkaListener(topics = "cab_location", groupId = "12")
    public void listenGroupFoo(Cab cabPayload) {
        LOG.info("received cab payload");
        Cab cab = this.cabHelper.updateLocation(cabPayload);
        if (cab != null) {
            LOG.info("saved successfully");
        } else {
            LOG.info("couldn't be saved");
        }
    }
}
