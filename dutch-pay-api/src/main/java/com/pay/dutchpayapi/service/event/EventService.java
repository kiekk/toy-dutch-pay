package com.pay.dutchpayapi.service.event;

import com.pay.dutchpayapi.domain.event.Event;
import com.pay.dutchpayapi.repository.event.EventRepository;
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

    public List<Event> search() {
        return eventRepository.findAll();
    }

}
