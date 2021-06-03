package com.awesome.cab.cab.controller;

import com.awesome.cab.cab.CabUserRunner;
import com.awesome.cab.cab.model.GeoLocation;
import com.awesome.cab.cab.model.User;
import com.awesome.cab.cab.model.repository.GeoLocationRepository;
import com.awesome.cab.cab.model.repository.UserRepository;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private GeoLocationRepository geoRepo;

    @PostMapping()
    public @ResponseBody
    User createUser(@NotNull @RequestBody User userPayload) {
        return userRepo.save(userPayload);
    }

    @PutMapping(path = "locations")
    public @ResponseBody
    ResponseEntity updateLocation(@NotNull @RequestBody User userPayload) {
        if (userPayload.getGeoLocation() != null) {
            Optional<User> userOpt = userRepo.findByEmail(userPayload.getEmail());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                userPayload.setId(user.getId());
                geoRepo.save(userPayload.getGeoLocation());
                userRepo.save(userPayload);
                CabUserRunner.LOG.info("geo location added successfully");
                return ResponseEntity.status(HttpStatus.OK).body(user);
            } else {
                CabUserRunner.LOG.debug("no user with id " + userPayload.getId());
            }
        } else {
            CabUserRunner.LOG.debug("No geo location in the payload");
        }

        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(Problem.create()
                        .withTitle("Content not allowed")
                        .withDetail("either new location is empty or attempted user is not longer there"));
    }

    @GetMapping(path = "{email}/locations")
    public @ResponseBody
    ResponseEntity<Object> fetchLocations(@PathVariable("email") String id) {
        GeoLocation r = userRepo.findGeoLocationByQuery(id);
        if (r == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(r);
    }
}
