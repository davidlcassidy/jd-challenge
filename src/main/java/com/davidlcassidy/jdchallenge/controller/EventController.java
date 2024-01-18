package com.davidlcassidy.jdchallenge.controller;

import com.davidlcassidy.jdchallenge.model.Event;
import com.davidlcassidy.jdchallenge.model.EventDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

	@GetMapping()
	public List<Event> getEvents(
			@RequestParam(value = "machineId") String machineId,
			@RequestParam(value = "sessionId") String sessionId) {

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
