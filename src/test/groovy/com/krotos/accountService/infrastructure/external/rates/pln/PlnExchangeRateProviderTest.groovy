package com.krotos.accountService.infrastructure.external.rates.pln

import com.krotos.accountService.domain.Currency
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class PlnExchangeRateProviderTest extends Specification {

    NbpApiConsumer apiConsumer = Mock()

    @Subject
    PlnExchangeRateProvider rateProvider = new PlnExchangeRateProvider(apiConsumer)

    def "should create ExchangeRate with value received from api consumer"(){
        given:
        def newRateValue = 2.22
        def targetCurrency = Currency.USD
        apiConsumer.fetchAverageRateFor(targetCurrency) >> BigDecimal.valueOf(newRateValue)

        when:
        def rate = rateProvider.fetchCurrentAverageRateFor(targetCurrency)

        then:
        rate.averageRate == BigDecimal.valueOf(newRateValue)
        rate.baseCurrency == Currency.PLN
        rate.targetCurrency == targetCurrency
        rate.date.isAfter(LocalDateTime.now().minusDays(1))
    }
}
