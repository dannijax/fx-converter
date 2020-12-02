package com.danijax.currencyconverter.service

import com.danijax.currencyconverter.BuildConfig
import com.danijax.currencyconverter.model.CurrencyResponseModel
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("live")
    suspend fun getQuotes(@Query("access_key") token: String = BuildConfig.ACCESS_KEY): CurrencyResponseModel
}