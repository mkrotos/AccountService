package com.krotos.accountService.domain

import com.krotos.accountService.infrastructure.external.rates.ExchangeRatesProvider
import com.krotos.accountService.infrastructure.persistence.rates.ExchangeRatesRepository
import spock.lang.Specification
import spock.lang.Subject

class ExchangeRatesTablesProviderTest extends Specification {

    static final long REFRESH_PERIOD = 100L

    ExchangeRatesRepository repository = Mock()
    ExchangeRatesProvider provider = Mock()

    @Subject
    def exchangeRatesTables = new ExchangeRatesTablesProvider(repository, provider, REFRESH_PERIOD)

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
