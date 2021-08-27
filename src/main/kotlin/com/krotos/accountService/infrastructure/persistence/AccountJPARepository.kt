package com.krotos.accountService.infrastructure.persistence

import org.springframework.data.repository.Repository

@org.springframework.stereotype.Repository
interface AccountJPARepository : Repository<AccountDTO, Long> {

    fun getAccountDTOByUserId(userId: Long): AccountDTO?
}