package com.awesome.cab.cab.model;

import com.awesome.cab.cab.tracker.GpsProvider;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class GeoLocation implements GpsProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonInclude()
    private long id;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    private double distance;

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
        distance = value;
    }

    @Transient()
    @Override
    public double getDistance() {
        return distance;
    }
}
