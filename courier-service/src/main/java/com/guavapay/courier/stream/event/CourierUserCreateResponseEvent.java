package com.guavapay.courier.stream.event;

import com.guavapay.courier.dto.CourierDTO;
import com.guavapay.courier.dto.UserDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierUserCreateResponseEvent {

    private CourierDTO courier;
    private UserDTO user;
}
