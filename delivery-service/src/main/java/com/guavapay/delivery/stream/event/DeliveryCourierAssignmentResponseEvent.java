package com.guavapay.delivery.stream.event;

import com.guavapay.delivery.dto.CourierDTO;
import com.guavapay.delivery.dto.DeliveryDTO;
import lombok.Data;

@Data
public class DeliveryCourierAssignmentResponseEvent {

    private DeliveryDTO delivery;
    private CourierDTO courier;
    private boolean isSuccess;

}
