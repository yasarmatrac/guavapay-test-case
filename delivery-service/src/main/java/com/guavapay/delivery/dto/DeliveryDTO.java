package com.guavapay.delivery.dto;

import com.guavapay.delivery.enums.DeliveryStatus;
import lombok.Data;


@Data
public class DeliveryDTO {

    private Long id;

    private DeliveryStatus deliveryStatus;

    private AddressDTO destination;

    private Long assignedCourier;

    private String createdBy;

    private LocationDTO currentLocation;
}
