package com.krotos.accountService.infrastructure.external.rates.pln

import java.math.BigDecimal

data class NbpExchangeRatesDTO(
    val table: String?,
    val currency: String?,
    val code: String,
    val rates: List<NbpRateRecord>
)

data class NbpRateRecord(val no: String, val effectiveDate: String, val mid: BigDecimal)
