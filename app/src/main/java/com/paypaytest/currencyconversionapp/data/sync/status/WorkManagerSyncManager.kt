package com.paypaytest.currencyconversionapp.data.sync.status

import android.content.Context
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.paypaytest.currencyconversionapp.common.constant.syncWorkerName
import com.paypaytest.currencyconversionapp.data.utils.SyncManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkManagerSyncManager @Inject constructor(
    @ApplicationContext private  val context: Context
):SyncManager {

    override val isSyncing: Flow<Boolean>
        get() = WorkManager.getInstance(context).getWorkInfosForUniqueWorkFlow(syncWorkerName)
            .map(List<WorkInfo>::anyRunning)
            .conflate()
}

private fun List<WorkInfo>.anyRunning() = any { it.state == WorkInfo.State.RUNNING }
