package com.paypaytest.currencyconversionapp.data.sync.initializer

import androidx.work.Constraints
import androidx.work.NetworkType


// All sync work needs an internet connectionS
val SyncConstraints
    get() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
