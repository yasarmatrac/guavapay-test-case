package com.guavapay.delivery.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class DeliveryCreateRequest {

    @NotNull
    @NotEmpty
    private String country;

    @NotNull
    @NotEmpty
    private String city;

    @NotNull
    @NotEmpty
    private String district;

    @NotNull
    @NotEmpty
    private String street;

    @NotNull
    @NotEmpty
    private String receiver;

    private String detail;

}
