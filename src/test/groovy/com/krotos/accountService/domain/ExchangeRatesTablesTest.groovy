package com.krotos.accountService.domain

import com.krotos.accountService.infrastructure.external.rates.ExchangeRatesProvider
import com.krotos.accountService.infrastructure.persistence.ExchangeRatesRepository
import spock.lang.Specification

class ExchangeRatesTablesTest extends Specification {

    public static final long REFRESH_PERIOD = 100L
    ExchangeRatesRepository repository = Mock()
    ExchangeRatesProvider provider = Mock()
    def exchangeRatesTables = new ExchangeRatesTables(repository, provider, REFRESH_PERIOD)

    def "should provide rate table for supported currency"(){
        when:
        def rateTable = exchangeRatesTables.rateForConversionOf(Currency.PLN)

        then:
        rateTable instanceof RateTable
    }

    def "should throw exception when getting table for undefined currency"(){
        when:
        def rateTable = exchangeRatesTables.rateForConversionOf(Currency.UNDEF)

        then:
        thrown(NullPointerException)
    }

}
