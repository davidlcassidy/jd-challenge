package com.davidlcassidy.jdchallenge.service;

import com.davidlcassidy.jdchallenge.model.Event;
import com.davidlcassidy.jdchallenge.model.Session;
import com.davidlcassidy.jdchallenge.repository.EventRepository;
import com.davidlcassidy.jdchallenge.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private EventRepository eventRepository;

	public Event createEvent(String sessionId, String eventAt, String eventType, double numericEventValue) {
		Session session = sessionRepository.findBySessionId(sessionId);

		if (session != null) {
			Event event = new Event();
			event.setEventAt(eventAt);
			event.setEventType(eventType);
			event.setNumericEventValue(numericEventValue);
			event.setSession(session);

			session.getEventList().add(event);
			sessionRepository.save(session);
			return event;
		} else {
			return null;
		}
	}

	public List<Event> getEventsBySessionId(String sessionId) {
		return eventRepository.findBySession_SessionId(sessionId);
	}

}
