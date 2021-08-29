package com.krotos.accountService.domain

import com.krotos.accountService.infrastructure.persistence.account.AccountRepository
import java.math.BigDecimal
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val exchangeRatesTables: ExchangeRatesTablesProvider
) {

    fun getUserAccountValueIn(targetCurrency: Currency, userId: Long): BigDecimal {
        val account = accountRepository.findAccount(userId)
            ?: throw AccountNotFoundException("Account not present in the repository")

        return if (account.currency == targetCurrency) {
            account.balance
        } else {
            account.balance.multiply(
                exchangeRatesTables.rateForConversionOf(account.currency).to(targetCurrency)
            )
        }
    }

}