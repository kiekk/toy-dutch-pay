package com.pay.dutchpayapi.dto.event;

import com.pay.dutchpayapi.domain.event.Event;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class EventCreateRequest {
    private String title;
    private LocalDate date;

    @Builder
    private EventCreateRequest(String title, LocalDate date) {
        this.title = title;
        this.date = date;
    }

    public Event toEntity() {
        return Event.builder()
                .title(title)
                .date(date)
                .build();
    }
}
