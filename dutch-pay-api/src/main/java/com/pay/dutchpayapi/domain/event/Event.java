package com.pay.dutchpayapi.domain.event;

import com.pay.dutchpayapi.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    private String title;
    private LocalDate date;

    @Builder
    private Event(String title, LocalDate date) {
        this.title = title;
        this.date = date;
    }
}
