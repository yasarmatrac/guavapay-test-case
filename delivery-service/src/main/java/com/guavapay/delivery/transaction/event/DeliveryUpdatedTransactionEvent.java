package com.guavapay.delivery.transaction.event;

import com.guavapay.delivery.domain.Delivery;
import com.guavapay.delivery.enums.DomainUpdateEventType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryUpdatedTransactionEvent {

    private DomainUpdateEventType eventType;
    private Delivery delivery;
}
