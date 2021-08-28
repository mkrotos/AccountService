package com.krotos.accountService.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class ExchangeRate(
    val baseCurrency: Currency,
    val targetCurrency: Currency,
    val averageRate: BigDecimal,
    val date: LocalDateTime
) {
    fun isTooOld(periodOfValiditySeconds: Long) =
        date.plusSeconds(periodOfValiditySeconds).isBefore(LocalDateTime.now())
}
