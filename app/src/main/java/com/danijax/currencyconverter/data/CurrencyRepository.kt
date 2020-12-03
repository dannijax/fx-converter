package com.danijax.currencyconverter.data

class CurrencyRepository( source: CurrencyLocalDataSource) {

    init {
        //TODO: Save supported currencies on once to improve performance
        source.saveSupportedCurrencies()
    }

    val dataStorePref = source.dataStorePreference
}