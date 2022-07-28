package com.guavapay.courier.stream.event;

import com.guavapay.courier.dto.CourierDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierUserCreateEvent {

    private CourierDTO courier;

    private String username;

    private String password;

}
