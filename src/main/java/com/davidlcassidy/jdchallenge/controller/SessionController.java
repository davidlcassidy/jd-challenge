package com.davidlcassidy.jdchallenge.controller;

import com.davidlcassidy.jdchallenge.model.Event;
import com.davidlcassidy.jdchallenge.model.Session;
import com.davidlcassidy.jdchallenge.model.SessionAggregatedEvents;
import com.davidlcassidy.jdchallenge.service.SessionService;
import com.mysql.cj.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@Tag(name = "Session Controller", description = "Endpoints for retrieving session data")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    Random random = new Random();

    @GetMapping("/events")
    @Operation(summary = "Get session aggregated events", description = "Retrieve aggregated events for a session")
    @Parameter(name = "machineId", required = true)
    @Parameter(name = "sessionId", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved session aggregated events",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SessionAggregatedEvents.class))),
            @ApiResponse(responseCode = "204", description = "No content available for the requested session",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Bad request, machineId or sessionId is missing or empty",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<SessionAggregatedEvents> getSessionAggregatedEvents(
            @RequestParam(value = "machineId") String machineId,
            @RequestParam(value = "sessionId") String sessionId) {

        createSampleSessionAndEvent();

        if (StringUtils.isNullOrEmpty(machineId) || StringUtils.isNullOrEmpty(sessionId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<SessionAggregatedEvents> sessionAggregatedEvents = sessionService.getSessionAggregatedEvents(sessionId, machineId);

        if (sessionAggregatedEvents.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(sessionAggregatedEvents.get(), HttpStatus.OK);
    }

    @GetMapping("/machines")
    @Operation(summary = "Get machine IDs", description = "Retrieve a list of all machine IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved machine IDs",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = String.class)))),
            @ApiResponse(responseCode = "204", description = "No machine IDs available",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<List<String>> getMachineIds() {

        List<String> machineIdList = sessionService.getMachineIds();

        if (machineIdList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(machineIdList, HttpStatus.OK);
    }

    @GetMapping("machines/{machineId}/mostRecentSession")
    @Operation(summary = "Get the most recent session for a machine", description = "Retrieve the most recent session for a specific machine")
    @Parameter(in = ParameterIn.PATH, name = "machineId", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the most recent session",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Session.class))),
            @ApiResponse(responseCode = "204", description = "No content available for the requested machine",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Session> getLatestSession(
            @PathVariable String machineId
    ) {

        Optional<Session> latestSession = sessionService.getMostRecentSessionByMachineId(machineId);

        if (latestSession.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(latestSession.get(), HttpStatus.OK);
    }

    private void createSampleSessionAndEvent() {
        String sessionId = String.valueOf(random.nextInt(100000));
        sessionService.createSession(sessionId, String.valueOf(random.nextInt(100000)), LocalDateTime.now());
        sessionService.createSessionEvent(sessionId, "eventAtValue", "eventTypeValue", random.nextInt(100));
    }

    @PostMapping("/newSession")
    @Operation(summary = "Add a new session", description = "Add a new session")
    @Parameter(name = "sessionId", required = true)
    @Parameter(name = "machineId", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New session added successfully",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Void> newSession(
            @RequestParam(value = "sessionId") String sessionId,
            @RequestParam(value = "machineId") String machineId) {

        if (StringUtils.isNullOrEmpty(sessionId) || StringUtils.isNullOrEmpty(machineId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        sessionService.createSession(sessionId, machineId, LocalDateTime.now());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/newEvent")
    @Operation(summary = "Add a new event", description = "Add a new event for an existing session")
    @Parameter(name = "sessionId", required = true)
    @Parameter(name = "eventType", required = true)
    @Parameter(name = "numericEventValue", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New event added successfully",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Void> newEvent(
            @RequestParam(value = "sessionId") String sessionId,
            @RequestParam(value = "eventType") String eventType,
            @RequestParam(value = "numericEventValue") double numericEventValue) {

        if (StringUtils.isNullOrEmpty(sessionId) || StringUtils.isNullOrEmpty(eventType)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LocalDateTime now = LocalDateTime.now();

        Event event = sessionService.createSessionEvent(sessionId, now.toString(), eventType, numericEventValue);

        if (event == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
