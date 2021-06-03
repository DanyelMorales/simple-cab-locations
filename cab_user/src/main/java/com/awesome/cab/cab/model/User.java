package com.awesome.cab.cab.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@IdClass(EmailId.class)
public class User {

    @Id
    private String email;

    @Id
    @JsonProperty("customerId")
    private long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("geoLocation")
    GeoLocation geoLocation;

}
