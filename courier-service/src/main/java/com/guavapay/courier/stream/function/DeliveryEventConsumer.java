package com.guavapay.courier.stream.function;


import com.guavapay.courier.mapper.CourierMapper;
import com.guavapay.courier.service.CourierService;
import com.guavapay.courier.stream.event.CourierUserCreateResponseEvent;
import com.guavapay.courier.stream.event.DeliveryCourierAssignmentEvent;
import com.guavapay.courier.stream.event.DeliveryCourierAssignmentResponseEvent;
import com.guavapay.courier.stream.event.DeliveryUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DeliveryEventConsumer {

    private final CourierService courierService;
    private final CourierMapper courierMapper;

    @Bean
    public Function<DeliveryCourierAssignmentEvent, DeliveryCourierAssignmentResponseEvent> deliveryCourierAssignmentEventFunction() {
        return input -> {
            var courierAssignment = courierService.assignCourier(input.getDelivery());
            return DeliveryCourierAssignmentResponseEvent.builder()
                    .delivery(input.getDelivery())
                    .courier(courierMapper.map(courierAssignment.getCourier()))
                    .isSuccess(courierAssignment.isSuccess())
                    .build();
        };
    }


    @Bean
    public Consumer<DeliveryUpdatedEvent> deliveryUpdatesConsumer() {
        return event -> {
            courierService.handleDeliveryEvent(event.getDelivery());
        };
    }

    @Bean
    public Consumer<CourierUserCreateResponseEvent> courierUserCreateResponseEventConsumer() {
        return event -> {
            courierService.setCourierUserInfos(event.getCourier().getId(),event.getUser().getId());
        };
    }

}
