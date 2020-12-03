package com.danijax.currencyconverter.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.danijax.currencyconverter.model.Currency
import com.danijax.currencyconverter.service.work.QuotesDownloadWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private const val CC_PREFERENCES_NAME = "currency_converter_pref"
private const val TAG = "Local Data Source"


/**
 * Local DataStore for quick retrieval and low memory data
 */
class CurrencyLocalDataSource private constructor(private val context: Context) {
    private val executor = Executors.newSingleThreadExecutor()
    private val scope = CoroutineScope(SupervisorJob() + executor.asCoroutineDispatcher())


    /**
     * Initialize DataStore
     */
    private val dataStore: DataStore<Preferences> =
        context.createDataStore(CC_PREFERENCES_NAME)

    /**
     * Keys for accessing preference objects
     */
    private object PreferencesKeys {
        val QUOTES = preferencesKey<String>("quotes")
        val UPDATED_AT = preferencesKey<String>("last sync time")
        val SYNC_INTERVAL = preferencesKey<String>("sync interval")
        val CURRENCIES = preferencesKey<String>("supported currences")
        val ERROR_STATE = preferencesKey<String>("error")
        val SUCCESS_STATE = preferencesKey<String>("success")
    }

    /**
     * Get all the quotes saved in the DataStore
     */
    val quotes = dataStore
        .data
        .map {
            getQuotes(it)
        }


    /**
     * Get All supported currencies from DataStore
     */
    val currencies = dataStore
        .data
        .map { pref ->
            if (getCurrencies(pref).isEmpty()) {
                saveSupportedCurrencies()
            }
            getCurrencies(pref)
        }

    private fun getCurrencies(pref: Preferences): List<Currency> {
        val res = pref[PreferencesKeys.CURRENCIES] ?: Json.encodeToString(emptyList<Currency>())
        return decodeT(res)
    }

    private fun getQuotes(prefs: Preferences): Map<String, Float> {
        val res = prefs[PreferencesKeys.QUOTES] ?: Json.encodeToString(emptyMap<String, Float>())
        return decode(res)
    }

    /**
     * Get the last sync time from DataStore
     */
    val lastUpdate = dataStore
        .data
        .map { pref ->
            getLastSyncTime(pref)
        }

    /**
     * Get the Settings for the sync interval
     */
    val interval = dataStore
        .data
        .map { pref ->
            getInterval(pref)

        }


    /**
     * Get all Data for the UI from Datastore
     */
    val dataStorePreference = dataStore
        .data
        .catch {

        }
        .map { pref ->
            val syncTIme = getLastSyncTime(pref)
            val quotes = getQuotes(pref)
            val currencies = getCurrencies(pref)
            val error = getError(pref)
            val success = getSuccess(pref).toBoolean()
            DataStorePreference(syncTIme, currencies, quotes, error, success)
        }

    private fun getError(pref: Preferences): ErrorStates {
        val error = pref[PreferencesKeys.ERROR_STATE] ?: ""
        Log.e("Tag", error)
        return if (error.isNotEmpty()) Json.decodeFromString(error) else ErrorStates.NoError(0)

    }

    private fun getSuccess(pref: Preferences): String {
        return pref[PreferencesKeys.SUCCESS_STATE] ?: ""
    }

    private fun getInterval(pref: Preferences): String {
        return pref[PreferencesKeys.SYNC_INTERVAL] ?: ""
    }

    private fun getLastSyncTime(pref: Preferences): Long {
        val syncTime = pref[PreferencesKeys.UPDATED_AT] ?: ""
        return if (syncTime.isNotEmpty()) syncTime.toLong() else 0
    }

    private fun encode(quotes: Map<String, Float>): String {
        return Json.encodeToString(quotes)
    }

    private fun decode(json: String): Map<String, Float> {
        return Json.decodeFromString(json)
    }

    private inline fun <reified T> decodeT(json: String): T {
        return Json.decodeFromString(json)
    }


    suspend fun saveQuotes(quotes: Map<String, Float>) {
        save(encode(quotes), PreferencesKeys.QUOTES)
    }

    private suspend fun save(data: String, key: Preferences.Key<String>) {
        dataStore.edit { pref ->
            pref[key] = data
        }
    }


    suspend fun saveUpdateTime(timestamp: Long) {
        save(timestamp.toString(), PreferencesKeys.UPDATED_AT)

    }

    suspend fun saveErrorState(error: ErrorStates){
        val err = Json.encodeToString(error)
        save(err, PreferencesKeys.ERROR_STATE)
    }

    suspend fun saveSuccessState(success: Boolean){
        save(success.toString(), PreferencesKeys.SUCCESS_STATE)
    }

    /**
     * Save Supported currencies from file in Asset and save in DataStore
     */
    fun saveSupportedCurrencies() {
        //TODO: Save only once to improve performance
        val countries =
            context.assets.open("supported_currencies.json").readBytes().toString(Charsets.UTF_8)
        scope.launch {
            save(countries, PreferencesKeys.CURRENCIES)
        }
    }

    /**
     * Model of Data for display on UI
     */
    data class DataStorePreference(
        val lastSyncTime: Long,
        val supportedCountries: List<Currency>,
        val quotes: Map<String, Float>,
        val error: ErrorStates,
        val success: Boolean = false
    )

    companion object {
        @Volatile
        private var INSTANCE: CurrencyLocalDataSource? = null
        private const val defaultSyncInterval = 30L

        fun getInstance(context: Context): CurrencyLocalDataSource {
            return INSTANCE ?: synchronized(this) {
                val instance = initiateWork(context)
                INSTANCE = instance
                instance
            }
        }

        /**
         * Initiate periodic background worker for downloading data from API with
         * Constraints Network connected
         */
        private fun initiateWork(context: Context): CurrencyLocalDataSource {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = PeriodicWorkRequestBuilder<QuotesDownloadWork>(defaultSyncInterval, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(context).enqueue(request)
            return CurrencyLocalDataSource(context)

        }
    }
}