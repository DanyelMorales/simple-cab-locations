package com.awesome.cab.cab.model.repository;

import com.awesome.cab.cab.model.GeoLocation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GeoLocationRepository extends CrudRepository<GeoLocation, Long> {

}



