package com.guavapay.delivery.stream.function;

import com.guavapay.delivery.mapper.DeliveryMapper;
import com.guavapay.delivery.service.DeliveryService;
import com.guavapay.delivery.stream.event.DeliveryCourierAssignmentResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;


@Component
@RequiredArgsConstructor
public class DeliveryEventConsumer {


    private final DeliveryService deliveryService;
    private final DeliveryMapper deliveryMapper;


    @Bean
    public Consumer<DeliveryCourierAssignmentResponseEvent> deliveryAssignmentResponseEventConsumer() {
        return event -> {
            var delivery = event.getDelivery();
            deliveryService.accomplishAssignment(delivery.getId(), delivery.getAssignedCourier(), event.isSuccess());
        };
    }


}
