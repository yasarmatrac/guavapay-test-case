package com.guavapay.delivery.mapper;

import com.guavapay.delivery.domain.Delivery;
import com.guavapay.delivery.dto.DeliveryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, LocationMapper.class})
public interface DeliveryMapper {

    DeliveryDTO map(Delivery delivery);
}
