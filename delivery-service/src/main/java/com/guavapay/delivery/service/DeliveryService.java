package com.guavapay.delivery.service;

import com.guavapay.delivery.client.CourierServiceClient;
import com.guavapay.delivery.domain.Address;
import com.guavapay.delivery.domain.Delivery;
import com.guavapay.delivery.domain.DeliveryStatusHistory;
import com.guavapay.delivery.domain.QDelivery;
import com.guavapay.delivery.enums.DeliveryStatus;
import com.guavapay.delivery.enums.DomainUpdateEventType;
import com.guavapay.delivery.repository.DeliveryRepository;
import com.guavapay.delivery.repository.DeliveryStatusHistoryRepository;
import com.guavapay.delivery.request.DeliveryCreateRequest;
import com.guavapay.delivery.request.DeliveryUpdateRequest;
import com.guavapay.delivery.security.CustomOidcUserInfo;
import com.guavapay.delivery.security.SecurityService;
import com.guavapay.delivery.transaction.event.DeliveryCourierAssignmentTransactionEvent;
import com.guavapay.delivery.transaction.event.DeliveryUpdatedTransactionEvent;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryStatusHistoryRepository deliveryStatusHistoryRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SecurityService securityService;
    private final CourierServiceClient courierServiceClient;


    public Delivery create(DeliveryCreateRequest request) {
        Delivery delivery = Delivery.builder()
                .destination(Address.builder()
                        .country(request.getCountry())
                        .city(request.getCity())
                        .district(request.getDistrict())
                        .street(request.getStreet())
                        .detail(request.getDetail()).build())
                .receiver(request.getReceiver())
                .deliveryStatus(DeliveryStatus.CREATED)
                .createdBy(securityService.getAuthenticatedUserId().get())
                .build();
        Delivery saved = saveDeliveryWithStatusHistory(delivery);
        applicationEventPublisher.publishEvent(DeliveryUpdatedTransactionEvent.builder().delivery(delivery).eventType(DomainUpdateEventType.CREATE).build());
        return saved;
    }

    public Delivery update(DeliveryUpdateRequest request) {
        var delivery = deliveryRepository.findById(request.getId()).orElseThrow();
        if (!isEligibleForChange(delivery)) {
            throw new RuntimeException("Delivery not eligible for change");
        }
        delivery.setReceiver(request.getReceiver());
        delivery.setDestination(Address.builder().country(request.getCountry()).city(request.getCity()).district(request.getDistrict()).street(request.getStreet()).detail(request.getDetail()).build());
        var saved = deliveryRepository.save(delivery);
        applicationEventPublisher.publishEvent(DeliveryUpdatedTransactionEvent.builder().delivery(delivery).eventType(DomainUpdateEventType.CREATE).build());
        return saved;
    }

    private boolean isEligibleForChange(Delivery delivery) {
        return List.of(DeliveryStatus.CREATED, DeliveryStatus.ASSIGNED, DeliveryStatus.ASSIGNMENT_FAILURE, DeliveryStatus.ASSIGNMENT_COMPLETED).contains(delivery.getDeliveryStatus());
    }


    private Delivery saveDeliveryWithStatusHistory(Delivery delivery) {
        var saved = deliveryRepository.save(delivery);
        deliveryStatusHistoryRepository.save(DeliveryStatusHistory.builder().delivery(saved).status(saved.getDeliveryStatus()).time(Instant.now()).actionBy(securityService.getAuthenticatedUserId().orElse(null)).build());
        return saved;
    }


    public Delivery assignCourier(Long deliveryId, Long courierId) {
        var delivery = deliveryRepository.findById(deliveryId).orElseThrow();
        if (!isEligibleForAssign(delivery)) {
            throw new RuntimeException("Delivery not eligible for change");
        }
        delivery.setDeliveryStatus(DeliveryStatus.ASSIGNED);
        delivery.setAssignedCourier(courierId);
        Delivery saved = saveDeliveryWithStatusHistory(delivery);
        applicationEventPublisher.publishEvent(DeliveryCourierAssignmentTransactionEvent.builder().delivery(saved).build());
        return saved;
    }

    private boolean isEligibleForAssign(Delivery delivery) {
        return List.of(DeliveryStatus.CREATED, DeliveryStatus.ASSIGNMENT_FAILURE).contains(delivery.getDeliveryStatus());
    }


    public Delivery accomplishAssignment(Long id, Long courierId, boolean isSuccess) {
        var delivery = deliveryRepository.findById(id).orElseThrow();
        if (delivery.getDeliveryStatus() != DeliveryStatus.ASSIGNED || !Objects.equals(delivery.getAssignedCourier(), courierId)) {
            throw new RuntimeException();
        }
        delivery.setDeliveryStatus(isSuccess ? DeliveryStatus.ASSIGNMENT_COMPLETED : DeliveryStatus.ASSIGNMENT_FAILURE);
        saveDeliveryWithStatusHistory(delivery);
        return deliveryRepository.save(delivery);
    }

    public Delivery cancelDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id).orElseThrow();
        if (!isEligibleForCancel(delivery)) {
            throw new RuntimeException("Delivery not eligible for change");
        }
        delivery.setDeliveryStatus(DeliveryStatus.CANCELED);
        Delivery saved = saveDeliveryWithStatusHistory(delivery);
        applicationEventPublisher.publishEvent(DeliveryUpdatedTransactionEvent.builder().delivery(saved).eventType(DomainUpdateEventType.UPDATE).build());
        return saved;
    }

    private boolean isEligibleForCancel(Delivery delivery) {
        return List.of(DeliveryStatus.CREATED, DeliveryStatus.ASSIGNMENT_COMPLETED, DeliveryStatus.ASSIGNMENT_FAILURE).contains(delivery.getDeliveryStatus());
    }

    public Delivery startTransportation(Long id) {
        Delivery delivery = deliveryRepository.findById(id).orElseThrow();
        if (!isEligibleForTransportation(delivery)) {
            throw new RuntimeException("Delivery not eligible for change");
        }
        delivery.setDeliveryStatus(DeliveryStatus.IN_TRANSPORT);
        Delivery saved = saveDeliveryWithStatusHistory(delivery);
        applicationEventPublisher.publishEvent(DeliveryUpdatedTransactionEvent.builder().delivery(saved).eventType(DomainUpdateEventType.UPDATE).build());
        return saved;
    }

    private boolean isEligibleForTransportation(Delivery delivery) {
        return List.of(DeliveryStatus.ASSIGNMENT_COMPLETED).contains(delivery.getDeliveryStatus());
    }


    public Delivery completeTransportation(Long id) {
        Delivery delivery = deliveryRepository.findById(id).orElseThrow();
        if (!isEligibleForCompletion(delivery)) {
            throw new RuntimeException("Delivery not eligible for change");
        }
        delivery.setDeliveryStatus(DeliveryStatus.COMPLETED);
        Delivery saved = saveDeliveryWithStatusHistory(delivery);
        applicationEventPublisher.publishEvent(DeliveryUpdatedTransactionEvent.builder().delivery(saved).eventType(DomainUpdateEventType.UPDATE).build());
        return saved;
    }

    private boolean isEligibleForCompletion(Delivery delivery) {
        return List.of(DeliveryStatus.IN_TRANSPORT).contains(delivery.getDeliveryStatus());
    }


    public Optional<Delivery> findById(Long id) {
        var deliveryOptional = deliveryRepository.findById(id);
        if (deliveryOptional.isPresent()) {
            var user = securityService.getUserInfo();
            if (!user.getRoles().contains(CustomOidcUserInfo.UserRole.ADMIN)) {
                if (user.getRoles().contains(CustomOidcUserInfo.UserRole.USER)) {
                    if (!Objects.equals(user.getUserId(), deliveryOptional.get().getCreatedBy())) {
                        throw new AccessDeniedException("Access Denied");
                    }
                } else if (user.getRoles().contains(CustomOidcUserInfo.UserRole.COURIER)) {
                    var courier = courierServiceClient.getLoggedCourier().orElseThrow();
                    if (!Objects.equals(courier.getId(), deliveryOptional.get().getAssignedCourier())) {
                        throw new AccessDeniedException("Access Denied");
                    }
                }
            }
        }
        return deliveryOptional;
    }

    public Page<Delivery> findAll(Predicate predicate, Pageable pageable) {
        var user = securityService.getUserInfo();
        QDelivery delivery = QDelivery.delivery;

        if (!user.getRoles().contains(CustomOidcUserInfo.UserRole.ADMIN)) {
            if (user.getRoles().contains(CustomOidcUserInfo.UserRole.USER)) {
                predicate = delivery.createdBy.eq(user.getUserId()).and(predicate);
            } else if (user.getRoles().contains(CustomOidcUserInfo.UserRole.COURIER)) {
                var courier = courierServiceClient.getLoggedCourier().orElseThrow();
                predicate = delivery.assignedCourier.eq(courier.getId()).and(predicate);
            }
        }

        return deliveryRepository.findAll(predicate, pageable);
    }


}
