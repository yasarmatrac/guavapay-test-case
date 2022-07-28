package com.guavapay.delivery.stream.event;

import com.guavapay.delivery.dto.DeliveryDTO;
import com.guavapay.delivery.enums.DomainUpdateEventType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryUpdatedEvent {

    private DomainUpdateEventType eventType;

    private DeliveryDTO delivery;
}
