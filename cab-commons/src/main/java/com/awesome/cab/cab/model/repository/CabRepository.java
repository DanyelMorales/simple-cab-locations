package com.awesome.cab.cab.model.repository;

import com.awesome.cab.cab.model.Cab;
import com.awesome.cab.cab.model.GeoLocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CabRepository extends CrudRepository<Cab, Long> {

    Optional<List<Cab>> findAllByIsAvailableIsTrue();

    @Query("select geoLocation from Cab where email = ?1")
    GeoLocation findGeoLocationByQuery(String email);

    Optional<Cab> findByEmail(String email);

}
