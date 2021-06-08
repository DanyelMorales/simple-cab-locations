package com.awesome.cab.cab.service;

import com.awesome.cab.cab.CabUserRunner;
import com.awesome.cab.commons.model.GeoLocation;
import com.awesome.cab.commons.model.User;
import com.awesome.cab.commons.model.repository.GeoLocationRepository;
import com.awesome.cab.commons.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GeoLocationRepository geoRepo;

    public Optional<User> UpdateLocation(User userPayload) {
        if (userPayload.getGeoLocation() != null) {
            Optional<User> userOpt = userRepo.findByEmail(userPayload.getEmail());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                userPayload.setId(user.getId());
                geoRepo.save(userPayload.getGeoLocation());
                userRepo.save(userPayload);
                CabUserRunner.LOG.info("geo location added successfully");
                return Optional.of(userPayload);
            } else {
                CabUserRunner.LOG.debug("no user with id " + userPayload.getId());
            }
        } else {
            CabUserRunner.LOG.debug("No geo location in the payload");
        }

        return Optional.empty();
    }

    public Optional<GeoLocation> findGeoLocation(String id) {
        GeoLocation r = userRepo.findGeoLocationByQuery(id);
        if (r == null) {
            return Optional.empty();
        }
        return Optional.of(r);
    }

    public User saveUserPayload(User userPayload){
        return userRepo.save(userPayload);
    }

}
