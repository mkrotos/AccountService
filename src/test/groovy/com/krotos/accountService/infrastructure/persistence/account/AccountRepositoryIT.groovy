package com.krotos.accountService.infrastructure.persistence.account

import com.krotos.accountService.TestData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
class AccountRepositoryIT extends Specification {

    @Autowired
    AccountJPARepository jpaRepository

    @Subject
    def repo

    def setup() {
        repo = new AccountRepository(jpaRepository)
    }

    def "should return account for the given user id"() {
        when:
        def account = repo.findAccount(1)

        then:
        account == TestData.account
    }

    def "should return null when user account do not exists"() {
        when:
        def account = repo.findAccount(99999)

        then:
        account == null
    }
}
