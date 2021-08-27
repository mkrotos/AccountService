package com.krotos.accountService.domain

import java.math.BigDecimal

class Account(
    val id: Long,
    val userId: Long,
    val balance: BigDecimal,
    val currency: Currency
)