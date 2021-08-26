package com.krotos.accountService.infrastructure.db

import com.krotos.accountService.domain.Currency
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

const val INVALID_ID = -1L

@Entity
@Table(name = "account")
class AccountDTO {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = INVALID_ID

    @Column(name = "user_id", nullable = false)
    var userId: Long = INVALID_ID

    @Column(name = "balance", nullable = false)
    var balance: BigDecimal = BigDecimal.ZERO

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    var currency: Currency = Currency.UNDEF

    override fun toString(): String {
        return "AccountDAO(id=$id, userId=$userId, balance=$balance, currency=$currency)"
    }

}