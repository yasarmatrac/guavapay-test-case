package com.guavapay.user.stream.function;

import com.guavapay.user.enums.UserRole;
import com.guavapay.user.mapper.UserMapper;
import com.guavapay.user.request.UserCreateRequest;
import com.guavapay.user.service.UserService;
import com.guavapay.user.stream.event.CourierUserCreateEvent;
import com.guavapay.user.stream.event.CourierUserCreateResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;


@Component
@RequiredArgsConstructor
public class UserEventConsumer {

    private final UserService userService;
    private final UserMapper userMapper;

    @Bean
    public Function<CourierUserCreateEvent, CourierUserCreateResponseEvent> courierUserCreateEventFunction() {
        return input -> {
            UserCreateRequest request = UserCreateRequest.builder()
                    .name(input.getCourier().getName())
                    .surname(input.getCourier().getSurname())
                    .phoneNumber(input.getCourier().getPhoneNumber())
                    .username(input.getUsername())
                    .password(input.getPassword())
                    .userRole(UserRole.COURIER)
                    .build();
            var user = userService.save(request);

            return CourierUserCreateResponseEvent
                    .builder()
                    .courier(input.getCourier())
                    .user(userMapper.map(user))
                    .build();
        };
    }

}
