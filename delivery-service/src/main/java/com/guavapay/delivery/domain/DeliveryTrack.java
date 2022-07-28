package com.guavapay.delivery.domain;

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
@Table(name = "delivery_track")
@QueryEntity
public class DeliveryTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Embedded
    @AttributeOverride(name = "latitude", column = @Column(name = "latitude",nullable = false))
    @AttributeOverride(name = "longitude", column = @Column(name = "longitude",nullable = false))
    private Location location;

    @Column(name = "time", nullable = false)
    private Instant time;
}
