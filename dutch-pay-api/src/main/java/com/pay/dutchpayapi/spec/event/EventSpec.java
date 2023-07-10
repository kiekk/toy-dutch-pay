package com.pay.dutchpayapi.spec.event;

import com.pay.dutchpayapi.domain.event.Event;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EventSpec {

    public static Specification<Event> beforeEventDate(String startDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("date"), LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public static Specification<Event> afterEventDate(String endDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("date"), LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

}
