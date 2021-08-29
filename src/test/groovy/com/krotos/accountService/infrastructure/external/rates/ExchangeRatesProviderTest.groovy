package com.krotos.accountService.infrastructure.external.rates

import com.krotos.accountService.domain.ConversionUnavailableException
import com.krotos.accountService.domain.Currency
import com.krotos.accountService.domain.ExchangeRate
import com.krotos.accountService.infrastructure.external.rates.pln.PlnExchangeRateProvider
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class ExchangeRatesProviderTest extends Specification {

    PlnExchangeRateProvider plnProvider = Mock()

    @Subject
    ExchangeRatesProvider provider = new ExchangeRatesProvider(plnProvider)

    def "should return rates received from currency rates provider"(){
        given:
        def expectedRate = new ExchangeRate(Currency.PLN, Currency.USD, BigDecimal.ONE, LocalDateTime.now())
        plnProvider.fetchCurrentAverageRateFor(Currency.USD) >> expectedRate

        when:
        def actualRate = provider.getAverageRateFor(Currency.PLN, Currency.USD)

        then:
        actualRate == expectedRate
    }

    def "should throw ConversionUnavailableException when provider for the specified currency is not implemented"(){
        when:
        provider.getAverageRateFor(Currency.UNDEF, Currency.USD)

        then:
        thrown(ConversionUnavailableException)
    }

}
