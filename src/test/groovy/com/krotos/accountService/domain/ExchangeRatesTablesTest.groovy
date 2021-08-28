package com.krotos.accountService.domain

import com.krotos.accountService.infrastructure.external.rates.ExchangeRatesProvider
import com.krotos.accountService.infrastructure.persistence.rates.ExchangeRatesRepository
import spock.lang.Specification

class ExchangeRatesTablesTest extends Specification {

    public static final long REFRESH_PERIOD = 100L
    ExchangeRatesRepository repository = Mock()
    ExchangeRatesProvider provider = Mock()
    def exchangeRatesTables = new ExchangeRatesTables(repository, provider, REFRESH_PERIOD)

    def "should provide rate table for supported currency"(){
        when:
        def ratesTable = exchangeRatesTables.rateForConversionOf(Currency.PLN)

        then:
        ratesTable instanceof RatesTable
    }

    def "should throw exception when getting table for undefined currency"(){
        when:
        exchangeRatesTables.rateForConversionOf(Currency.UNDEF)

        then:
        thrown(NullPointerException)
    }

}
