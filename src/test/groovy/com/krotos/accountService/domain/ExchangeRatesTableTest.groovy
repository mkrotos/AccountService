package com.krotos.accountService.domain

import com.krotos.accountService.infrastructure.external.rates.ExchangeRatesProvider
import com.krotos.accountService.infrastructure.persistence.rates.ExchangeRatesRepository
import spock.lang.Specification

import java.time.LocalDateTime
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ExchangeRatesTableTest extends Specification {

    private static final BigDecimal OLD_RATE_VALUE = BigDecimal.valueOf(4.44)
    private static final BigDecimal NEW_RATE_VALUE = BigDecimal.valueOf(8.44)
    private static final long REFRESH_PERIOD_SECONDS = 1000L

    def newRate = new ExchangeRate(Currency.PLN, Currency.USD, NEW_RATE_VALUE, LocalDateTime.now())
    def oldRate = new ExchangeRate(Currency.PLN, Currency.USD, OLD_RATE_VALUE, LocalDateTime.now().minusDays(1))

    private ExchangeRatesRepository repository = Mock()
    private ExchangeRatesProvider provider = Mock()

    def "should return rate value taken from the repository"() {
        given:
        1 * repository.getLastRateFor(Currency.PLN, Currency.USD) >> newRate
        def rateTable = new RatesTable(Currency.PLN, repository, provider, REFRESH_PERIOD_SECONDS)

        when:
        def exchangeRate = rateTable.to(Currency.USD)

        then:
        exchangeRate == NEW_RATE_VALUE
    }

    def "should take the value from the repository only once"() {
        given:
        1 * repository.getLastRateFor(Currency.PLN, Currency.USD) >> newRate
        def rateTable = new RatesTable(Currency.PLN, repository, provider, REFRESH_PERIOD_SECONDS)

        when:
        rateTable.to(Currency.USD)
        rateTable.to(Currency.USD)
        def exchangeRate = rateTable.to(Currency.USD)

        then:
        exchangeRate == NEW_RATE_VALUE
    }

    def "should get and return value from the provider when it is not present in the repository"() {
        given:
        1 * provider.getAverageRateFor(Currency.PLN, Currency.USD) >> newRate
        def rateTable = new RatesTable(Currency.PLN, repository, provider, REFRESH_PERIOD_SECONDS)

        when:
        def exchangeRate = rateTable.to(Currency.USD)

        then:
        exchangeRate == NEW_RATE_VALUE
    }

    def "should get and return value from the provider when value from the repository is older than refresh period"() {
        given:
        repository.getLastRateFor(Currency.PLN, Currency.USD) >> oldRate
        1 * provider.getAverageRateFor(Currency.PLN, Currency.USD) >> newRate
        def rateTable = new RatesTable(Currency.PLN, repository, provider, REFRESH_PERIOD_SECONDS)

        when:
        def exchangeRate = rateTable.to(Currency.USD)

        then:
        exchangeRate == NEW_RATE_VALUE
    }

    def "should save new rates taken from the provider to the repository"() {
        given:
        1 * provider.getAverageRateFor(Currency.PLN, Currency.USD) >> newRate
        def rateTable = new RatesTable(Currency.PLN, repository, provider, REFRESH_PERIOD_SECONDS)

        when:
        rateTable.to(Currency.USD)

        then:
        1 * repository.saveRate(newRate)
    }


    def "should return old rate when provider failed"() {
        given:
        repository.getLastRateFor(Currency.PLN, Currency.USD) >> oldRate
        1 * provider.getAverageRateFor(Currency.PLN, Currency.USD) >> { throw new ProviderFailureException("Provider failed") }
        def rateTable = new RatesTable(Currency.PLN, repository, provider, REFRESH_PERIOD_SECONDS)

        when:
        def exchangeRate = rateTable.to(Currency.USD)

        then:
        exchangeRate == OLD_RATE_VALUE
    }

    def "should fetch provider again on next call when the previous rate from it is too old"() {
        given:
        2 * provider.getAverageRateFor(Currency.PLN, Currency.USD) >>> [oldRate, newRate]
        def rateTable = new RatesTable(Currency.PLN, repository, provider, REFRESH_PERIOD_SECONDS)

        when:
        def firstExchangeRate = rateTable.to(Currency.USD)
        def secondExchangeRate = rateTable.to(Currency.USD)

        then:
        firstExchangeRate == OLD_RATE_VALUE
        secondExchangeRate == NEW_RATE_VALUE
    }


    def "should throw ProviderFailureException when failed to get rates from any source"() {
        given:
        1 * provider.getAverageRateFor(Currency.PLN, Currency.USD) >> { throw new ProviderFailureException("Provider failed") }
        def rateTable = new RatesTable(Currency.PLN, repository, provider, REFRESH_PERIOD_SECONDS)

        when:
        rateTable.to(Currency.USD)

        then:
        thrown(ProviderFailureException)
    }

    def "should update rate only once even for many threads"() {
        given:
        def rateTable = new RatesTable(Currency.PLN, repository, provider, REFRESH_PERIOD_SECONDS)

        def tasks = new ArrayList<Callable<BigDecimal>>()
        def threadsNumber = 100
        threadsNumber.times { tasks.add({ rateTable.to(Currency.USD) }) }

        when:
        def results = new ArrayList<BigDecimal>()
        Executors.newFixedThreadPool(threadsNumber)
                .invokeAll(tasks)
                .forEach(future -> results.add( future.get(10, TimeUnit.SECONDS)))

        then:
        1 * provider.getAverageRateFor(Currency.PLN, Currency.USD) >> newRate
        results.forEach({it == BigDecimal.valueOf(NEW_RATE_VALUE)})
    }
}
