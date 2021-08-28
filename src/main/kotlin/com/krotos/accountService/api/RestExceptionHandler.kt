package com.krotos.accountService.api

import com.krotos.accountService.domain.AccountNotFoundException
import com.krotos.accountService.domain.ConversionUnavailableException
import com.krotos.accountService.domain.ProviderFailureException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ProviderFailureException::class)
    fun handleProviderFailure(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        logger.warn("Handling exception: ${ex::class.simpleName}, with message: ${ex.message}")
        val responseBody = "Couldn't convert account value. Try again later."
        return handleExceptionInternal(
            ex, responseBody, HttpHeaders.EMPTY, HttpStatus.FAILED_DEPENDENCY, request
        )
    }

    @ExceptionHandler(ConversionUnavailableException::class)
    fun handleConversionUnavailable(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        logger.warn("Handling exception: ${ex::class.simpleName}, with message: ${ex.message}")
        val responseBody = "Conversion for the given account currency is not available."
        return handleExceptionInternal(
            ex, responseBody, HttpHeaders.EMPTY, HttpStatus.NOT_IMPLEMENTED, request
        )
    }

    @ExceptionHandler(AccountNotFoundException::class)
    fun handleAccountNotFound(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        logger.warn("Handling exception: ${ex::class.simpleName}, with message: ${ex.message}")
        val responseBody = "Account not found"
        return handleExceptionInternal(
            ex, responseBody, HttpHeaders.EMPTY, HttpStatus.NOT_FOUND, request
        )
    }
}