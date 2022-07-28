package com.guavapay.delivery.stream.event;

import com.guavapay.delivery.dto.DeliveryDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryCourierAssignmentEvent {

    private DeliveryDTO delivery;
}
