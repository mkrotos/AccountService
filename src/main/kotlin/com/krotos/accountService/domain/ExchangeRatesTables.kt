package com.krotos.accountService.domain

import com.krotos.accountService.infrastructure.external.rates.ExchangeRatesProvider
import com.krotos.accountService.infrastructure.persistence.rates.ExchangeRatesRepository
import java.util.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ExchangeRatesTables(
    private val exchangeRatesRepository: ExchangeRatesRepository,
    private val exchangeRatesProvider: ExchangeRatesProvider,
    @Value("\${exchangeRates.refreshPeriod.seconds}") private val refreshPeriodSeconds: Long
) {

    private val rateTables = EnumMap<Currency, RateTable>(Currency::class.java)

    init {
        validCurrencies().forEach {
            rateTables[it] = RateTable(
                it,
                exchangeRatesRepository,
                exchangeRatesProvider,
                refreshPeriodSeconds
            )
        }
    }

    fun rateForConversionOf(currency: Currency): RateTable {
        return rateTables[currency]!!
    }

}
