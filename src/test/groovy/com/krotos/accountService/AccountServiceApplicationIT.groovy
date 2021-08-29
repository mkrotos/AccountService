package com.krotos.accountService

import com.krotos.accountService.domain.Currency
import com.krotos.accountService.domain.ProviderFailureException
import com.krotos.accountService.infrastructure.external.rates.pln.NbpApiConsumer
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountServiceApplicationIT extends Specification {

    @Autowired
    TestRestTemplate restTemplate

    @SpringBean
    NbpApiConsumer nbpApiConsumer = Mock()

    def "context loads"() {
        expect: "application started"
    }

    @DirtiesContext
    def "should return account balance value in usd"() {
        given:
        nbpApiConsumer.fetchAverageRateFor(Currency.USD) >> 4.444

        when:
        def response = restTemplate.getForEntity("/account/usd?userId=1", String)

        then:
        response.statusCode == HttpStatus.OK
        response.body == "444.4000"
    }

    @DirtiesContext
    def "should return account balance value in usd using db exchange rate when nbp api failed"() {
        given:
        nbpApiConsumer.fetchAverageRateFor(Currency.USD) >> { throw new ProviderFailureException("Provider failed") }

        when:
        def response = restTemplate.getForEntity("/account/usd?userId=1", String)

        then:
        response.statusCode == HttpStatus.OK
        response.body == "377.0000"
    }
}
