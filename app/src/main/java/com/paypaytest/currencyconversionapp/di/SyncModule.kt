package com.paypaytest.currencyconversionapp.di

import com.paypaytest.currencyconversionapp.data.sync.status.WorkManagerSyncManager
import com.paypaytest.currencyconversionapp.data.utils.SyncManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SyncModule {

    @Binds
    fun bindsSyncStatusMonitor(
        syncStatusMonitor: WorkManagerSyncManager,
    ): SyncManager
}