package com.krotos.accountService.domain

fun validCurrencies(): List<Currency> {
    return Currency.values().filter { it != Currency.UNDEF }
}

enum class Currency(val code: String) {
    PLN("pln"), USD("usd"), UNDEF("undefined");
}