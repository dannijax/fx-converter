package com.danijax.currencyconverter.data

import android.content.res.AssetManager
import com.danijax.currencyconverter.model.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class CurrencyRepository(assetManager: AssetManager, private val source: CurrencyLocalDataSource) {

    init {
        source.saveSupportedCurrencies()
    }

    val currencies = flowOf(
        getCurrencies(assetManager)
    ).flowOn(Dispatchers.IO)

    val exchangeRates = flowOf(
        getRates(assetManager)
    ).flowOn(Dispatchers.IO)

    private fun getCurrencies(assetManager: AssetManager): List<Currency> {
        val json =
            assetManager.open("supported_currencies.json").readBytes().toString(Charsets.UTF_8)
        return Json.decodeFromString(json)
    }

    private fun getRates(assetManager: AssetManager): Map<String, Float> {
        val json = assetManager.open("exchange_rates.json").readBytes().toString(Charsets.UTF_8)
        return Json.decodeFromString(json)
    }

    fun getQuotes(): Flow<Map<String, Float>> {
        return source.quotes

    }

    suspend fun saveInterval(interval: Long) = source.saveSyncTimeInterval(interval)

    suspend fun saveLastSyncTime(time: Long) = source.updateLastSync(time)

    val syncInterval = source.interval.flowOn(Dispatchers.IO)

    val lastSyncTime = source.lastUpdate.flowOn(Dispatchers.IO)

    val dataStorePref = source.dataStorePreference
}