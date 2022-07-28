package com.guavapay.delivery.service;

import com.guavapay.delivery.client.CourierServiceClient;
import com.guavapay.delivery.domain.DeliveryTrack;
import com.guavapay.delivery.domain.Location;
import com.guavapay.delivery.enums.DeliveryStatus;
import com.guavapay.delivery.repository.DeliveryRepository;
import com.guavapay.delivery.repository.DeliveryTrackRepository;
import com.guavapay.delivery.request.DeliveryTrackCreateRequest;
import com.guavapay.delivery.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryTrackService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryTrackRepository deliveryTrackRepository;

    private final CourierServiceClient courierServiceClient;

    public DeliveryTrack addTrack(Long deliveryId, DeliveryTrackCreateRequest request){
        var courier = courierServiceClient.getLoggedCourier().orElseThrow();

        var delivery = deliveryRepository.findById(deliveryId).orElseThrow();

        if (!Objects.equals(delivery.getAssignedCourier(),courier.getId())){
            throw new AccessDeniedException("Illegal access");
        }
        if (delivery.getDeliveryStatus() != DeliveryStatus.IN_TRANSPORT){
            throw new RuntimeException("Status isn't eligible");
        }
        Location location = Location.builder()
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();
        delivery.setCurrentLocation(location);
        deliveryRepository.save(delivery);
        DeliveryTrack deliveryTrack = DeliveryTrack.builder()
                .location(location)
                .delivery(delivery)
                .time(Instant.now())
                .build();
        return deliveryTrackRepository.save(deliveryTrack);
    }

    public Page<DeliveryTrack> findAllByDeliveryId(Long deliveryId, Pageable pageable){
        return deliveryTrackRepository.findAllByDelivery_Id(deliveryId,pageable);
    }
}
