package com.guavapay.courier.domain;

import com.guavapay.courier.enums.CourierStatus;
import com.guavapay.courier.enums.VehicleType;
import com.querydsl.core.annotations.QueryEntity;
import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(of = {"id"})
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courier")
@QueryEntity
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "courier_status", nullable = false)
    private CourierStatus courierStatus;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "current_delivery_id")
    private Long currentDeliveryId;

    @Column(name = "user_id")
    private Long userId;

}
