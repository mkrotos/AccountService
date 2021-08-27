package com.krotos.accountService.domain

import com.krotos.accountService.infrastructure.persistence.AccountRepository
import java.math.BigDecimal
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val exchangeRatesTables: ExchangeRatesTables
) {

    fun getUserAccountValueIn(targetCurrency: Currency, userId: Long): BigDecimal? {
        return accountRepository.findAccount(userId)?.let { user ->
            user.balance.multiply(
                exchangeRatesTables.rateForConversionOf(user.currency).to(targetCurrency)
            )
        }
    }

}