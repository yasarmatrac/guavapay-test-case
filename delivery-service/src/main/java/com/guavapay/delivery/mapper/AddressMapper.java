package com.guavapay.delivery.mapper;

import com.guavapay.delivery.dto.AddressDTO;
import com.guavapay.delivery.domain.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDTO map(Address address);

    Address map(AddressDTO address);
}
