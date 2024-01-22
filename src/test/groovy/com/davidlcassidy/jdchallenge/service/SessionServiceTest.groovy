package com.davidlcassidy.jdchallenge.service

import com.davidlcassidy.jdchallenge.model.Session
import com.davidlcassidy.jdchallenge.model.Event
import com.davidlcassidy.jdchallenge.repository.EventRepository
import com.davidlcassidy.jdchallenge.repository.SessionRepository
import spock.lang.Specification

import java.time.LocalDateTime

class SessionServiceTest extends Specification {

    // Variables
    static sessionId = UUID.randomUUID().toString()
    static machineId = UUID.randomUUID().toString()
    def startAt = LocalDateTime.now()
    def eventAt = LocalDateTime.now().toString()
    def eventType = "TestEventType"
    def numericEventValue = 42.0
    def sessionList = [
            Session.builder().sessionId("session1").machineId("machine1").startAt(LocalDateTime.now()).build(),
            Session.builder().sessionId("session2").machineId("machine1").startAt(LocalDateTime.now().minusDays(1)).build(),
            Session.builder().sessionId("session3").machineId("machine2").startAt(LocalDateTime.now().minusDays(2)).build()
    ]
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

    def "createSessionEvent should create and save an event"() {
        when:
        def result = sessionService.createSessionEvent(sessionId, eventAt, eventType, numericEventValue)

        then:
        1 * sessionRepository.findBySessionId(sessionId) >> sessionList[0]
        1 * sessionRepository.save(sessionList[0]) >> Session.builder().build()

        and:
        result != null
        result.eventAt == eventAt
        result.eventType == eventType
        result.numericEventValue == numericEventValue
    }

    def "createSessionEvent should return null for unknown session"() {
        when:
        def result = sessionService.createSessionEvent(sessionId, eventAt, eventType, numericEventValue)

        then:
        1 * sessionRepository.findBySessionId(sessionId) >> null
        0 * eventRepository.save(*_)

        result == null
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

    def "getMachineIds should return a list of unique machine IDs"() {
        when:
        def result = sessionService.getMachineIds()

        then:
        1 * sessionRepository.findAll() >> sessionList

        and:
        result.size() == 2
        result.contains("machine1")
        result.contains("machine2")
    }

    def "getMostRecentSessionByMachineId should return the most recent session for a machine ID"() {
        when:
        def result = sessionService.getMostRecentSessionByMachineId(machineId)

        then:
        1 * sessionRepository.findTopByMachineIdOrderByStartAtDesc(machineId) >> Optional.of(sessionList[0])

        and:
        result.isPresent()
        result.get().sessionId == "session1"
        result.get().machineId == "machine1"
        result.get().startAt == sessionList[0].startAt
    }

    def "getMostRecentSessionByMachineId should return empty when no sessions are found for a machine ID"() {
        when:
        def result = sessionService.getMostRecentSessionByMachineId("nonexistentMachineId")

        then:
        1 * sessionRepository.findTopByMachineIdOrderByStartAtDesc("nonexistentMachineId") >> Optional.empty()

        and:
        result.isEmpty()
    }

}
