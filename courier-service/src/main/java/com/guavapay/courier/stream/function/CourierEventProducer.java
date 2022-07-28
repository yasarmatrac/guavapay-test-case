package com.guavapay.courier.stream.function;

import com.guavapay.courier.domain.Courier;
import com.guavapay.courier.mapper.CourierMapper;
import com.guavapay.courier.stream.event.CourierUserCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourierEventProducer {

    private final StreamBridge bridge;
    private final CourierMapper courierMapper;

    public boolean sendCourierUserCreateEvent(Courier courier, String username, String password) {
        var data = CourierUserCreateEvent.builder()
                .courier(courierMapper.map(courier))
                .username(username)
                .password(password).build();
        return bridge.send("courierUserCreateEventFunction-out-0", data);
    }
}
