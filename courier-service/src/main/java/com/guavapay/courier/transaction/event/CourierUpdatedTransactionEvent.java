package com.guavapay.courier.transaction.event;

import com.guavapay.courier.domain.Courier;
import com.guavapay.courier.enums.DomainUpdateEventType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierUpdatedTransactionEvent {

    private Courier courier;
    private String username;
    private String password;
    private DomainUpdateEventType domainUpdateEventType;


}
