package com.guavapay.courier.stream.event;

import com.guavapay.courier.dto.CourierDTO;
import com.guavapay.courier.dto.DeliveryDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryCourierAssignmentResponseEvent {

    private DeliveryDTO delivery;
    private CourierDTO courier;
    private boolean isSuccess;

}
