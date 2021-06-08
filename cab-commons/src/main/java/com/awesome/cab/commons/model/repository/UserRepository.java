package com.awesome.cab.commons.model.repository;

import com.awesome.cab.commons.model.GeoLocation;
import com.awesome.cab.commons.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("select geoLocation from User where email = ?1")
    GeoLocation findGeoLocationByQuery(String email);

    Optional<User> findByEmail(String email);
}
