package com.guavapay.courier.repository;

import com.guavapay.courier.domain.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface CourierRepository extends JpaRepository<Courier, Long>, QuerydslPredicateExecutor<Courier> {
    Optional<Courier> findByUserId(Long userId);
}
