package com.pay.dutchpayapi.service.event;

import com.pay.dutchpayapi.domain.event.Event;
import com.pay.dutchpayapi.dto.event.EventCreateRequest;
import com.pay.dutchpayapi.dto.event.EventSearchRequest;
import com.pay.dutchpayapi.repository.event.EventRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @AfterEach
    void tearDown() {
        eventRepository.deleteAllInBatch();
    }

    @DisplayName("주어진 기간에 해당하는 일정 조회 시나리오")
    @TestFactory
    Collection<DynamicTest> searchBySpec() {
        // given
        LocalDate now = LocalDate.now();
        Event event1 = createEvent("일정1", now.minus(1, ChronoUnit.DAYS));
        Event event2 = createEvent("일정2", now);
        Event event3 = createEvent("일정3", now.plus(1, ChronoUnit.DAYS));
        eventRepository.saveAll(List.of(event1, event2, event3));


        return List.of(
                DynamicTest.dynamicTest("현재 날짜에 저장된 일정은 1개다.", () -> {
                    // when
                    String startDate = now.toString();
                    String endDate = now.toString();
                    EventSearchRequest searchRequest = new EventSearchRequest(startDate, endDate);
                    List<Event> events = eventService.search(searchRequest);

                    // then
                    assertThat(events)
                            .hasSize(1)
                            .extracting("title", "date")
                            .containsExactlyInAnyOrder(
                                    tuple("일정2", now)
                            );
                }),
                DynamicTest.dynamicTest("현재 날짜-1 부터 현재 날짜까지 저장된 일정은 2개다.", () -> {
                    // when
                    String startDate = now.minus(1, ChronoUnit.DAYS).toString();
                    String endDate = now.toString();
                    EventSearchRequest searchRequest = new EventSearchRequest(startDate, endDate);
                    List<Event> events = eventService.search(searchRequest);

                    // then
                    assertThat(events)
                            .hasSize(2)
                            .extracting("title", "date")
                            .containsExactlyInAnyOrder(
                                    tuple("일정1", now.minus(1, ChronoUnit.DAYS)),
                                    tuple("일정2", now)
                            );
                }),
                DynamicTest.dynamicTest("현재 날짜부터 현재 날짜+1까지 저장된 일정은 1개다.", () -> {
                    // when
                    String startDate = now.toString();
                    String endDate = now.plus(1, ChronoUnit.DAYS).toString();
                    EventSearchRequest searchRequest = new EventSearchRequest(startDate, endDate);
                    List<Event> events = eventService.search(searchRequest);

                    // then
                    assertThat(events)
                            .hasSize(2)
                            .extracting("title", "date")
                            .containsExactlyInAnyOrder(
                                    tuple("일정2", now),
                                    tuple("일정3", now.plus(1, ChronoUnit.DAYS))
                            );
                }),
                DynamicTest.dynamicTest("주어진 기간에 저장된 일정이 없을 경우 Empty List를 반환한다.", () -> {
                    // when
                    String startDate = now.plus(2, ChronoUnit.DAYS).toString();
                    String endDate = now.plus(3, ChronoUnit.DAYS).toString();
                    EventSearchRequest searchRequest = new EventSearchRequest(startDate, endDate);
                    List<Event> events = eventService.search(searchRequest);

                    // then
                    assertThat(events).isEmpty();
                })
        );
    }

    @DisplayName("일정을 등록한다.")
    @Test
    void createEvent() {
        // given
        String title = "일정1";
        LocalDate now = LocalDate.now();
        EventCreateRequest request = EventCreateRequest.builder()
                .title(title)
                .date(now)
                .build();

        // when
        Event event = eventService.create(request);

        // then
        assertThat(event)
                .extracting("title", "date")
                .contains(title, now);
    }

    private Event createEvent(String title, LocalDate date) {
        return Event.builder()
                .title(title)
                .date(date)
                .build();
    }

}