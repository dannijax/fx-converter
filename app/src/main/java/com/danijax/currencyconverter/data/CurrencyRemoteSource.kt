package com.danijax.currencyconverter.data

import com.danijax.currencyconverter.model.CurrencyResponseModel
import com.danijax.currencyconverter.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CurrencyRemoteSource constructor(private val apiService: ApiService) {

    companion object {
        @Volatile
        private var INSTANCE: CurrencyRemoteSource? = null

        fun getInstance(apiService: ApiService): CurrencyRemoteSource {
            return INSTANCE ?: synchronized(this) {
                val instance = CurrencyRemoteSource(apiService)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun getQuotes(): Flow<CurrencyResponseModel> {
        return flow {
            val flowResult = apiService.getQuotes()
            emit(flowResult)
        }.flowOn(Dispatchers.IO)

    }


}