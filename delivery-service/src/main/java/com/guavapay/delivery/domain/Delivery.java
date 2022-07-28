package com.guavapay.delivery.domain;

import com.guavapay.delivery.enums.DeliveryStatus;
import com.querydsl.core.annotations.QueryEntity;
import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(of = {"id"})
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery")
@QueryEntity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus deliveryStatus;

    @Embedded
    private Address destination;

    @Column(name = "receiver", nullable = false)
    private String receiver;

    @Column(name = "assigned_courier")
    private Long assignedCourier;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Embedded
    @AttributeOverride(name = "latitude", column = @Column(name = "latitude",nullable = true))
    @AttributeOverride(name = "longitude", column = @Column(name = "longitude",nullable = true))
    private Location currentLocation;

}
