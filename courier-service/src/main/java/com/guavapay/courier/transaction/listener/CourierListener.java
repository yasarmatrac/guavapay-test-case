package com.guavapay.courier.transaction.listener;

import com.guavapay.courier.enums.DomainUpdateEventType;
import com.guavapay.courier.stream.function.CourierEventProducer;
import com.guavapay.courier.transaction.event.CourierUpdatedTransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CourierListener {

    private final CourierEventProducer producer;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void handleCourierUpdatedEvent(CourierUpdatedTransactionEvent event) {
        if (event.getDomainUpdateEventType() == DomainUpdateEventType.CREATE) {
            producer.sendCourierUserCreateEvent(event.getCourier(), event.getUsername(), event.getPassword());
        }
    }


}
