package com.krotos.accountService.domain

class ProviderFailureException(message: String) : Exception(message)

class ConversionUnavailableException(message: String): Exception(message)

class AccountNotFoundException(message: String): Exception(message)