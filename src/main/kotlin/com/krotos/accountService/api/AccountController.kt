package com.krotos.accountService.api

import com.krotos.accountService.domain.AccountService
import com.krotos.accountService.domain.Currency
import java.math.RoundingMode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

val logger: Logger = LoggerFactory.getLogger(AccountController::class.java)

@RestController
@RequestMapping("/account")
class AccountController(val accountService: AccountService) {

    @GetMapping
    fun getUsdBalance(@RequestParam(name = "userId") userId: Long): String? {
        logger.info("Parameter: $userId")
        return accountService.getUserAccountValueIn(Currency.USD, userId)
            ?.setScale(4, RoundingMode.HALF_UP)
            ?.toPlainString()
    }
}