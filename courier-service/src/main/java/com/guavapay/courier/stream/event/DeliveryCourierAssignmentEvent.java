package com.guavapay.courier.stream.event;

import com.guavapay.courier.dto.DeliveryDTO;
import com.guavapay.courier.enums.DomainUpdateEventType;
import lombok.Builder;
import lombok.Data;

@Data
public class DeliveryCourierAssignmentEvent {

    private DomainUpdateEventType eventType;

    private DeliveryDTO delivery;

}
