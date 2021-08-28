package com.krotos.accountService.domain

import com.krotos.accountService.infrastructure.persistence.account.AccountRepository
import spock.lang.Specification

import static org.mockito.BDDMockito.given
import static org.mockito.Mockito.mock

class AccountServiceTest extends Specification {

    AccountRepository accountRepository = Mock()
    ExchangeRatesTables exchangeRatesTables = Mock()
    def accountService = new AccountService(accountRepository, exchangeRatesTables)

    def "one plus one"() {
        expect:
        1 + 1 == 2
    }

    def "should return null when can't find user account"() {
        when:
        def value = accountService.getUserAccountValueIn(Currency.USD, 111)

        then:
        value == null
    }

    def "should return account balance when request currency is equal account currency"() {
        given:
        def userId = 10L
        def balance = BigDecimal.TEN
        accountRepository.findAccount(userId) >> new Account(1L, userId, balance, Currency.PLN)

        when:
        def value = accountService.getUserAccountValueIn(Currency.PLN, userId)

        then:
        value == balance
    }

    def "should return account value in target currency"() {
        given:
        def userId = 10L
        def balance = BigDecimal.TEN
        def targetCurrency = Currency.USD
        def accountCurrency = Currency.PLN
        def rateTable = mock(RateTable)

        accountRepository.findAccount(userId) >> new Account(1L, userId, balance, accountCurrency)
        exchangeRatesTables.rateForConversionOf(accountCurrency) >> rateTable
        given(rateTable.to(targetCurrency)).willReturn(BigDecimal.valueOf(4.44))

        when:
        def value = accountService.getUserAccountValueIn(targetCurrency, userId)

        then:
        value == BigDecimal.valueOf(44.4)
    }


}
