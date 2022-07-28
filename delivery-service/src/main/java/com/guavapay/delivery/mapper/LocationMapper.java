package com.guavapay.delivery.mapper;

import com.guavapay.delivery.domain.Location;
import com.guavapay.delivery.dto.LocationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationDTO map(Location location);
}
