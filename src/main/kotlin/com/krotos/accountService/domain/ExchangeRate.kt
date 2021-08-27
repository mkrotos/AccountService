package com.krotos.accountService.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class ExchangeRate(
    val from: Currency,
    val to: Currency,
    val averageRate: BigDecimal,
    val date: LocalDateTime
)
