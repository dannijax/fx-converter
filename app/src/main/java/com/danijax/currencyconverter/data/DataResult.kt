package com.danijax.currencyconverter.data

sealed class DataResult<out T> {
    class Success<T>(val message: String, val data: T) : DataResult<T>()
    class Error<T>(message: String) : DataResult<T>()
    class TokenExpiry(message: String) : DataResult<Nothing>()
}
