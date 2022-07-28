package com.guavapay.courier.service;

import com.guavapay.courier.domain.Courier;
import com.guavapay.courier.domain.CourierAssignment;
import com.guavapay.courier.dto.DeliveryDTO;
import com.guavapay.courier.dto.request.CourierCreateRequest;
import com.guavapay.courier.dto.request.CourierUpdateRequest;
import com.guavapay.courier.enums.CourierStatus;
import com.guavapay.courier.enums.DeliveryStatus;
import com.guavapay.courier.enums.DomainUpdateEventType;
import com.guavapay.courier.repository.CourierRepository;
import com.guavapay.courier.security.CustomOidcUserInfo;
import com.guavapay.courier.security.SecurityService;
import com.guavapay.courier.transaction.event.CourierUpdatedTransactionEvent;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CourierService {

    private final CourierRepository courierRepository;
    private final SecurityService securityService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Courier create(CourierCreateRequest request) {
        var courier = Courier.builder().courierStatus(CourierStatus.AVAILABLE).vehicleType(request.getVehicleType()).name(request.getName()).surname(request.getSurname()).phoneNumber(request.getPhoneNumber()).build();

        var saved = courierRepository.save(courier);
        applicationEventPublisher.publishEvent(CourierUpdatedTransactionEvent.builder().courier(saved).username(request.getUsername()).password(request.getPassword()).domainUpdateEventType(DomainUpdateEventType.CREATE).courier(courier).build());
        return saved;
    }

    public Courier update(CourierUpdateRequest request) {
        var courier = courierRepository.findById(request.getId()).orElseThrow();
        if (courier.getCourierStatus() == CourierStatus.BUSY) {
            throw new RuntimeException("You cant change courier information when status is busy");
        }
        courier.setVehicleType(request.getVehicleType());
        courier.setName(request.getName());
        courier.setSurname(request.getSurname());
        courier.setPhoneNumber(request.getPhoneNumber());
        var saved = courierRepository.save(courier);
        applicationEventPublisher.publishEvent(CourierUpdatedTransactionEvent.builder().courier(saved).username(request.getUsername()).password(request.getPassword()).domainUpdateEventType(DomainUpdateEventType.UPDATE).courier(courier).build());
        return saved;
    }

    public CourierAssignment assignCourier(DeliveryDTO delivery) {
        var courier = courierRepository.findById(delivery.getAssignedCourier()).orElseThrow();
        boolean assigned = false;
        if (courier.getCourierStatus() == CourierStatus.AVAILABLE) {
            courier.setCourierStatus(CourierStatus.BUSY);
            courier.setCurrentDeliveryId(delivery.getId());
            courier = courierRepository.save(courier);
            assigned = true;
        }
        return CourierAssignment.builder().courier(courier).success(assigned).build();

    }

    public void setCourierUserInfos(Long courierId, Long userId) {
        var courierOptional = courierRepository.findById(courierId);
        if (courierOptional.isPresent()) {
            var courier = courierOptional.get();
            courier.setUserId(userId);
            courierRepository.save(courier);
        }
    }

    public void handleDeliveryEvent(DeliveryDTO delivery) {
        if (delivery.getAssignedCourier() == null) {
            return;
        }
        var courierOptional = courierRepository.findById(delivery.getAssignedCourier());
        if (courierOptional.isEmpty()) {
            return;
        }
        var courier = courierOptional.get();
        if ((delivery.getDeliveryStatus() == DeliveryStatus.CANCELED || delivery.getDeliveryStatus() == DeliveryStatus.COMPLETED) && Objects.equals(courier.getCurrentDeliveryId(), delivery.getId()) && courier.getCourierStatus() == CourierStatus.BUSY) {
            courier.setCourierStatus(CourierStatus.AVAILABLE);
            courier.setCurrentDeliveryId(null);
        }
    }

    public Page<Courier> findAll(Predicate predicate, Pageable pageable) {
        return courierRepository.findAll(predicate, pageable);
    }

    public Optional<Courier> findOne(Predicate predicate) {
        var courier = courierRepository.findOne(predicate);
        return courier;
    }

    public Optional<Courier> findById(Long id) {
        return courierRepository.findById(id);
    }

    public Optional<Courier> getLoggedCourier() {
        var userInfo = securityService.getUserInfo();
        if (!userInfo.getRoles().contains(CustomOidcUserInfo.UserRole.COURIER)) {
            return Optional.empty();
        }
        return courierRepository.findByUserId(userInfo.getUserId());
    }

    public void delete(Long id) {
        var courier = courierRepository.findById(id).orElseThrow();
        if (courier.getCourierStatus() != CourierStatus.AVAILABLE) {
            throw new RuntimeException("Courier status is not eligible");
        }
        courierRepository.deleteById(id);
    }
}
