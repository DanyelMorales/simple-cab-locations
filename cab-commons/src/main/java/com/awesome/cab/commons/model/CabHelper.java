package com.awesome.cab.commons.model;

import com.awesome.cab.commons.CabRunner;
import com.awesome.cab.commons.model.repository.CabRepository;
import com.awesome.cab.commons.model.repository.GeoLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CabHelper {

    @Autowired
    private GeoLocationRepository geoRepo;
    @Autowired
    private CabRepository cabRepo;
    @Autowired
    private GeoLocationHelper geoLocationHelper;

    public Cab updateLocation(Cab cabPayload) {
        if (cabPayload.getGeoLocation() != null) {
            Optional<Cab> cabOpt = cabRepo.findByEmail(cabPayload.getEmail());
            if (cabOpt.isPresent()) {
                Cab cab = cabOpt.get();
                cabPayload.setId(cab.getId());
                geoRepo.save(cabPayload.getGeoLocation());
                cabRepo.save(cabPayload);
                CabRunner.LOG.info("geo location added successfully");
                return cab;
            } else {
                CabRunner.LOG.info("no cab with id " + cabPayload.getId());
                return null;
            }
        }
        CabRunner.LOG.info("No geo location in the payload");
        return null;
    }


    public List<Cab> findGeoLocation(Long locationId, int limit) {
        Optional<GeoLocation> location = geoRepo.findById(locationId);
        if (!location.isPresent()) {
            CabRunner.LOG.debug("no location with id " + locationId);
            return null;
        }
        GeoLocation referencePoint = location.get();

        Optional<List<Cab>> freeCabsOpt = this.cabRepo.findAllByIsAvailableIsTrue();
        if (!freeCabsOpt.isPresent()) {
            CabRunner.LOG.debug("no cabs free right now ");
            return null;
        }
        List<Cab> freeCabs = freeCabsOpt.get();

        if (freeCabs.isEmpty()) {
            CabRunner.LOG.debug("no cabs free right now ");
            return null;
        }
        CabRunner.LOG.info("evaluating " + freeCabs.size() + " free cabs");
        List<Cab> results = geoLocationHelper.findFreeCabs(referencePoint, freeCabs, limit);
        CabRunner.LOG.info("found " + results.size() + " free near cabs");
        return results;
    }

}
