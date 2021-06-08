package com.awesome.cab.cab.controller;

import com.awesome.cab.cab.model.GeoLocation;
import com.awesome.cab.cab.model.User;
import com.awesome.cab.cab.service.Service;
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
    private Service userService;

    @PostMapping()
    public @ResponseBody
    User createUser(@NotNull @RequestBody User userPayload) {
        return this.userService.saveUserPayload(userPayload);
    }

    @PutMapping(path = "locations")
    public @ResponseBody
    ResponseEntity updateLocation(@NotNull @RequestBody User userPayload) {
        Optional<User> userOptional = this.userService.UpdateLocation(userPayload);
        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(userOptional.get());
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
        Optional<GeoLocation> op = userService.findGeoLocation(id);
        if (!op.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(op.get());
    }
}
