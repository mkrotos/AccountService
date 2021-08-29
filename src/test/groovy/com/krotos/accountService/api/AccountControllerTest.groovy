package com.krotos.accountService.api

import com.krotos.accountService.domain.AccountService
import com.krotos.accountService.domain.Currency
import spock.lang.Specification
import spock.lang.Subject

class AccountControllerTest extends Specification {

    AccountService service = Mock()

    @Subject
    AccountController controller = new AccountController(service)

    def "should return properly formatted account value"() {
        given:
        service.getUserAccountValueIn(Currency.USD, 1) >> BigDecimal.valueOf(10.11111111)

        when:
        def response = controller.getUsdBalance(1)

        then:
        response == "10.1111"
    }
}
