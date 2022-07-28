package com.guavapay.delivery.domain;

import com.guavapay.delivery.enums.DeliveryStatus;
import com.querydsl.core.annotations.QueryEntity;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Data
@EqualsAndHashCode(of = {"id"})
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery_status_history")
@QueryEntity
public class DeliveryStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private DeliveryStatus status;

    @Column(name = "action_by")
    private Long actionBy;

    @Column(name = "time", nullable = false)
    private Instant time;
}
