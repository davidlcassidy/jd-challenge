package com.davidlcassidy.jdchallenge.controller;

import com.davidlcassidy.jdchallenge.model.Event;
import com.davidlcassidy.jdchallenge.service.EventService;
import com.davidlcassidy.jdchallenge.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/events")
public class EventController {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private EventService eventService;

	Random random = new Random();

	@GetMapping()
	public ResponseEntity<List<Event>> getEvents(
			@RequestParam(value = "machineId") String machineId,
			@RequestParam(value = "sessionId") String sessionId) {

		createSampleSessionAndEvent();

		List<Event> events = eventService.getEventsBySessionId(sessionId);
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	private void createSampleSessionAndEvent(){
		String sessionId = String.valueOf(random.nextInt(100000));
		sessionService.createSession(sessionId, String.valueOf(random.nextInt(100000)), "startAt");
		eventService.createEvent(sessionId, "eventAtValue", "eventTypeValue", random.nextInt(100));
	}

}
