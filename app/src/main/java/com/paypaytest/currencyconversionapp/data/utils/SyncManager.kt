package com.paypaytest.currencyconversionapp.data.utils

import kotlinx.coroutines.flow.Flow

interface SyncManager {
    val isSyncing: Flow<Boolean>
}