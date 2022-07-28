package com.guavapay.courier.mapper;

import com.guavapay.courier.dto.CourierDTO;
import com.guavapay.courier.domain.Courier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourierMapper {

    CourierDTO map(Courier courier);
}
