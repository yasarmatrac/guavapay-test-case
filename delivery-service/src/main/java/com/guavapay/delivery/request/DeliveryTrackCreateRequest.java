package com.guavapay.delivery.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class DeliveryTrackCreateRequest {

    @NotNull
    private Long latitude;

    @NotNull
    private Long longitude;
}
