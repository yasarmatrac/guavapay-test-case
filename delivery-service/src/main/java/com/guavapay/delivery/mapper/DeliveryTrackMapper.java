package com.guavapay.delivery.mapper;

import com.guavapay.delivery.domain.Delivery;
import com.guavapay.delivery.domain.DeliveryTrack;
import com.guavapay.delivery.dto.DeliveryDTO;
import com.guavapay.delivery.dto.DeliveryTrackDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {LocationMapper.class})
public interface DeliveryTrackMapper {

    DeliveryTrackDTO map(DeliveryTrack deliveryTrack);
}
