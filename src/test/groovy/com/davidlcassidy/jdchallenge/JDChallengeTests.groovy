package com.davidlcassidy.jdchallenge

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest
@ContextConfiguration(classes = JDChallenge)
class JDChallengeTests extends Specification {

    def "context loads successfully"() {
        when:
        JDChallenge jdChallenge = new JDChallenge()

        then:
        notThrown(Exception)
    }
}

