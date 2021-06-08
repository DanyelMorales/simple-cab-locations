/**
 * This class aims to interface between actors behaving like Gps point providers and  a reference point, that way
 * it will be easy to resolve closer points.
 *
 * Main calculation formula is backed into GpsProvider interface as a default method.
 */
package com.awesome.cab.commons.tracker;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class GpsHelper {

    public double calcDistance(GpsProvider startingPoint, GpsProvider target) {
        return startingPoint.calculateDistance(target);
    }

    private Stream<GpsProvider> fetchGpsPoints(GpsProvider startingPoint, List<GpsProvider> targets, Comparator<Double> cmp, Integer limit) {
        Stream<GpsProvider> gpsStream = targets.stream().sorted(Comparator.comparing(x -> startingPoint.calculateDistance(x), cmp));
        if (limit > 0) {
            return gpsStream.limit(limit);
        }
        return gpsStream;
    }

    public Stream<GpsProvider> nClosestPointStream(GpsProvider startingPoint, List<GpsProvider> targets, Integer limit) {
        return fetchGpsPoints(startingPoint, targets, Comparator.naturalOrder(), limit);
    }

    public Stream<GpsProvider> nFarthestPointStream(GpsProvider startingPoint, List<GpsProvider> targets, Integer limit) {
        return fetchGpsPoints(startingPoint, targets, Comparator.reverseOrder(), limit);
    }

    public Optional<GpsProvider> calcClosestPoint(GpsProvider startingPoint, List<GpsProvider> targets) {
        return nClosestPointStream(startingPoint, targets, 1).findFirst();
    }

    public Optional<GpsProvider> calcFarthestPoint(GpsProvider startingPoint, List<GpsProvider> targets) {
        return nFarthestPointStream(startingPoint, targets, 1).findFirst();
    }
}
