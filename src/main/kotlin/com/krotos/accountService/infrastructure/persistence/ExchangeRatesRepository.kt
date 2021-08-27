package com.krotos.accountService.infrastructure.persistence

import com.krotos.accountService.domain.Currency
import com.krotos.accountService.domain.ExchangeRate
import java.math.BigDecimal
import java.time.LocalDateTime
import org.springframework.stereotype.Service

@Service
class ExchangeRatesRepository {
    fun getLastRateFor(currency: Currency, targetCurrency: Currency): ExchangeRate? {
        return ExchangeRate(Currency.PLN, Currency.USD, BigDecimal.ONE, LocalDateTime.of(2020,1,1,1,1,1))
    }

    fun saveRate(newRate: ExchangeRate) {

    }

}
