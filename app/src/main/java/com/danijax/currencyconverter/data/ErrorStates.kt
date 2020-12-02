package com.danijax.currencyconverter.data

import kotlinx.serialization.Serializable


@Serializable
sealed class ErrorStates() {
    @Serializable
    data class DeveloperError(val code: Int): ErrorStates()
    @Serializable
    data class NetworkError(val code: Int): ErrorStates()
    @Serializable
    data class UnKnownError(val code: Int): ErrorStates()
    @Serializable
    data class NoError(val code: Int): ErrorStates()
}
