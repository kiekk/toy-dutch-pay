package com.pay.dutchpayapi.repository.event;

import com.pay.dutchpayapi.domain.event.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @DisplayName("일정들을 모두 조회한다.")
    @Test
    void findAll() {
        // given
        LocalDate now = LocalDate.now();
        Event event1 = createEvent("일정1", now);
        Event event2 = createEvent("일정2", now);
        Event event3 = createEvent("일정3", now);
        eventRepository.saveAll(List.of(event1, event2, event3));

        // when
        List<Event> events = eventRepository.findAll();

        // then
        assertThat(events)
                .hasSize(3)
                .extracting("title", "date")
                .containsExactlyInAnyOrder(
                        tuple("일정1", now),
                        tuple("일정2", now),
                        tuple("일정3", now)
                );
    }

    private Event createEvent(String title, LocalDate date) {
        return Event.builder()
                .title(title)
                .date(date)
                .build();
    }

}