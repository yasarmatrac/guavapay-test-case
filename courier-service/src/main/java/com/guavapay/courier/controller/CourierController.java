package com.guavapay.courier.controller;

import com.guavapay.courier.domain.Courier;
import com.guavapay.courier.dto.CourierDTO;
import com.guavapay.courier.dto.request.CourierCreateRequest;
import com.guavapay.courier.dto.request.CourierUpdateRequest;
import com.guavapay.courier.mapper.CourierMapper;
import com.guavapay.courier.service.CourierService;
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
@RequestMapping("/courier")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;
    private final CourierMapper courierMapper;


    @SecurityRequirement(name = "Oauth2",scopes = {"api.courier"})
    @Operation(summary = "Create courier by given information")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Validated CourierCreateRequest request) {
        courierService.create(request);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "Oauth2",scopes = {"api.courier"})
    @Operation(summary = "Update courier by given information")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Validated CourierUpdateRequest request) {
        courierService.update(request);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "Oauth2",scopes = {"api.courier"})
    @Operation(summary = "Delete courier by given information")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courierService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "Oauth2",scopes = {"api.courier"})
    @Operation(summary = "Get courier by query")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/get-one")
    public ResponseEntity<CourierDTO> findOne(@QuerydslPredicate(root = Courier.class) Predicate predicate) {
        return ResponseEntity.of(courierService.findOne(predicate).map(courierMapper::map));
    }

    @SecurityRequirement(name = "Oauth2",scopes = {"api.courier"})
    @Operation(summary = "Get logged courier if logged user role is courier")
    @PreAuthorize("hasAnyRole('COURIER')")
    @GetMapping("/me")
    public ResponseEntity<CourierDTO> getLoggedCourier() {
        return ResponseEntity.of(courierService.getLoggedCourier().map(courierMapper::map));
    }

    @SecurityRequirement(name = "Oauth2",scopes = {"api.courier"})
    @Operation(summary = "Get couriers by query")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public Page<CourierDTO> findAll(@QuerydslPredicate(root = Courier.class) Predicate predicate, Pageable pageable) {
        return courierService.findAll(predicate, pageable).map(courierMapper::map);
    }


}
