package jdr.tv.data.core.initialization

import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import jdr.tv.data.work.RemoveWorker
import jdr.tv.data.work.SyncWorker
import jdr.tv.data.work.extensions.enqueueUniquePeriodicWork
import javax.inject.Inject

class WorkInitializer @Inject constructor(private val context: Context, private val configuration: Configuration) : Initializer {
    override fun initialize() {
        WorkManager.initialize(context, configuration)
        WorkManager.getInstance(context).apply {
            enqueueUniquePeriodicWork<RemoveWorker>(RemoveWorker.createPeriodicWorkRequest())
            enqueueUniquePeriodicWork<SyncWorker>(SyncWorker.createPeriodicWorkRequest())
        }
    }
}
