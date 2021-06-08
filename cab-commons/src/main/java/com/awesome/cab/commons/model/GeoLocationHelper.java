package com.awesome.cab.commons.model;

import com.awesome.cab.commons.tracker.GpsHelper;
import com.awesome.cab.commons.tracker.GpsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GeoLocationHelper {

    @Autowired
    private GpsHelper helper;

    List<Cab> findFreeCabs(GeoLocation referencePoint, List<Cab> freeCabs, int limit) {
        List<GpsProvider> freeCabLocations = freeCabs.stream().map(x -> x.getGeoLocation()).collect(Collectors.toList());
        Stream<GpsProvider> locationProvider = helper.nClosestPointStream(referencePoint, freeCabLocations, limit);
        List<GpsProvider> nearestCabs = locationProvider.collect(Collectors.toList());

        List<Cab> result = nearestCabs.stream().flatMap(
                x -> freeCabs.stream().filter(
                        y -> y.getGeoLocation().getId() == ((GeoLocation) x).getId()
                )
        ).collect(Collectors.toList());

        return result;
    }
}
