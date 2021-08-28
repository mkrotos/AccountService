package com.krotos.accountService.infrastructure.external.rates.pln

import com.krotos.accountService.domain.Currency
import com.krotos.accountService.domain.ExchangeRate
import com.krotos.accountService.infrastructure.external.rates.CurrencyExchangeRateProvider
import java.time.LocalDateTime
import org.springframework.stereotype.Component

@Component
class PlnExchangeRateProvider(private val nbpApiConsumer: NbpApiConsumer) : CurrencyExchangeRateProvider {
    override fun fetchCurrentAverageRateFor(targetCurrency: Currency): ExchangeRate {
        val currentRate = nbpApiConsumer.fetchCurrentRateFor(targetCurrency)
        return ExchangeRate(Currency.PLN, targetCurrency, currentRate, LocalDateTime.now())
    }

}
