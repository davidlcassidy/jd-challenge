package com.davidlcassidy.jdchallenge.service;

import com.davidlcassidy.jdchallenge.model.Event;
import com.davidlcassidy.jdchallenge.model.Session;
import com.davidlcassidy.jdchallenge.model.SessionAggregatedEvents;
import com.davidlcassidy.jdchallenge.repository.EventRepository;
import com.davidlcassidy.jdchallenge.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Session createSession(String sessionId, String machineId, LocalDateTime startAt) {
        Session session = Session.builder().sessionId(sessionId).machineId(machineId).startAt(startAt).build();
        sessionRepository.save(session);
        return session;
    }

    public Event createSessionEvent(String sessionId, String eventAt, String eventType, double numericEventValue) {
        Session session = sessionRepository.findBySessionId(sessionId);

        if (session != null) {
            Event event = Event.builder()
                    .eventAt(eventAt)
                    .eventType(eventType)
                    .numericEventValue(numericEventValue)
                    .session(session)
                    .build();

            session.getEventList().add(event);
            sessionRepository.save(session);
            return event;
        } else {
            return null;
        }
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

    public List<String> getMachineIds() {
        List<Session> allSessions = sessionRepository.findAll();
        return allSessions.stream()
                .map(Session::getMachineId)
                .distinct()
                .collect(Collectors.toList());
    }

    public Optional<Session> getMostRecentSessionByMachineId(String machineId) {
        return sessionRepository.findTopByMachineIdOrderByStartAtDesc(machineId);
    }
}