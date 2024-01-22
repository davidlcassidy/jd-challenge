package com.davidlcassidy.jdchallenge.controller

import com.davidlcassidy.jdchallenge.model.Session
import com.davidlcassidy.jdchallenge.model.SessionAggregatedEvents
import com.davidlcassidy.jdchallenge.service.EventService
import com.davidlcassidy.jdchallenge.service.SessionService
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Unroll

class SessionControllerTest extends Specification {

    // Variables
    static sessionId = UUID.randomUUID().toString()
    static machineId = UUID.randomUUID().toString()
    static sessionAggregatedEvents = SessionAggregatedEvents.builder().build()

    // Services
    SessionService sessionService = Mock()
    EventService eventService = Mock()

    // Controller
    SessionController sessionController = new SessionController(
            eventService: eventService,
            sessionService: sessionService
    )

    def "getSessionAggregatedEvents should return session aggregated events"() {
        given:
        def expectedEvents = SessionAggregatedEvents.builder()
                .sessionId(sessionId)
                .events([
                        new SessionAggregatedEvents.EventAggregation(eventType: "Type1", totalEventValue: 10.0),
                        new SessionAggregatedEvents.EventAggregation(eventType: "Type2", totalEventValue: 15.0)
                ])
                .build()

        when:
        def response = sessionController.getSessionAggregatedEvents(machineId, sessionId)

        then:
        1 * sessionService.getSessionAggregatedEvents(sessionId) >> Optional.of(expectedEvents)
        response.statusCode == HttpStatus.OK
        response.body == expectedEvents
    }

    @Unroll
    def "getSessionAggregatedEvents should return correct status code when #scenario"() {
        when:
        def response = sessionController.getSessionAggregatedEvents(machineId, sessionId)

        then:
        (expectedResult != null ? 1 : 0) * sessionService.getSessionAggregatedEvents(sessionId) >> expectedResult
        response.statusCode == expectedStatusCode

        where:
        scenario                 | sessionId | machineId | expectedResult                       | expectedStatusCode
        "valid request"          | sessionId | machineId | Optional.of(sessionAggregatedEvents) | HttpStatus.OK
        "non-existent sessionId" | sessionId | machineId | Optional.empty()                     | HttpStatus.NO_CONTENT
        "invalid sessionId parm" | null      | machineId | null                                 | HttpStatus.BAD_REQUEST
        "invalid machineId parm" | sessionId | null      | null                                 | HttpStatus.BAD_REQUEST
    }

    def "getMachineIds should return machine IDs"() {
        given:
        def expectedMachineIds = ["machine1", "machine2"]

        when:
        def response = sessionController.getMachineIds()

        then:
        1 * sessionService.getMachineIds() >> expectedMachineIds
        response.statusCode == HttpStatus.OK
        response.body == expectedMachineIds
    }

    def "getMachineIds should return no content when no sessions exist"() {
        when:
        def response = sessionController.getMachineIds()

        then:
        1 * sessionService.getMachineIds() >> []
        response.statusCode == HttpStatus.NO_CONTENT
    }

    def "getLatestSession should return the latest session"() {
        given:
        def expectedSession = Session.builder()
                .sessionId(sessionId)
                .machineId(machineId)
                .build()

        when:
        def response = sessionController.getLatestSession(machineId)

        then:
        1 * sessionService.getMostRecentSessionByMachineId(machineId) >> Optional.of(expectedSession)
        response.statusCode == HttpStatus.OK
        response.body == expectedSession
    }

    def "getLatestSession should return NO_CONTENT when the latest session is not found"() {
        when:
        def response = sessionController.getLatestSession(machineId)

        then:
        1 * sessionService.getMostRecentSessionByMachineId(machineId) >> Optional.empty()
        response.statusCode == HttpStatus.NO_CONTENT
    }

}
