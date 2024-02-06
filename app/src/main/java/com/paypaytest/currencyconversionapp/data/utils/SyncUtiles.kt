package com.paypaytest.currencyconversionapp.data.utils

import android.util.Log
import kotlin.coroutines.cancellation.CancellationException


interface Syncable {
    /**
     * Synchronizes the local database backing the repository with the network.
     * Returns if the sync was successful or not.
     */
    suspend fun syncWith(apiId:String): Boolean
}