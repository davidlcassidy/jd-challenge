package com.davidlcassidy.jdchallenge.controller;

import com.davidlcassidy.jdchallenge.model.Session;
import com.davidlcassidy.jdchallenge.model.SessionAggregatedEvents;
import com.davidlcassidy.jdchallenge.service.SessionService;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
public class SessionController {

    @Autowired
    private SessionService sessionService;

    Random random = new Random();

    @GetMapping("/events")
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

    @GetMapping("/machines")
    public ResponseEntity<List<String>> getMachineIds() {

        List<String> machineIdList = sessionService.getMachineIds();

        if (machineIdList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(machineIdList, HttpStatus.OK);
    }

    @GetMapping("machines/{machineId}/mostRecentSession")
    public ResponseEntity<Session> getLatestSession(@PathVariable String machineId) {
        Optional<Session> latestSession = sessionService.getMostRecentSessionByMachineId(machineId);

        if (latestSession.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(latestSession.get(), HttpStatus.OK);
    }

    private void createSampleSessionAndEvent() {
        String sessionId = String.valueOf(random.nextInt(100000));
        sessionService.createSession(sessionId, String.valueOf(random.nextInt(100000)), LocalDateTime.now());
        sessionService.createSessionEvent(sessionId, "eventAtValue", "eventTypeValue", random.nextInt(100));
    }

}
