package com.guavapay.delivery.repository;

import com.guavapay.delivery.domain.DeliveryStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryStatusHistoryRepository extends JpaRepository<DeliveryStatusHistory, Long> {
}
