package com.guavapay.courier.dto.request;

import com.guavapay.courier.enums.VehicleType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CourierCreateRequest {

    @NotNull
    private VehicleType vehicleType;

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String surname;

    @NotNull
    @NotEmpty
    private String phoneNumber;
}
