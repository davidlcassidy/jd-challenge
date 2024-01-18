package com.davidlcassidy.jdchallenge.service;

import com.davidlcassidy.jdchallenge.model.Event;
import com.davidlcassidy.jdchallenge.model.EventDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

	public List<Event> getByMachineIdAndSessionId(String machineId, String sessionId) {
		return getTestEvent(machineId, sessionId);
	}

	private List<Event> getTestEvent(String machineId, String sessionId){
		EventDetails eventDetails = new EventDetails();
		eventDetails.setEventAt("2022-01-18T12:00:00");
		eventDetails.setEventType(machineId + " - SampleEvent");
		eventDetails.setNumericEventValue(42.0);

		Event event = new Event();
		event.setSessionId(sessionId);
		event.setEvents(List.of(eventDetails));

		return List.of(event);
	}
}
