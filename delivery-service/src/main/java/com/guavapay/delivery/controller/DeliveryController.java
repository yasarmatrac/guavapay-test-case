package com.guavapay.delivery.controller;

import com.guavapay.delivery.domain.Delivery;
import com.guavapay.delivery.dto.DeliveryDTO;
import com.guavapay.delivery.mapper.DeliveryMapper;
import com.guavapay.delivery.request.DeliveryCreateRequest;
import com.guavapay.delivery.request.DeliveryUpdateRequest;
import com.guavapay.delivery.service.DeliveryService;
import com.querydsl.core.types.Predicate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final DeliveryMapper deliveryMapper;

    @SecurityRequirement(name = "Oauth2", scopes = {"api.delivery"})
    @Operation(summary = "Create parcel delivery order by given information")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Validated DeliveryCreateRequest request) {
        deliveryService.create(request);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "Oauth2", scopes = {"api.delivery"})
    @Operation(summary = "Update parcel delivery order by given information")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Validated DeliveryUpdateRequest request) {
        deliveryService.update(request);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "Oauth2", scopes = {"api.delivery"})
    @Operation(summary = "Assign delivery order to courier")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}/assign/{courierId}")
    public ResponseEntity<Void> assign(@PathVariable Long id, @PathVariable Long courierId) {
        deliveryService.assignCourier(id, courierId);
        return ResponseEntity.ok().build();
    }

    @SecurityRequirement(name = "Oauth2", scopes = {"api.delivery"})
    @Operation(summary = "Cancel delivery order by id")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        deliveryService.cancelDelivery(id);
        return ResponseEntity.ok().build();
    }


    @SecurityRequirement(name = "Oauth2", scopes = {"api.delivery"})
    @Operation(summary = "Start delivery transportation by id")
    @PreAuthorize("hasAnyRole('COURIER')")
    @PutMapping("/{id}/start-transportation")
    public ResponseEntity<Void> startTransportation(@PathVariable Long id) {
        deliveryService.startTransportation(id);
        return ResponseEntity.ok().build();
    }

    @SecurityRequirement(name = "Oauth2", scopes = {"api.delivery"})
    @Operation(summary = "Complete delivery transportation by id")
    @PreAuthorize("hasAnyRole('COURIER')")
    @PutMapping("/{id}/complete-transportation")
    public ResponseEntity<Void> completeTransportation(@PathVariable Long id) {
        deliveryService.completeTransportation(id);
        return ResponseEntity.ok().build();
    }

    @SecurityRequirement(name = "Oauth2", scopes = {"api.delivery"})
    @Operation(summary = "Get delivery by id")
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> findById(@PathVariable Long id) {
        return ResponseEntity.of(deliveryService.findById(id).map(deliveryMapper::map));
    }

    @SecurityRequirement(name = "Oauth2", scopes = {"api.delivery"})
    @Operation(summary = "Get delivery by query")
    @GetMapping
    public Page<DeliveryDTO> findAll(@QuerydslPredicate(root = Delivery.class) Predicate predicate, Pageable pageable) {
        return deliveryService.findAll(predicate, pageable).map(deliveryMapper::map);
    }

}
