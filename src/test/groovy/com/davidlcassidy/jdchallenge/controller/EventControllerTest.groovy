package com.davidlcassidy.jdchallenge.controller

import com.davidlcassidy.jdchallenge.model.Event
import com.davidlcassidy.jdchallenge.service.EventService
import com.davidlcassidy.jdchallenge.service.SessionService
import org.springframework.http.HttpStatus
import spock.lang.Specification

class EventControllerTest extends Specification {

    // Services
    SessionService sessionService = Mock()
    EventService eventService = Mock()

    // Controller
    EventController eventController = new EventController(
        eventService: eventService,
        sessionService: sessionService
    )

    def "getEvents should return list of events"() {
        given:
        def sessionId = UUID.randomUUID().toString()
        def expectedEvents = [
                new Event(),
                new Event()
        ]

        when:
        def response = eventController.getEvents("machineId", sessionId)

        then:
        1 * eventService.getEventsBySessionId(sessionId) >> expectedEvents
        response.statusCode == HttpStatus.OK
        response.body == expectedEvents
    }

}
