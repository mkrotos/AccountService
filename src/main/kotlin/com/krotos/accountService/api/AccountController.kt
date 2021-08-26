package com.krotos.accountService.api

import com.krotos.accountService.infrastructure.db.AccountRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.math.RoundingMode

val logger: Logger = LoggerFactory.getLogger(AccountController::class.java)

@RestController
@RequestMapping("/account")
class AccountController(val accountRepository: AccountRepository) {

    @GetMapping
    fun getUsdBalance(@RequestParam(name = "userId") userId: Long): String {
        logger.info("Parameter: $userId")
        logger.info("${accountRepository.getAccountDTOByUserId(userId)}")
        return BigDecimal.valueOf(10.32455675).setScale(4, RoundingMode.HALF_UP).toPlainString()
    }
}