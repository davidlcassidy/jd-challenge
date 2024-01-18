package com.davidlcassidy.jdchallenge.controller;

import com.davidlcassidy.jdchallenge.model.Event;
import com.davidlcassidy.jdchallenge.model.EventDetails;
import com.davidlcassidy.jdchallenge.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

	@Autowired
	private EventService eventService;

	@GetMapping()
	public ResponseEntity<List<Event>> getEvents(
			@RequestParam(value = "machineId") String machineId,
			@RequestParam(value = "sessionId") String sessionId) {

		List<Event> events = eventService.getByMachineIdAndSessionId(machineId, sessionId);
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

}
