package com.krotos.accountService.api

import com.krotos.accountService.domain.AccountService
import com.krotos.accountService.domain.Currency
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerIT extends Specification {

    @Autowired
    TestRestTemplate restTemplate

    @SpringBean
    AccountService service = Mock()

    def "should respond with account value in usd"() {
        given:
        service.getUserAccountValueIn(Currency.USD, 1) >> BigDecimal.valueOf(4.44)

        when:
        def response = restTemplate.getForEntity("/account/usd?userId=1", String)

        then:
        response.statusCode == HttpStatus.OK
        response.body == "4.4400"
    }

}
