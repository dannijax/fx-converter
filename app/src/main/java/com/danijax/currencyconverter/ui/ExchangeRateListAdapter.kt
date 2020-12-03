package com.danijax.currencyconverter.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.danijax.currencyconverter.model.Currency
import com.danijax.currencyconverter.model.ExchangeRateManager

class ExchangeRateListAdapter(private var manager: ExchangeRateManager?) :
    ListAdapter<Currency, ExchangeRatesViewHolder>(
        COMPARATOR
    ) {

    private var amount = 0.0F

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Currency>() {
            override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean =
                oldItem.hashCode() == newItem.hashCode()

            override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean =
                oldItem == newItem
        }
    }

    fun setExchangeRateManager(exchangeRateManager: ExchangeRateManager) {
        this.manager = exchangeRateManager
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRatesViewHolder {
        return ExchangeRatesViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ExchangeRatesViewHolder, position: Int) {
        val data = getItem(position)
        data?.let { apartment ->
            manager?.let { holder.bind(apartment, it, amount) }
        }
    }

    fun updateAmount(amount: Float?) {
        amount?.let {
            this.amount = amount
            notifyDataSetChanged()
        }

    }

}