package com.danijax.currencyconverter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.danijax.currencyconverter.R
import com.danijax.currencyconverter.data.CurrencyRepository
import com.danijax.currencyconverter.data.ErrorStates
import com.danijax.currencyconverter.model.Currency
import com.danijax.currencyconverter.toTimeStamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ExchangeRateViewModel(private val repo: CurrencyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<CurrencyExchangeRateUiState?>(null)
    val uiState = _uiState.asLiveData()

    private val _amountState = MutableStateFlow(0.0F)
    val amountState = _amountState.asLiveData()


    fun getDataStorePreferences() {
        viewModelScope.launch {
            val d = Int
            repo.dataStorePref
                .collect { data ->
                    val message = if (data.success) {
                        R.string.sync_success
                    } else{
                        when(data.error){
                            is ErrorStates.DeveloperError -> R.string.developer_error
                            is ErrorStates.UnKnownError -> R.string.unknown_error
                            is ErrorStates.NetworkError -> R.string.network_error
                            else -> 0
                        }
                    }
                    _uiState.value = CurrencyExchangeRateUiState(
                        message = message,
                        currencies = data.supportedCountries,
                        quotes = data.quotes,
                        syncTime = data.lastSyncTime.toString(),
                        showLoading = data.quotes.isNullOrEmpty()
                    )
                }
        }
    }

    fun updateAmount(amount: Float) {
        _amountState.value = amount
    }
}

data class CurrencyExchangeRateUiState(
    val showLoading: Boolean? = true,
    val message: Int,
    val currencies: List<Currency>? = null,
    val quotes: Map<String, Float>? = null,
    val syncTime: String? = null
)

class CurrencyViewModelFactory(private val repo: CurrencyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExchangeRateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExchangeRateViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
