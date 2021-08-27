package com.krotos.accountService.infrastructure.data.rates

import com.krotos.accountService.domain.ConversionUnavailableException
import com.krotos.accountService.domain.Currency
import com.krotos.accountService.domain.ExchangeRate
import com.krotos.accountService.infrastructure.data.rates.pln.PlnExchangeRateProvider
import java.util.*
import org.springframework.stereotype.Service

@Service
class ExchangeRatesProvider(plnExchangeRateProvider: PlnExchangeRateProvider) {

    private val ratesProviders = EnumMap<Currency, CurrencyExchangeRateProvider>(Currency::class.java)

    init {
        ratesProviders[Currency.PLN] = plnExchangeRateProvider
    }

    fun getAverageRateFor(currency: Currency, targetCurrency: Currency): ExchangeRate {
        val rateProvider = ratesProviders[currency]
            ?: throw ConversionUnavailableException("No registered provider for the $currency")
        return rateProvider.fetchCurrentAverageRateFor(targetCurrency)
    }

}
