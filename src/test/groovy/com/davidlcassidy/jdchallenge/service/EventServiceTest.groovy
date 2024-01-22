package com.davidlcassidy.jdchallenge.service

import com.davidlcassidy.jdchallenge.model.Session
import com.davidlcassidy.jdchallenge.repository.EventRepository
import com.davidlcassidy.jdchallenge.repository.SessionRepository
import spock.lang.Specification

class EventServiceTest extends Specification {

    // Variables
    def sessionId = "testSessionId"
    def eventAt = "2024-01-19T12:00:00"
    def eventType = "TestEventType"
    def numericEventValue = 42.0

    //Models
    def session = new Session(sessionId: sessionId)

    // Repositories
    def sessionRepository = Mock(SessionRepository)
    def eventRepository = Mock(EventRepository)

    // Service
    EventService eventService = new EventService(
        sessionRepository: sessionRepository,
        eventRepository: eventRepository
    )

    def "createEvent should create and save an event"() {
        when:
        def result = eventService.createEvent(sessionId, eventAt, eventType, numericEventValue)

        then:
        1 * sessionRepository.findBySessionId(sessionId) >> session
        1 * sessionRepository.save(session) >> new Session()

        and:
        result != null
        result.eventAt == eventAt
        result.eventType == eventType
        result.numericEventValue == numericEventValue
    }

    def "createEvent should return null for unknown session"() {
        when:
        def result = eventService.createEvent(sessionId, eventAt, eventType, numericEventValue)

        then:
        1 * sessionRepository.findBySessionId(sessionId) >> null
        0 * eventRepository.save(*_)

        result == null
    }

}
