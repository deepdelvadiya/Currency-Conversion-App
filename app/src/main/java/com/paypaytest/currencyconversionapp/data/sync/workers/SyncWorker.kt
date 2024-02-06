package com.paypaytest.currencyconversionapp.data.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.paypaytest.currencyconversionapp.BuildConfig
import com.paypaytest.currencyconversionapp.common.DispatcherProvider
import com.paypaytest.currencyconversionapp.common.constant.syncWorkerName
import com.paypaytest.currencyconversionapp.data.sync.initializer.SyncConstraints
import com.paypaytest.currencyconversionapp.domain.repository.CurrencyAppRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val currencyAppRepository: CurrencyAppRepository,
    private val dispatcherProvider: DispatcherProvider
) :CoroutineWorker(appContext,workerParams) {

    override suspend fun doWork(): Result = withContext(dispatcherProvider.io){

       val syncedSuccessfully = async {
            currencyAppRepository.syncWith(BuildConfig.API_ID)
        }.await()

        if (syncedSuccessfully){
            Result.success()
        }else{
            Result.retry()
        }
    }

    companion object {
        /**
         * Expedited periodic work to sync data
         */
            fun startUpSyncWork() = PeriodicWorkRequestBuilder<DelegatingWorker>(30,TimeUnit.MINUTES)
            .setConstraints(SyncConstraints)
            .setInputData(SyncWorker::class.delegatedData())
            .build()
    }
}