package com.krotos.accountService.infrastructure.external.rates.pln

import com.krotos.accountService.TestData
import com.krotos.accountService.domain.Currency
import com.krotos.accountService.domain.ProviderFailureException
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import spock.lang.Specification
import spock.lang.Subject

import java.util.concurrent.TimeUnit

class NbpApiConsumerIT extends Specification {

    private static MockWebServer mockWebServer

    @Subject
    private NbpApiConsumer nbpApi

    def setupSpec() {
        mockWebServer = new MockWebServer()
        mockWebServer.start()
    }

    def setup() {
        def url = String.format("http://localhost:%s", mockWebServer.getPort())
        nbpApi = new NbpApiConsumer(url, 10)
    }

    def cleanupSpec() {
        mockWebServer.shutdown()
    }

    def "should call nbp api and return current exchange rate value"() {
        given:
        mockWebServer.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200 OK")
                .setBody(TestData.getValidNbpResponse())
                .addHeader("Content-Type", "application/json"))

        when:
        def rateValue = nbpApi.fetchAverageRateFor(Currency.USD)
        def requestPath = mockWebServer.takeRequest(1, TimeUnit.SECONDS).path

        then:
        rateValue == TestData.nbpResponseMidValue
        requestPath == "/exchangerates/rates/a/usd/"
    }

    def "should throw ProviderFailureException on response status 4**"() {
        given:
        mockWebServer.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 404 NOT_FOUND"))
        when:
        nbpApi.fetchAverageRateFor(Currency.USD)

        then:
        def ex = thrown(ProviderFailureException)
        ex.message.contains("NBP returned error code: 404 NOT_FOUND, reason: Not Found")
    }

    def "should throw ProviderFailureException on response status 5**"() {
        given:
        mockWebServer.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 500 INTERNAL_SERVER_ERROR"))
        when:
        nbpApi.fetchAverageRateFor(Currency.USD)

        then:
        def ex = thrown(ProviderFailureException)
        ex.message.contains("NBP returned error code: 500 INTERNAL_SERVER_ERROR, reason: Internal Server Error")
    }

    def "should throw ProviderFailureException on timeout"() {
        given:
        def url = String.format("http://localhost:%s/api", mockWebServer.getPort())
        nbpApi = new NbpApiConsumer(url, 0)
        when:
        nbpApi.fetchAverageRateFor(Currency.USD)

        then:
        def ex = thrown(ProviderFailureException)
        ex.message.contains("Timeout on blocking read for 0 NANOSECONDS")
    }

    def "should throw ProviderFailureException on invalid response"() {
        given:
        mockWebServer.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200 OK")
                .setBody(TestData.getInvalidNbpResponse())
                .addHeader("Content-Type", "application/json"))

        when:
        nbpApi.fetchAverageRateFor(Currency.USD)

        then:
        def ex = thrown(ProviderFailureException)
        ex.message.contains("NBP api haven't provided mid rate for USD, response is:")
    }
}
