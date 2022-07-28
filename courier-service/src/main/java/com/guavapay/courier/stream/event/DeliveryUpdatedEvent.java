package com.guavapay.courier.stream.event;

import com.guavapay.courier.dto.DeliveryDTO;
import com.guavapay.courier.enums.DomainUpdateEventType;
import lombok.Data;

@Data
public class DeliveryUpdatedEvent {

    private DeliveryDTO delivery;
}
