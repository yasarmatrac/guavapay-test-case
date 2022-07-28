package com.guavapay.delivery.repository;


import com.guavapay.delivery.domain.DeliveryTrack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryTrackRepository extends JpaRepository<DeliveryTrack, Long> {
    Page<DeliveryTrack> findAllByDelivery_Id(Long deliveryId, Pageable pageable);
}
