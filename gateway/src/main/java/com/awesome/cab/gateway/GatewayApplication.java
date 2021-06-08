package com.awesome.cab.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }


    @Value("${server.servlet.context-path}")
    private String apiVersionBase;

    @Value("${routes.cabs.host}")
    private String cabAddr;
    @Value("${routes.users.host}")
    private String userAddr;

    @Value("${routes.cabs.base}")
    private String cabBase;
    @Value("${routes.users.base}")
    private String userBase;
    @Value("${routes.users.port}")
    private String userPort;
    @Value("${routes.cabs.port}")
    private String cabPort;

    @Bean
    public RouteLocator cabRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("a", p -> p
                        .method("GET", "PUT", "POST")
                        .and()
                        .path(apiVersionBase + "/cabs/**")
                        .uri(cabAddr + ":" + cabPort + cabBase)
                ).route("b", p -> p
                        .method("GET", "PUT", "POST").and()
                        .path(apiVersionBase + "/docs/cabs/**")
                        .uri(cabAddr + ":" + cabPort + apiVersionBase + "/swagger-ui.html")
                ).route("c", p -> p
                        .method("GET", "PUT", "POST").and()
                        .path(apiVersionBase + "/docs/users/**")
                        .uri(userAddr + ":" + userPort + apiVersionBase + "/swagger-ui.html")
                ).route("d", p -> p
                        .method("GET", "PUT", "POST")
                        .and()
                        .path(apiVersionBase + "/users/**")
                        .uri(userAddr + ":" + userPort + userBase)
                )
                .build();
    }
}
