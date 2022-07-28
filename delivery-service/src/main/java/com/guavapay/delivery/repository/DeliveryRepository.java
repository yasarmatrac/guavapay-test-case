package com.guavapay.delivery.repository;

import com.guavapay.delivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DeliveryRepository extends JpaRepository<Delivery, Long>, QuerydslPredicateExecutor<Delivery> {
}
