package com.danijax.currencyconverter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danijax.currencyconverter.R
import com.danijax.currencyconverter.databinding.CurrencyItemLayoutBinding
import com.danijax.currencyconverter.generateFlagUrl
import com.danijax.currencyconverter.model.Currency
import com.danijax.currencyconverter.model.Exchange
import com.danijax.currencyconverter.model.ExchangeRateManager
import com.squareup.picasso.Picasso

class ExchangeRatesViewHolder(
    private val binding: CurrencyItemLayoutBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(currency: Currency, manager: ExchangeRateManager, amount: Float) {
        Picasso.get().load(currency.generateFlagUrl()).into(binding.flag)
        binding.countryName.text = currency.Name
        binding.countryCode.text = currency.Code
        val rate = Exchange(manager.baseCurrency, currency)
        if (!manager.getQuotes().isNullOrEmpty()){

            val exRate = manager.fromQuote(rate.rateCode()).rate(manager.getQuotes().toMap())
            exRate?.let {
                binding.rate.text = it.toString()
            }
            binding.result.text = rate.convert(amount, manager.getQuotes(), rate.rateCode()).toString()
        }

    }


    // Helper function for creating new instance of ViewHolder
    companion object {
        fun create(parent: ViewGroup): ExchangeRatesViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.currency_item_layout, parent, false)
            val binding = CurrencyItemLayoutBinding.bind(view)
            return ExchangeRatesViewHolder(binding)
        }
    }
}