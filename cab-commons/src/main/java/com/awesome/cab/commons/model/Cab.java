package com.awesome.cab.commons.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@IdClass(EmailId.class)
public class Cab {
    @Id
    @JsonProperty("driverId")
    private String email;

    @Id
    @JsonProperty("CabId")
    private long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "available")
    private boolean isAvailable;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("geoLocation")
    GeoLocation geoLocation;


}
