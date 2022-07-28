package com.guavapay.delivery.dto;

import com.guavapay.delivery.enums.CourierStatus;
import com.guavapay.delivery.enums.VehicleType;
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
