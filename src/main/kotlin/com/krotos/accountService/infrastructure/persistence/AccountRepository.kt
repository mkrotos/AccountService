package com.krotos.accountService.infrastructure.persistence

import com.krotos.accountService.domain.Account
import org.springframework.stereotype.Repository

@Repository
class AccountRepository(private val accountJPARepository: AccountJPARepository) {

    fun findAccount(userId: Long): Account? {
        return accountJPARepository.getAccountDTOByUserId(userId)
            ?.let { Account(it.id, it.userId, it.balance, it.currency) }
    }
}