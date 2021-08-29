package com.krotos.accountService.infrastructure.persistence.rates

import com.krotos.accountService.TestData
import com.krotos.accountService.domain.Currency
import com.krotos.accountService.domain.ExchangeRate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

@SpringBootTest
class ExchangeRatesRepositoryIT extends Specification {

    @Autowired
    ExchangeRatesJPARepository jpaRepository

    @Subject
    ExchangeRatesRepository repo

    def setup() {
        repo = new ExchangeRatesRepository(jpaRepository)
    }

    def "should return newest exchange rate for given currencies"() {
        when:
        def rate = repo.getLastRateFor(Currency.PLN, Currency.USD)

        then:
        rate == TestData.exchangeRate
    }

    def "should return null when exchange rate do not exists in db"() {
        when:
        def rate = repo.getLastRateFor(Currency.UNDEF, Currency.UNDEF)

        then:
        rate == null
    }

}
