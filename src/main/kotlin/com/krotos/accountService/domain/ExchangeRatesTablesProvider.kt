package com.krotos.accountService.domain

import com.krotos.accountService.infrastructure.external.rates.ExchangeRatesProvider
import com.krotos.accountService.infrastructure.persistence.rates.ExchangeRatesRepository
import java.util.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ExchangeRatesTablesProvider(
    private val exchangeRatesRepository: ExchangeRatesRepository,
    private val exchangeRatesProvider: ExchangeRatesProvider,
    @Value("\${exchangeRates.refreshPeriod.seconds}") private val refreshPeriodSeconds: Long
) {

    private val rateTables = EnumMap<Currency, RatesTable>(Currency::class.java)

    init {
        validCurrencies().forEach {
            rateTables[it] = RatesTable(
                it,
                exchangeRatesRepository,
                exchangeRatesProvider,
                refreshPeriodSeconds
            )
        }
    }

    fun rateForConversionOf(currency: Currency): RatesTable {
        return rateTables[currency]!!
    }

}
