package com.krotos.accountService.infrastructure.persistence.rates

import com.krotos.accountService.domain.Currency
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ExchangeRatesJPARepository : CrudRepository<ExchangeRateDTO, Long> {

    fun getFirstByBaseCurrencyAndTargetCurrencyOrderByIdDesc(
        baseCurrency: Currency,
        targetCurrency: Currency
    ): ExchangeRateDTO?
}
