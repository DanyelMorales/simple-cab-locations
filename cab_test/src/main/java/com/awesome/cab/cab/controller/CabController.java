package com.awesome.cab.cab.controller;

import com.awesome.cab.cab.model.Cab;
import com.awesome.cab.cab.model.GeoLocation;
import com.awesome.cab.cab.service.CabService;
import com.awesome.cab.cab.service.Streaming;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("cabs")
public class CabController {

    @Autowired
    private Streaming streaming;

    @Autowired
    private CabService cabService;

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
        return this.cabService.createCabDriver(cab);
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
        Optional<List<Cab>> opts = this.cabService.showCabs();
        if (opts.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(opts.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
        Optional<GeoLocation> locations = this.cabService.fetchCabLocation(id);
        if (!locations.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(locations.get());
    }

    @Operation(summary = "Update cab location via kafka")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "fire and forget")})
    @PutMapping(path = "locations")
    public @ResponseBody
    ResponseEntity updateLocation(@NotNull @RequestBody Cab cabPayload, @RequestParam(value = "available", defaultValue = "true") boolean isAvailable) {
        String eventId = this.streaming.communicateLocation(cabPayload, isAvailable);
        return ResponseEntity.status(HttpStatus.OK).body(eventId);
    }

    @Operation(summary = "Find cabs near to reference point")
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
        Optional<List<Cab>> result = this.cabService.findLocationsNearTo(locationId, cabs);
        if (!result.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no free cabs near to you, try later.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(result.get());
    }


}
