package com.guavapay.user.dto;

import com.guavapay.user.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;

    private String username;

    private UserRole userRole;

    private String name;

    private String surname;

    private String email;

    private String password;

    private LocalDateTime lastLoggedTime;

    private int failureLoginCount;

    private boolean expired;

    private boolean locked;

    private boolean enabled;
}
