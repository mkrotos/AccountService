package com.krotos.accountService.domain

import com.krotos.accountService.infrastructure.external.rates.ExchangeRatesProvider
import com.krotos.accountService.infrastructure.persistence.rates.ExchangeRatesRepository
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger(RateTable::class.java)

class RateTable(
    private val currency: Currency,
    private val exchangeRatesRepository: ExchangeRatesRepository,
    private val exchangeRatesProvider: ExchangeRatesProvider,
    private val refreshPeriodSeconds: Long
) {

    private val exchangeRates = EnumMap<Currency, ExchangeRate>(Currency::class.java) // not safe for concurrent

    init {
        validCurrencies()
            .filter { it != currency }
            .forEach { exchangeRates[it] = exchangeRatesRepository.getLastRateFor(currency, it) }
    }

    fun to(targetCurrency: Currency): BigDecimal {
        val oldRate = exchangeRates[targetCurrency]
        val exchangeRate =
            if (oldRate == null || oldRate.isTooOld(refreshPeriodSeconds)) {
                logger.info("Trying to update old rate: $oldRate")
                getNewRateIfPossible(targetCurrency, oldRate)
            } else {
                oldRate
            }
        return exchangeRate.averageRate
    }

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
