package com.krotos.accountService.infrastructure.external.rates.pln

import com.krotos.accountService.domain.Currency
import com.krotos.accountService.domain.ProviderFailureException
import java.math.BigDecimal
import java.time.Duration
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

private const val MID_RATES_TABLE = "a"

@Component
class NbpApiConsumer(
    @Value("\${infrastructure.rates.pln.nbp.url}") private val url: String,
    @Value("\${infrastructure.rates.pln.nbp.timeout.seconds}") private val timeoutSeconds: Long,
) {

    private val webClient = WebClient.create(url)
    private val timeoutDuration = Duration.ofSeconds(timeoutSeconds)

    fun fetchAverageRateFor(targetCurrency: Currency): BigDecimal {
        val responseBody = safeCallApi(targetCurrency, MID_RATES_TABLE)
        return responseBody?.rates?.get(0)?.mid
            ?: throw ProviderFailureException("NBP api haven't provided mid rate for $targetCurrency, response is: \n$responseBody")
    }

    private fun safeCallApi(targetCurrency: Currency, table: String): NbpExchangeRatesDTO? {
        return try {
            callApi(targetCurrency, table)
        } catch (ex: Exception) {
            throw ProviderFailureException(ex.message ?: "No message")
        }
    }

    private fun callApi(targetCurrency: Currency, table: String): NbpExchangeRatesDTO? {
        return webClient
            .get()
            .uri("/exchangerates/rates/$table/${targetCurrency.code}/")
            .retrieve()
            .onStatus({ status -> status.isError },
                { throw ProviderFailureException("NBP returned error code: ${it.statusCode()}, reason: ${it.statusCode().reasonPhrase}") })
            .bodyToMono<NbpExchangeRatesDTO>()
            .block(timeoutDuration)
    }

}