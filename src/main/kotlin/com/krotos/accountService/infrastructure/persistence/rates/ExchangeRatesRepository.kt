package com.krotos.accountService.infrastructure.persistence.rates

import com.krotos.accountService.domain.Currency
import com.krotos.accountService.domain.ExchangeRate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

private val logger: Logger = LoggerFactory.getLogger(ExchangeRatesRepository::class.java)

@Repository
class ExchangeRatesRepository(private val jpaRepository: ExchangeRatesJPARepository) {
    fun getLastRateFor(currency: Currency, targetCurrency: Currency): ExchangeRate? {
        logger.info("Retrieving last rate for: $currency -> $targetCurrency")
        return jpaRepository
            .getFirstByBaseCurrencyAndTargetCurrencyOrderByIdDesc(currency, targetCurrency)
            ?.toDomainEntity()
    }

    fun saveRate(newRate: ExchangeRate) {
        logger.info("Saving new exchange rate: $newRate")
        jpaRepository.save(toDTO(newRate))
    }

}
