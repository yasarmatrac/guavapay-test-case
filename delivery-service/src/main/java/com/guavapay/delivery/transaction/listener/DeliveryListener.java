package com.guavapay.delivery.transaction.listener;

import com.guavapay.delivery.stream.function.DeliveryEventProducer;
import com.guavapay.delivery.transaction.event.DeliveryCourierAssignmentTransactionEvent;
import com.guavapay.delivery.transaction.event.DeliveryUpdatedTransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DeliveryListener {

    private final DeliveryEventProducer deliveryEventProducer;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void handleUpdatedEvent(DeliveryUpdatedTransactionEvent event) {
        var delivery = event.getDelivery();
        deliveryEventProducer.sendDeliveryUpdateEvent(delivery,event.getEventType());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void handleAssignmentEvent(DeliveryCourierAssignmentTransactionEvent event) {
        var delivery = event.getDelivery();
        deliveryEventProducer.sendDeliveryAssignmentEvent(delivery);
    }
}
