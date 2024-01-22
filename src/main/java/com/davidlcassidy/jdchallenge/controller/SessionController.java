package com.davidlcassidy.jdchallenge.controller;

import com.davidlcassidy.jdchallenge.model.SessionAggregatedEvents;
import com.davidlcassidy.jdchallenge.service.EventService;
import com.davidlcassidy.jdchallenge.service.SessionService;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/events")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private EventService eventService;

    Random random = new Random();

    @GetMapping()
    public ResponseEntity<SessionAggregatedEvents> getSessionAggregatedEvents(
            @RequestParam(value = "machineId") String machineId,
            @RequestParam(value = "sessionId") String sessionId) {

        createSampleSessionAndEvent();

        if (StringUtils.isNullOrEmpty(machineId) || StringUtils.isNullOrEmpty(sessionId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<SessionAggregatedEvents> sessionAggregatedEvents = sessionService.getSessionAggregatedEvents(sessionId);

        if (sessionAggregatedEvents.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(sessionAggregatedEvents.get(), HttpStatus.OK);
    }

    private void createSampleSessionAndEvent() {
        String sessionId = String.valueOf(random.nextInt(100000));
        sessionService.createSession(sessionId, String.valueOf(random.nextInt(100000)), "startAt");
        eventService.createEvent(sessionId, "eventAtValue", "eventTypeValue", random.nextInt(100));
    }

}
