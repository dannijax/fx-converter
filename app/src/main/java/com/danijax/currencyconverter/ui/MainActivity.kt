package com.danijax.currencyconverter.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.danijax.currencyconverter.*
import com.danijax.currencyconverter.data.CurrencyLocalDataSource
import com.danijax.currencyconverter.data.CurrencyRepository
import com.danijax.currencyconverter.databinding.ActivityMainBinding
import com.danijax.currencyconverter.model.Currency
import com.danijax.currencyconverter.model.ExchangeRateManager
import com.danijax.currencyconverter.ui.settings.SettingsFragment
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ExchangeRateViewModel
    private lateinit var adapter: SelectCurrencyAdapter
    private lateinit var exchangeListAdapter: ExchangeRateListAdapter
    private var exchangeManager: ExchangeRateManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        manageInputTextFocus()
        val source = CurrencyLocalDataSource.getInstance(this)
        viewModel =
            ViewModelProvider(
                this,
                CurrencyViewModelFactory(CurrencyRepository(source))
            ).get(
                ExchangeRateViewModel::class.java
            )

        adapter = SelectCurrencyAdapter(this)
        setupRecyclerView()

        setUpDropCurrencyDown()
        setUpViewModelListeners()
        viewModel.getDataStorePreferences()

        binding.top.amount.doAfterTextChanged { value ->
            val amount = value.toString().trim()
            val result = if (amount.isNotEmpty()) amount.toFloat() else 0.0F

                viewModel.updateAmount(result)


        }

        binding.settings.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, SettingsFragment(), "")
                .commitNow()

        }
    }

    private fun setUpViewModelListeners() {
        viewModel.uiState.observe(this, {
            val state = it ?: return@observe
            //state.showLoading?.let { show -> binding.progressbar.showOrHide(show) }
            binding.syncTime.text =
                getString(R.string.format_date, state.syncTime?.toLong()?.toDateTime()?.formatFor())
            if (state.message != 0) {
                binding.errorMessage.showOrHide(true)
                binding.errorMessage.setTextColor(Color.RED)
                binding.errorMessage.setText(state.message)
                Toast.makeText(this, getString(state.message), Toast.LENGTH_SHORT).show()
            } else {
                binding.errorMessage.setTextColor(Color.BLACK)
            }
            adapter.update(state.currencies)
            state.quotes?.let {
                exchangeManager = ExchangeRateManager(
                    quotes = state.quotes,
                    supportedCountries = state.currencies!!
                )
                exchangeManager?.let { manager ->

                    exchangeListAdapter.setExchangeRateManager(
                        manager
                    )
                    val base = exchangeManager?.baseCurrency
                    exchangeListAdapter.submitList(state.currencies)
                    binding.top.selectCurrencyDropDown.setText(base?.Code)
                    binding.top.countryName.text = base?.Name
                }
            }


        })

        viewModel.amountState.observe(this, { amount ->
            exchangeListAdapter.updateAmount(amount)
        })
    }

    private fun setUpDropCurrencyDown() {
        binding.top.selectCurrencyDropDown.setAdapter(adapter)

        binding.top.selectCurrencyDropDown.setOnItemClickListener { parent, _, position, _ ->
            val currency = parent.getItemAtPosition(position) as Currency
            binding.top.selectCurrencyDropDown.setText(currency.Code)
            binding.top.countryName.text = currency.Name
            exchangeManager?.baseCurrency = currency
            exchangeListAdapter.setExchangeRateManager(exchangeManager!!)
            binding.top.amount.setText("0")
            Picasso.get().load(currency.generateFlagUrl()).into(binding.top.flag)
        }
    }

    private fun setupRecyclerView() {
        exchangeListAdapter = ExchangeRateListAdapter(exchangeManager)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.currencyList.addItemDecoration(decoration)
        binding.currencyList.layoutManager = LinearLayoutManager(this)
        binding.currencyList.adapter = exchangeListAdapter
    }

    private fun manageInputTextFocus(){
        binding.top.amount.isFocusableInTouchMode = false
        binding.top.amount.isFocusable = false
        binding.top.amount.isFocusableInTouchMode = true
        binding.top.amount.isFocusable = true
    }

}