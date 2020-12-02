package com.danijax.currencyconverter.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.danijax.currencyconverter.R
import com.danijax.currencyconverter.databinding.CurrencySelectionLayoutBinding
import com.danijax.currencyconverter.generateFlagUrl
import com.danijax.currencyconverter.model.Currency
import com.squareup.picasso.Picasso

class SelectCurrencyAdapter(context: Context) :
    ArrayAdapter<Currency>(context, R.layout.currency_selection_layout) {

    private var items: MutableList<Currency> = mutableListOf()

    override fun getCount() = items.size

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.currency_selection_layout, parent, false)
            holder = ViewHolder(view)


            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }
        holder.bind(items[position])
        return view
    }

    fun update(currencies: List<Currency>?) {
        currencies?.let {

            items.clear()
            items.addAll(it)
            notifyDataSetChanged()
        }
    }

    //Helper object
    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    private class ViewHolder(view: View) {
        private val binding = CurrencySelectionLayoutBinding.bind(view)

        fun bind(currency: Currency) {
            binding.currencyCode.text = currency.Code
            binding.countryName.text = currency.Name
            Picasso.get().load(currency.generateFlagUrl()).into(binding.flag)
        }
    }


}