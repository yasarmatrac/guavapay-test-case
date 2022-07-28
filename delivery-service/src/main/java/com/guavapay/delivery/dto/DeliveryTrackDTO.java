package com.guavapay.delivery.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class DeliveryTrackDTO {

    private LocationDTO location;
    private Instant time;
}
