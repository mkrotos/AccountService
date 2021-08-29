package com.krotos.accountService

import com.krotos.accountService.domain.Account
import com.krotos.accountService.domain.Currency
import com.krotos.accountService.domain.ExchangeRate

import java.time.LocalDateTime

class TestData {
    private static final String testDataDirectory = "src/test/resources/testData"

    static def getValidNbpResponse() {
        return new File(testDataDirectory + "/nbpValidResponse.json").text
    }

    public static nbpResponseMidValue = BigDecimal.valueOf(3.8978)

    static def getInvalidNbpResponse() {
        return new File(testDataDirectory + "/nbpInvalidResponse.json").text
    }

    public static exchangeRate =
            new ExchangeRate(
                    Currency.PLN,
                    Currency.USD,
                    BigDecimal.valueOf(3.77),
                    LocalDateTime.of(2021, 05, 01, 11, 11, 11, 550000000)
            )

    public static account = new Account(11, 1, BigDecimal.valueOf(100), Currency.PLN)
}
