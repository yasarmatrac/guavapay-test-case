package com.guavapay.delivery.stream.function;

import com.guavapay.delivery.domain.Delivery;
import com.guavapay.delivery.enums.DomainUpdateEventType;
import com.guavapay.delivery.mapper.DeliveryMapper;
import com.guavapay.delivery.stream.event.DeliveryCourierAssignmentEvent;
import com.guavapay.delivery.stream.event.DeliveryUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryEventProducer {

    private final StreamBridge streamBridge;
    private final DeliveryMapper mapper;

    public boolean sendDeliveryUpdateEvent(Delivery delivery, DomainUpdateEventType eventType) {
        var event = DeliveryUpdatedEvent
                .builder()
                .eventType(eventType)
                .delivery(mapper.map(delivery))
                .build();
        return streamBridge.send("deliveryUpdateEventFunction-out-0", event);
    }
    public boolean sendDeliveryAssignmentEvent(Delivery delivery) {
        var event = DeliveryCourierAssignmentEvent
                .builder()
                .delivery(mapper.map(delivery))
                .build();
        return streamBridge.send("deliveryAssignmentEventFunction-out-0", event);
    }

}
