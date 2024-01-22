package com.davidlcassidy.jdchallenge.service

import com.davidlcassidy.jdchallenge.model.Session
import com.davidlcassidy.jdchallenge.repository.SessionRepository
import spock.lang.Specification

class SessionServiceTest extends Specification {

    // Variables
    def sessionId = "testSessionId"
    def machineId = "testMachineId"
    def startAt = "2024-01-19T12:00:00"

    // Repositories
    def sessionRepository = Mock(SessionRepository)

    // Service
    SessionService sessionService = new SessionService(
        sessionRepository: sessionRepository
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

}
