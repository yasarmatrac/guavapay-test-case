package com.guavapay.delivery.client;

import com.guavapay.delivery.dto.CourierDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Component
public class CourierServiceClient {

    private final WebClient courierWebClient;

    public CourierServiceClient(final WebClient courierWebClient) {
        this.courierWebClient = courierWebClient;
    }

    public Optional<CourierDTO> findCourierByUserId(Long userId) {
        return this.courierWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/courier/get-one")
                        .queryParam("userId", userId)
                        .build()
                )
                .retrieve()
                .bodyToMono(CourierDTO.class)
                .blockOptional();
    }

    public Optional<CourierDTO> getLoggedCourier() {
        return this.courierWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/courier/me")
                        .build()
                )
                .retrieve()
                .bodyToMono(CourierDTO.class)
                .blockOptional();
    }
}
