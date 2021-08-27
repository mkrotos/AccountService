package com.krotos.accountService.infrastructure.data.rates

import com.krotos.accountService.domain.Currency
import com.krotos.accountService.domain.ExchangeRate

interface CurrencyExchangeRateProvider {
    fun fetchCurrentAverageRateFor(targetCurrency: Currency): ExchangeRate
}