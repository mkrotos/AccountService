package com.krotos.accountService.infrastructure.persistence.account

import com.krotos.accountService.domain.Account
import com.krotos.accountService.infrastructure.persistence.rates.ExchangeRatesRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

private val logger: Logger = LoggerFactory.getLogger(AccountRepository::class.java)

@Repository
class AccountRepository(private val jpaRepository: AccountJPARepository) {

    fun findAccount(userId: Long): Account? {
        logger.info("Retrieving account for user: $userId")
        return jpaRepository
            .getAccountDTOByUserId(userId)
            ?.toDomainEntity()
    }
}