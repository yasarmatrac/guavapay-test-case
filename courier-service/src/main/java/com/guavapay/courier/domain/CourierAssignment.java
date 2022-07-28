package com.guavapay.courier.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierAssignment {

    private boolean success;
    private Courier courier;
}
