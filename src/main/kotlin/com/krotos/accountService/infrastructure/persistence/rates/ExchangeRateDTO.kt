package com.krotos.accountService.infrastructure.persistence.rates

import com.krotos.accountService.domain.Currency
import com.krotos.accountService.domain.ExchangeRate
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

const val INVALID_ID = -1L


fun fromDomainEntity(exchangeRate: ExchangeRate): ExchangeRateDTO {
    val dto = ExchangeRateDTO()
    dto.baseCurrency = exchangeRate.baseCurrency
    dto.targetCurrency = exchangeRate.targetCurrency
    dto.averageRate = exchangeRate.averageRate
    dto.date = exchangeRate.date
    return dto
}


@Entity
@Table(name = "exchange_rate")
class ExchangeRateDTO {

    @Id
    @Column(name = "id", nullable = false)
    var id: Long = INVALID_ID

    @Column(name = "base_currency", nullable = false)
    @Enumerated(EnumType.STRING)
    var baseCurrency: Currency = Currency.UNDEF

    @Column(name = "target_currency", nullable = false)
    @Enumerated(EnumType.STRING)
    var targetCurrency: Currency = Currency.UNDEF

    @Column(name = "average_rate", nullable = false)
    var averageRate: BigDecimal = BigDecimal.ZERO

    @Column(name = "date", nullable = false)
    var date: LocalDateTime = LocalDateTime.MIN

    fun toDomainEntity(): ExchangeRate {
        return ExchangeRate(baseCurrency, targetCurrency, averageRate, date)
    }

}
