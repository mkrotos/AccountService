package com.krotos.accountService.api

import com.krotos.accountService.domain.*
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestExceptionHandlerIT extends Specification {

    @Autowired
    TestRestTemplate restTemplate

    @SpringBean
    AccountService service = Mock()

    def "should respond 404 if account do not exists"() {
        given:
        service.getUserAccountValueIn(Currency.USD, 99) >> { throw new AccountNotFoundException("not found") }

        when:
        def response = restTemplate.getForEntity("/account/usd?userId=99", String)

        then:
        response.statusCode == HttpStatus.NOT_FOUND
        response.body == "Account not found"
    }

    def "should respond 424 if can't find exchange rate"() {
        given:
        service.getUserAccountValueIn(Currency.USD, 1) >> { throw new ProviderFailureException("Provider failed") }

        when:
        def response = restTemplate.getForEntity("/account/usd?userId=1", String)

        then:
        response.statusCode == HttpStatus.FAILED_DEPENDENCY
        response.body == "Couldn't convert account value. Try again later."
    }

    def "should respond 501 when account value is in currency which can't be converted"() {
        given:
        service.getUserAccountValueIn(Currency.USD, 1) >> { throw new ConversionUnavailableException("Can't convert") }

        when:
        def response = restTemplate.getForEntity("/account/usd?userId=1", String)

        then:
        response.statusCode == HttpStatus.NOT_IMPLEMENTED
        response.body == "Conversion for the given account currency is not available."
    }
}
