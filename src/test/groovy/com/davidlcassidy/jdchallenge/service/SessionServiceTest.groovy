package com.davidlcassidy.jdchallenge.service

import com.davidlcassidy.jdchallenge.model.Session
import com.davidlcassidy.jdchallenge.model.Event
import com.davidlcassidy.jdchallenge.repository.EventRepository
import com.davidlcassidy.jdchallenge.repository.SessionRepository
import spock.lang.Specification

class SessionServiceTest extends Specification {

    // Variables
    def sessionId = "testSessionId"
    def machineId = "testMachineId"
    def startAt = "2024-01-19T12:00:00"
    def eventList = [
            Event.builder().eventType("Type1").numericEventValue(5.0).build(),
            Event.builder().eventType("Type2").numericEventValue(3.0).build(),
            Event.builder().eventType("Type1").numericEventValue(2.0).build()
    ]

    // Repositories
    def sessionRepository = Mock(SessionRepository)
    def eventRepository = Mock(EventRepository)

    // Service
    SessionService sessionService = new SessionService(
            sessionRepository: sessionRepository,
            eventRepository: eventRepository
    )

    def "createSession should create and save a session"() {
        when:
        def result = sessionService.createSession(sessionId, machineId, startAt)

        then:
        1 * sessionRepository.save(_ as Session)

        and:
        result != null
        result.sessionId == sessionId
        result.machineId == machineId
        result.startAt == startAt
    }

    def "getSessionAggregatedEvents should return aggregated events for a session"() {
        when:
        def result = sessionService.getSessionAggregatedEvents(sessionId)

        then:
        1 * eventRepository.findBySession_SessionId(sessionId) >> eventList

        and:
        result.isPresent()
        result.get().sessionId == sessionId
        result.get().events.size() == 2
        result.get().events[0].eventType == "Type2"
        result.get().events[0].totalEventValue == 3.0
        result.get().events[1].eventType == "Type1"
        result.get().events[1].totalEventValue == 7.0

    }

    def "getSessionAggregatedEvents should return empty for a session with no events"() {
        when:
        def result = sessionService.getSessionAggregatedEvents(sessionId)

        then:
        1 * eventRepository.findBySession_SessionId(sessionId) >> []

        and:
        result.isEmpty()
    }

}
