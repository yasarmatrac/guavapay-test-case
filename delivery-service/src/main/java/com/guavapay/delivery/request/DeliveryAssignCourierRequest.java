package com.guavapay.delivery.request;

import lombok.Data;

@Data
public class DeliveryAssignCourierRequest {

    private Long deliveryId;
    private Long courierId;

}
