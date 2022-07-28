package com.guavapay.courier.dto;

import com.guavapay.courier.enums.CourierStatus;
import com.guavapay.courier.enums.VehicleType;
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
