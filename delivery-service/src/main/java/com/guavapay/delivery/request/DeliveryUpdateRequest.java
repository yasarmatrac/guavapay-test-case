package com.guavapay.delivery.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class DeliveryUpdateRequest {

    @NotNull
    private Long id;

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


    private String detail;

    @NotNull
    @NotEmpty
    private String receiver;

}
