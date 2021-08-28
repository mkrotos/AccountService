package com.krotos.accountService.domain

import com.krotos.accountService.infrastructure.external.rates.ExchangeRatesProvider
import com.krotos.accountService.infrastructure.persistence.rates.ExchangeRatesRepository
import java.math.BigDecimal
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger(RatesTable::class.java)

class RatesTable(
    private val currency: Currency,
    private val exchangeRatesRepository: ExchangeRatesRepository,
    private val exchangeRatesProvider: ExchangeRatesProvider,
    private val refreshPeriodSeconds: Long
) {

    private val exchangeRates = EnumMap<Currency, ExchangeRate>(Currency::class.java)

    init {
        validCurrencies()
            .filter { it != currency }
            .forEach { exchangeRates[it] = exchangeRatesRepository.getLastRateFor(currency, it) }
    }

    fun to(targetCurrency: Currency): BigDecimal {
        val currentRate = exchangeRates[targetCurrency]
        val exchangeRate =
            if (currentRate == null || currentRate.isTooOld(refreshPeriodSeconds)) {
                logger.info("Trying to update old rate: $currentRate")
                getNewRateIfPossible(targetCurrency, currentRate)
            } else {
                currentRate
            }
        return exchangeRate.averageRate
    }

    private fun getNewRateIfPossible(targetCurrency: Currency, oldRate: ExchangeRate?): ExchangeRate {
        return try {
            safeUpdateRate(targetCurrency)
        } catch (ex: ProviderFailureException) {
            logger.warn("Couldn't fetch new rate for $currency -> $targetCurrency, exception message: ${ex.message}")
            oldRate ?: throw ProviderFailureException(ex.message!!)
        }
    }

    private fun safeUpdateRate(targetCurrency: Currency): ExchangeRate {
        val rate = synchronized(this) {
            val currentRate = exchangeRates[targetCurrency]
            if (currentRate != null && !currentRate.isTooOld(refreshPeriodSeconds)) return currentRate

            val newRate = exchangeRatesProvider.getAverageRateFor(currency, targetCurrency)
            exchangeRates[targetCurrency] = newRate
            newRate
        }
        logger.info("Updated rate for $currency -> $targetCurrency = $rate")
        exchangeRatesRepository.saveRate(rate)
        return rate
    }
}
