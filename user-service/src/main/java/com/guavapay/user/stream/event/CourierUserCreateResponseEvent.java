package com.guavapay.user.stream.event;

import com.guavapay.user.dto.CourierDTO;
import com.guavapay.user.dto.UserDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierUserCreateResponseEvent {

    private CourierDTO courier;
    private UserDTO user;
}
