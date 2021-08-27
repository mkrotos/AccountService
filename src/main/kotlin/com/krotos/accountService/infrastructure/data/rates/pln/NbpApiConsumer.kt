package com.krotos.accountService.infrastructure.data.rates.pln

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
class NbpApiConsumer(@Value("\${infrastructure.rates.pln.nbp.url}") private val url: String) {

    private val webClient = WebClient.create(url)
    private val timeoutDuration = Duration.ofSeconds(10)

    fun fetchCurrentRateFor(targetCurrency: Currency): BigDecimal {
        return try {
            val responseBody = callApiForCurrentRate(targetCurrency)
            responseBody?.rates?.get(0)?.mid
                ?: throw ProviderFailureException("NBP api haven't provided mid rate for $targetCurrency, response is: \n$responseBody")
        } catch (ex: RuntimeException) {
            throw ProviderFailureException("NBP api haven't responded before the timeout occurred (${timeoutDuration})")
        }
    }

    private fun callApiForCurrentRate(targetCurrency: Currency): NbpExchangeRatesDTO? {
        return webClient.get().uri("/exchangerates/rates/$MID_RATES_TABLE/${targetCurrency.code}/")
            .retrieve()
            .onStatus({ status -> status.isError },
                { throw ProviderFailureException("NBP returned error code: ${it.statusCode()}, \nreason: ${it.statusCode().reasonPhrase}") })
            .bodyToMono<NbpExchangeRatesDTO>()
            .block(timeoutDuration)
    }

}

data class NbpExchangeRatesDTO(
    val table: String?,
    val currency: String?,
    val code: String,
    val rates: List<NbpRateRecord>
)

data class NbpRateRecord(val no: String, val effectiveDate: String, val mid: BigDecimal)