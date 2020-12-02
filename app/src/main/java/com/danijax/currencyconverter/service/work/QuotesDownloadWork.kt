package com.danijax.currencyconverter.service.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.danijax.currencyconverter.R
import com.danijax.currencyconverter.data.CurrencyLocalDataSource
import com.danijax.currencyconverter.data.CurrencyRemoteSource
import com.danijax.currencyconverter.data.ErrorStates
import com.danijax.currencyconverter.model.CurrencyResponseModel
import com.danijax.currencyconverter.service.ApiClient
import com.danijax.currencyconverter.service.ApiService
import com.danijax.currencyconverter.toTimeStamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import java.time.LocalDateTime

class QuotesDownloadWork(
    private val appContext: Context,
    workerParams: WorkerParameters
) :
    CoroutineWorker(appContext, workerParams) {

    private val networkErrorCode = 1000

    override suspend fun doWork(): Result {
        var result: Result? = null

        getQuotes()
            .catch { ex->

                CurrencyLocalDataSource
                    .getInstance(appContext)
                    .saveErrorState(ErrorStates.NetworkError(networkErrorCode))
                CurrencyLocalDataSource
                    .getInstance(appContext)
                    .saveSuccessState(false)

            }
            .collect { dataResult ->
                result = if (dataResult.success) {
                    //save to DB
                    CurrencyLocalDataSource
                        .getInstance(appContext)
                        .saveQuotes(dataResult.quotes)
                    CurrencyLocalDataSource
                        .getInstance(appContext)
                        .saveUpdateTime(LocalDateTime.now().toTimeStamp())
                    CurrencyLocalDataSource
                        .getInstance(appContext)
                        .saveErrorState(ErrorStates.NoError(0))
                    CurrencyLocalDataSource
                        .getInstance(appContext)
                        .saveSuccessState(true)
                    Result.success()
                } else{
                    val error = dataResult.error
                    error?.code?.let {
                        CurrencyLocalDataSource
                            .getInstance(appContext)
                            .saveErrorState(ErrorStates.DeveloperError(it))
                    }
                    CurrencyLocalDataSource
                        .getInstance(appContext)
                        .saveSuccessState(false)
                    Result.failure()
                }

            }

        return result ?: Result.failure()
    }


    private suspend fun getQuotes(): Flow<CurrencyResponseModel> {
        return CurrencyRemoteSource.getInstance(ApiClient.createService(ApiService::class.java))
            .getQuotes()
    }

}