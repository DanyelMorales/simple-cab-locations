package com.awesome.cab.commons.stubs;

import com.awesome.cab.commons.tracker.GpsProvider;

public class FakeProvider implements GpsProvider {
    private double longitude, latitude;
    private Long id;
    private double distance;

    public FakeProvider(Long id, double latitude, double longitude) {
        this(latitude, longitude);
        this.id = id;
    }

    public FakeProvider(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public void setDistance(double value) {
        this.distance = value;
    }

    @Override
    public double getDistance() {
        return this.distance;
    }

    public double getId() {
        return this.id;
    }
}
