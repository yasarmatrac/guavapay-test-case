package com.guavapay.delivery;

import com.guavapay.delivery.client.CourierServiceClient;
import com.guavapay.delivery.domain.Delivery;
import com.guavapay.delivery.enums.DeliveryStatus;
import com.guavapay.delivery.repository.DeliveryRepository;
import com.guavapay.delivery.repository.DeliveryStatusHistoryRepository;
import com.guavapay.delivery.request.DeliveryCreateRequest;
import com.guavapay.delivery.security.SecurityService;
import com.guavapay.delivery.service.DeliveryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;


@SpringBootTest
public class DeliveryServiceTest {

    @Autowired
    private DeliveryService deliveryService;

    @MockBean
    private DeliveryRepository deliveryRepository;

    @MockBean
    private DeliveryStatusHistoryRepository deliveryStatusHistoryRepository;

    @MockBean
    private ApplicationEventPublisher applicationEventPublisher;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private CourierServiceClient courierServiceClient;

    @Test
    public void deliveryShouldBeCreated() {
        final long userId = 1l;
        final var deliveryCreateRequest = DeliveryCreateRequest.builder()
                .country("Turkey")
                .city("İstanbul")
                .street("Kitabe Sokak")
                .district("Başakşehir")
                .receiver("Hamza Matraç")
                .build();
        Mockito.when(securityService.getAuthenticatedUserId()).thenReturn(Optional.ofNullable(userId));
        Mockito.when(deliveryRepository.save(Mockito.any(Delivery.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        var delivery = deliveryService.create(deliveryCreateRequest);
        Assertions.assertEquals(delivery.getDeliveryStatus(), DeliveryStatus.CREATED);
        Assertions.assertEquals(delivery.getReceiver(), deliveryCreateRequest.getReceiver());
        Assertions.assertEquals(delivery.getDestination().getCity(), deliveryCreateRequest.getCity());
        Assertions.assertEquals(delivery.getDestination().getCountry(), deliveryCreateRequest.getCountry());
        Assertions.assertEquals(delivery.getDestination().getDistrict(), deliveryCreateRequest.getDistrict());
        Assertions.assertEquals(delivery.getDestination().getDetail(), deliveryCreateRequest.getDetail());
        Assertions.assertEquals(delivery.getDestination().getStreet(), deliveryCreateRequest.getStreet());
        Assertions.assertEquals(delivery.getCreatedBy(), userId);
    }

    @Test
    public void deliveryShouldBeAssigned() {
        final long userId = 1l;
        final long courierId = 2l;
        final var delivery = Delivery.builder()
                .id(3l).deliveryStatus(DeliveryStatus.CREATED)
                .receiver("Hamza Matraç")
                .build();
        Mockito.when(securityService.getAuthenticatedUserId()).thenReturn(Optional.ofNullable(userId));
        Mockito.when(deliveryRepository.findById(delivery.getId())).thenReturn(Optional.ofNullable(delivery));
        Mockito.when(deliveryRepository.save(Mockito.any(Delivery.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        var assignedDelivery = deliveryService.assignCourier(delivery.getId(),courierId);
        Assertions.assertEquals(assignedDelivery.getDeliveryStatus(), DeliveryStatus.ASSIGNED);
        Assertions.assertEquals(delivery.getAssignedCourier(), courierId);
    }

    @Test
    public void deliveryCourierAllocated() {
        final long userId = 1l;
        final long courierId = 2l;
        final var delivery = Delivery.builder()
                .id(3l).deliveryStatus(DeliveryStatus.ASSIGNED)
                .assignedCourier(courierId)
                .receiver("Hamza Matraç")
                .build();
        Mockito.when(securityService.getAuthenticatedUserId()).thenReturn(Optional.ofNullable(userId));
        Mockito.when(deliveryRepository.findById(delivery.getId())).thenReturn(Optional.ofNullable(delivery));
        Mockito.when(deliveryRepository.save(Mockito.any(Delivery.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        var assignedDelivery = deliveryService.accomplishAssignment(delivery.getId(),courierId,true);
        Assertions.assertEquals(assignedDelivery.getDeliveryStatus(), DeliveryStatus.ASSIGNMENT_COMPLETED);
        Assertions.assertEquals(delivery.getAssignedCourier(), courierId);
    }

    @Test
    public void deliveryTransportationShouldBeStarted() {
        final long userId = 1l;
        final long courierId = 2l;
        final var delivery = Delivery.builder()
                .id(3l).deliveryStatus(DeliveryStatus.ASSIGNMENT_COMPLETED)
                .assignedCourier(courierId)
                .receiver("Hamza Matraç")
                .build();
        Mockito.when(securityService.getAuthenticatedUserId()).thenReturn(Optional.ofNullable(userId));
        Mockito.when(deliveryRepository.findById(delivery.getId())).thenReturn(Optional.ofNullable(delivery));
        Mockito.when(deliveryRepository.save(Mockito.any(Delivery.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        var assignedDelivery = deliveryService.startTransportation(delivery.getId());
        Assertions.assertEquals(assignedDelivery.getDeliveryStatus(), DeliveryStatus.IN_TRANSPORT);
        Assertions.assertEquals(delivery.getAssignedCourier(), courierId);
    }


    @Test
    public void deliveryTransportationShouldBeCompleted() {
        final long userId = 1l;
        final long courierId = 2l;
        final var delivery = Delivery.builder()
                .id(3l).deliveryStatus(DeliveryStatus.IN_TRANSPORT)
                .assignedCourier(courierId)
                .receiver("Hamza Matraç")
                .build();
        Mockito.when(securityService.getAuthenticatedUserId()).thenReturn(Optional.ofNullable(userId));
        Mockito.when(deliveryRepository.findById(delivery.getId())).thenReturn(Optional.ofNullable(delivery));
        Mockito.when(deliveryRepository.save(Mockito.any(Delivery.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        var assignedDelivery = deliveryService.completeTransportation(delivery.getId());
        Assertions.assertEquals(assignedDelivery.getDeliveryStatus(), DeliveryStatus.COMPLETED);
        Assertions.assertEquals(delivery.getAssignedCourier(), courierId);
    }
}
