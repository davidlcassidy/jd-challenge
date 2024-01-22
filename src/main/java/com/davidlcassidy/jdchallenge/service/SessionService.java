package com.davidlcassidy.jdchallenge.service;

import com.davidlcassidy.jdchallenge.model.Event;
import com.davidlcassidy.jdchallenge.model.Session;
import com.davidlcassidy.jdchallenge.model.SessionAggregatedEvents;
import com.davidlcassidy.jdchallenge.repository.EventRepository;
import com.davidlcassidy.jdchallenge.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private EventRepository eventRepository;

    public Session createSession(String sessionId, String machineId, String startAt) {
        Session session = Session.builder().sessionId(sessionId).machineId(machineId).startAt(startAt).build();
        sessionRepository.save(session);
        return session;
    }

    public Optional<SessionAggregatedEvents> getSessionAggregatedEvents(String sessionId) {
        List<Event> sessionEventList = eventRepository.findBySession_SessionId(sessionId);

        if (sessionEventList.isEmpty()) {
            return Optional.empty();
        }

        Map<String, Double> aggregatedValues = sessionEventList.stream()
                .collect(Collectors.groupingBy(Event::getEventType,
                        Collectors.summingDouble(Event::getNumericEventValue)));

        List<SessionAggregatedEvents.EventAggregation> eventAggregations = aggregatedValues.entrySet().stream()
                .map(entry -> {
                    SessionAggregatedEvents.EventAggregation eventAggregation =
                            new SessionAggregatedEvents.EventAggregation();
                    eventAggregation.setEventType(entry.getKey());
                    eventAggregation.setTotalEventValue(entry.getValue());
                    return eventAggregation;
                })
                .collect(Collectors.toList());

        return Optional.of(SessionAggregatedEvents.builder().sessionId(sessionId).events(eventAggregations).build());
    }
}