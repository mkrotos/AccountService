package com.krotos.accountService.infrastructure.persistence.account

import com.krotos.accountService.domain.Account
import com.krotos.accountService.domain.Currency
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

const val INVALID_ID = -1L

@Entity
@Table(name = "account")
class AccountDTO {
    @Id
    @Column(name = "id", nullable = false)
    var id: Long = INVALID_ID

    @Column(name = "user_id", nullable = false)
    var userId: Long = INVALID_ID

    @Column(name = "balance", nullable = false)
    var balance: BigDecimal = BigDecimal.ZERO

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    var currency: Currency = Currency.UNDEF

    fun toDomainEntity(): Account {
        return Account(id, userId, balance, currency)
    }

    override fun toString(): String {
        return "AccountDAO(id=$id, userId=$userId, balance=$balance, currency=$currency)"
    }

}