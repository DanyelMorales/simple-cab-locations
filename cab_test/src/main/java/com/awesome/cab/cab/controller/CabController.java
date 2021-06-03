package com.awesome.cab.cab.controller;

import com.awesome.cab.cab.CabRunner;
import com.awesome.cab.cab.model.Cab;
import com.awesome.cab.cab.model.CabHelper;
import com.awesome.cab.cab.model.GeoLocation;
import com.awesome.cab.cab.model.repository.CabRepository;
import com.awesome.cab.cab.model.repository.GeoLocationRepository;
import com.awesome.cab.cab.tracker.GpsHelper;
import com.awesome.cab.cab.tracker.GpsProvider;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("cabs")
public class CabController {
    @Value("${docker.kafka.topic}")
    private String cabTopic;

    @Autowired
    private GeoLocationRepository geoRepo;
    @Autowired
    private CabRepository cabRepo;


    @Autowired
    private CabHelper cabHelper;

    @Autowired
    private KafkaTemplate<String, Cab> kafkaTemplate;

    @Operation(summary = "Create a cab driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cab.class))}),
            @ApiResponse(responseCode = "408", description = "Invalid email supplied",
                    content = @Content)
    })
    @PostMapping()
    public @ResponseBody
    Cab createCabDriver(@NotNull @RequestBody Cab cab) {
        return cabRepo.save(cab);
    }

    @Operation(summary = "List all cabs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully listing current cabs",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cab.class))}),
            @ApiResponse(responseCode = "404", description = "cabs not found",
                    content = @Content)})
    @GetMapping()
    public @ResponseBody
    ResponseEntity showCabs() {
        ArrayList<Cab> cabs = new ArrayList<>();
        cabRepo.findAll().forEach(x -> cabs.add(x));
        return ResponseEntity.status(HttpStatus.OK).body(cabs);
    }

    @Operation(summary = "Get current location of cab by its email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the cab",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cab.class))}),
            @ApiResponse(responseCode = "404", description = "Cab not found",
                    content = @Content)})
    @GetMapping(path = "{email}/locations")
    public @ResponseBody
    ResponseEntity fetchLocations(@PathVariable("email") String id) {
        GeoLocation r = cabRepo.findGeoLocationByQuery(id);
        if (r == null) {
            CabRunner.LOG.debug("no cab with id " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        CabRunner.LOG.info("awesome, responding locations");
        return ResponseEntity.status(HttpStatus.OK).body(r);
    }

    @Operation(summary = "Update cab location via kafka")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "fire and forget")})
    @PutMapping(path = "locations")
    public @ResponseBody
    ResponseEntity updateLocation(@NotNull @RequestBody Cab cabPayload) {
        ProducerRecord<String, Cab> producerRecord = new ProducerRecord<>(cabTopic, cabPayload);
        producerRecord.headers().add("eventName", "CAB-LOCAION".getBytes(StandardCharsets.UTF_8));
        producerRecord.headers().add("timestamp", new Date().toString().getBytes(StandardCharsets.UTF_8));
        producerRecord.headers().add("eventID", "3b719a3a-bc3f-43a8-b311-94754a3a4d95".getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(producerRecord);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Find n near cabs near to you")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found a list of cabs",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cab.class))}),
            @ApiResponse(responseCode = "404", description = "No cabs available near to you",
                    content = @Content)})
    @GetMapping("locations/{locationId}")
    public @ResponseBody
    ResponseEntity fetchLocations(@PathVariable("locationId") Long locationId,
                                  @RequestParam(value = "cabs", defaultValue = "1") int cabs) {
        List<Cab> result = cabHelper.findGeoLocation(locationId, cabs);
        if (result == null || result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no free cabs near to you, try later.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
