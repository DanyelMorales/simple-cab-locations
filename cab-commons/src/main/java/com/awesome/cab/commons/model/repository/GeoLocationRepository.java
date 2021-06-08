package com.awesome.cab.commons.model.repository;

import com.awesome.cab.commons.model.GeoLocation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GeoLocationRepository extends CrudRepository<GeoLocation, Long> {

}



