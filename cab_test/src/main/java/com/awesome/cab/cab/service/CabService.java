package com.awesome.cab.cab.service;

import com.awesome.cab.cab.CabRunner;
import com.awesome.cab.commons.model.Cab;
import com.awesome.cab.commons.model.CabHelper;
import com.awesome.cab.commons.model.GeoLocation;
import com.awesome.cab.commons.model.repository.CabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class CabService {

    @Autowired
    private CabRepository cabRepo;
    @Autowired
    private CabHelper cabHelper;


    public Cab createCabDriver(Cab cab) {
        return cabRepo.save(cab);
    }

    public Optional<List<Cab>> showCabs() {
        ArrayList<Cab> cabs = new ArrayList<>();
        cabRepo.findAll().forEach(x -> cabs.add(x));
        if (cabs.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(cabs);
    }

    public Optional<GeoLocation> fetchCabLocation(String id) {
        GeoLocation r = cabRepo.findGeoLocationByQuery(id);
        if (r == null) {
            CabRunner.LOG.debug("no cab with id " + id);
            return Optional.empty();
        }
        CabRunner.LOG.info("awesome, responding locations");
        return Optional.of(r);
    }

    public Optional<List<Cab>> findLocationsNearTo(Long reference, int maxNumberOfCabs) {
        List<Cab> result = cabHelper.findGeoLocation(reference, maxNumberOfCabs);
        if (result == null || result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result);
    }

    @KafkaListener(topicPattern = "cab_location", groupId = "12")
    public void listenGroupFoo(Cab cabPayload) {
        CabRunner.LOG.info("received cab payload");
        Cab cab = this.cabHelper.updateLocation(cabPayload);
        if (cab != null) {
            CabRunner.LOG.info("saved successfully");
        } else {
            CabRunner.LOG.info("couldn't be saved");
        }
    }
}