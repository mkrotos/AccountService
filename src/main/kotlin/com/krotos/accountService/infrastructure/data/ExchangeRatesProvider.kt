package com.krotos.accountService.infrastructure.data

import com.krotos.accountService.domain.Currency
import com.krotos.accountService.domain.ExchangeRate
import java.math.BigDecimal
import java.time.LocalDateTime
import org.springframework.stereotype.Service

@Service
class ExchangeRatesProvider {
    fun getRateFor(currency: Currency, targetCurrency: Currency): ExchangeRate {
        return ExchangeRate(Currency.PLN, Currency.USD, BigDecimal.TEN, LocalDateTime.now())
    }

}
