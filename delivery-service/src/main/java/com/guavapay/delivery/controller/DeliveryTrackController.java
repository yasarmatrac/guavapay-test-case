package com.guavapay.delivery.controller;


import com.guavapay.delivery.dto.DeliveryTrackDTO;
import com.guavapay.delivery.mapper.DeliveryTrackMapper;
import com.guavapay.delivery.request.DeliveryTrackCreateRequest;
import com.guavapay.delivery.service.DeliveryTrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/delivery")
@RequiredArgsConstructor
public class DeliveryTrackController {


    private final DeliveryTrackService deliveryTrackService;
    private final DeliveryTrackMapper deliveryTrackMapper;

    @SecurityRequirement(name = "Oauth2", scopes = {"api.delivery"})
    @Operation(summary = "Add delivery location")
    @PreAuthorize("hasAnyRole('COURIER')")
    @PostMapping("/{id}/track")
    public ResponseEntity<Void> save(@PathVariable Long id, @RequestBody @Validated DeliveryTrackCreateRequest request) {
        deliveryTrackService.addTrack(id, request);
        return ResponseEntity.ok().build();
    }

    @SecurityRequirement(name = "Oauth2", scopes = {"api.delivery"})
    @Operation(summary = "Get delivery tracks by delivery id")
    @PreAuthorize("hasAnyRole('COURIER','ADMIN')")
    @GetMapping("/{id}/track")
    public Page<DeliveryTrackDTO> findAll(@PathVariable Long id, Pageable pageable) {
        return deliveryTrackService.findAllByDeliveryId(id, pageable).map(deliveryTrackMapper::map);
    }
}
