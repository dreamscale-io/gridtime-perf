package org.dreamscale.performance

import spock.lang.Specification

class BenchmarkSpec extends Specification {

    def "test wiring"() {
        given:

        String a = "a"
        String b = "b"

        when:

        String concat = a + b;

        then:

        assert concat == "ab"

    }
}
