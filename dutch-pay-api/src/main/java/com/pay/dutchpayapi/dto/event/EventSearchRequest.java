package com.pay.dutchpayapi.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventSearchRequest {
    private String startDate;
    private String endDate;

    public EventSearchRequest(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
