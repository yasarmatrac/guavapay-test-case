package com.guavapay.courier.dto.request;

import com.guavapay.courier.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserCreateRequest {

    private Long id;

    private String username;

    private UserRole userRole;

    private String name;

    private String surname;

    private String email;

    private String phoneNumber;

    private String password;

    private LocalDateTime lastLoggedTime;

}
