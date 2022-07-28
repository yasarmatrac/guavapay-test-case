package com.guavapay.courier.dto;

import com.guavapay.courier.enums.DeliveryStatus;
import lombok.Data;

@Data
public class DeliveryDTO {

    private Long id;

    private DeliveryStatus deliveryStatus;

    private AddressDTO destination;

    private Long assignedCourier;

    private String createdBy;
}