package com.guavapay.user.mapper;

import com.guavapay.user.domain.User;
import com.guavapay.user.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO map(User user);
}
