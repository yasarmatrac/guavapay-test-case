package com.guavapay.user.dto;

import com.guavapay.user.enums.CourierStatus;
import com.guavapay.user.enums.VehicleType;
import lombok.Data;

@Data
public class CourierDTO {

    private Long id;

    private VehicleType vehicleType;

    private CourierStatus courierStatus;

    private String name;

    private String surname;

    private String phoneNumber;
}
