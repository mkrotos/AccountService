package com.krotos.accountService.infrastructure.db

import org.springframework.data.repository.Repository

@org.springframework.stereotype.Repository
interface AccountRepository : Repository<AccountDTO, Long> {

    fun getAccountDTOByUserId(userId: Long): AccountDTO?
}