package com.krotos.accountService.domain

import com.krotos.accountService.infrastructure.data.rates.ExchangeRatesProvider
import com.krotos.accountService.infrastructure.persistence.ExchangeRatesRepository
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class ExchangeRatesTables(
    private val exchangeRatesRepository: ExchangeRatesRepository,
    private val exchangeRatesProvider: ExchangeRatesProvider,
    @Value("\${exchangeRates.refreshPeriod.seconds}") private val refreshPeriodSeconds: Long
) {

    private val rateTables = EnumMap<Currency, SingleCurrencyExchangeRateTable>(Currency::class.java)

    init {
        Currency.values().filter { it != Currency.UNDEF }.forEach {
            rateTables[it] = SingleCurrencyExchangeRateTable(
                it,
                exchangeRatesRepository,
                exchangeRatesProvider,
                refreshPeriodSeconds
            )
        }
    }

    fun rateForConversionOf(currency: Currency): SingleCurrencyExchangeRateTable {
        return rateTables[currency]!!
    }

}

val logger: Logger = LoggerFactory.getLogger(SingleCurrencyExchangeRateTable::class.java)

class SingleCurrencyExchangeRateTable(
    private val currency: Currency,
    private val exchangeRatesRepository: ExchangeRatesRepository,
    private val exchangeRatesProvider: ExchangeRatesProvider,
    private val refreshPeriodSeconds: Long
) {

    private val exchangeRates = EnumMap<Currency, ExchangeRate>(Currency::class.java) // not safe for concurrent

    init {
        Currency.values().filter { it != Currency.UNDEF }
            .forEach { exchangeRates[it] = exchangeRatesRepository.getLastRateFor(currency, it) }
    }

    fun to(targetCurrency: Currency): BigDecimal {
        val oldRate = exchangeRates[targetCurrency]
        val exchangeRate =
            if (oldRate == null || rateTooOld(oldRate)) {
                getNewRateIfPossible(targetCurrency, oldRate)
            } else {
                oldRate
            }
        return exchangeRate.averageRate
    }

    private fun rateTooOld(oldRate: ExchangeRate) =
        oldRate.date.plusSeconds(refreshPeriodSeconds).isBefore(LocalDateTime.now())

    private fun getNewRateIfPossible(targetCurrency: Currency, oldRate: ExchangeRate?): ExchangeRate {
        return try {
            updateRate(targetCurrency)
        } catch (ex: ProviderFailureException) {
            logger.warn("Couldn't fetch new rate for $currency -> $targetCurrency")
            oldRate ?: throw ProviderFailureException(ex.message!!)
        }
    }

    private fun updateRate(targetCurrency: Currency): ExchangeRate { //not safe for concurrent
        val newRate = exchangeRatesProvider.getAverageRateFor(currency, targetCurrency)
        exchangeRates[targetCurrency] = newRate
        logger.info("Updated rate for $currency -> $targetCurrency = $newRate")
        exchangeRatesRepository.saveRate(newRate)
        return newRate
    }
}
