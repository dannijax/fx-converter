package com.danijax.currencyconverter.service

import com.danijax.currencyconverter.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class ApiClient {

    companion object {
        private fun createLogger(): HttpLoggingInterceptor {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            return logging
        }

        private val contentType = "application/json".toMediaType()
        private val client = OkHttpClient.Builder()
            .addInterceptor(createLogger())
            .build()
        private val retrofit: Retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()

        fun <T> createService(klazz: Class<T>): T = retrofit.create(klazz)

    }

}