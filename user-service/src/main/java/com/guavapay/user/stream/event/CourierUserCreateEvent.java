package com.guavapay.user.stream.event;

import com.guavapay.user.dto.CourierDTO;
import lombok.Data;

@Data
public class CourierUserCreateEvent {

    private CourierDTO courier;

    private String username;

    private String password;

}
