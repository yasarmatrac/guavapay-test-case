package com.guavapay.delivery.transaction.event;

import com.guavapay.delivery.domain.Delivery;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryCourierAssignmentTransactionEvent {

    private Delivery delivery;
}
