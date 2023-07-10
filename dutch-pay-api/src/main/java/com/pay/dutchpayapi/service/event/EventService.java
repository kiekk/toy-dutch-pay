package com.pay.dutchpayapi.service.event;

import com.pay.dutchpayapi.domain.event.Event;
import com.pay.dutchpayapi.dto.event.EventSearchRequest;
import com.pay.dutchpayapi.repository.event.EventRepository;
import com.pay.dutchpayapi.spec.SpecBuilder;
import com.pay.dutchpayapi.spec.event.EventSpec;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> search(EventSearchRequest search) {
        Specification<Event> spec = SpecBuilder.builder(Event.class)
                .ifHasText(search.getStartDate(), EventSpec::beforeEventDate)
                .ifHasText(search.getEndDate(), EventSpec::afterEventDate)
                .toSpec();
        return eventRepository.findAll(spec);
    }

}
