package jdr.tv.data.work.di

import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import androidx.work.WorkerFactory
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton
import jdr.tv.data.local.Database
import jdr.tv.data.remote.TmdbApi
import jdr.tv.data.work.RemoveWorker
import jdr.tv.data.work.SyncWorker
import jdr.tv.data.work.WorkerProviderFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

@Module
object WorkModule {

    @Singleton
    @Provides
    @IntoSet
    fun providesRemoveWorkerFactory(database: Database): WorkerFactory {
        return WorkerProviderFactory(RemoveWorker::class.java.name) { context, params ->
            RemoveWorker(context, params, database)
        }
    }

    @Singleton
    @Provides
    @IntoSet
    fun providesSyncWorkerFactory(database: Database, tmdbApi: TmdbApi): WorkerFactory {
        return WorkerProviderFactory(SyncWorker::class.java.name) { context, params ->
            SyncWorker(context, params, database, tmdbApi)
        }
    }

    @Singleton
    @Provides
    fun providesWorkConfiguration(workerFactorySet: Set<@JvmSuppressWildcards WorkerFactory>): Configuration {
        return Configuration.Builder()
            .setExecutor(Dispatchers.IO.asExecutor())
            .setTaskExecutor(Dispatchers.IO.asExecutor())
            .setWorkerFactory(DelegatingWorkerFactory().apply { workerFactorySet.forEach(this::addFactory) })
            .build()
    }
}
