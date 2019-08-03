package jdr.tv.remote.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import jdr.tv.base.Dispatchers.IOExecutor
import jdr.tv.base.Log
import jdr.tv.base.extensions.MOSHI_DEFAULT
import jdr.tv.base.extensions.addAdapter
import jdr.tv.base.extensions.addListAdapter
import jdr.tv.remote.TmdbApi
import jdr.tv.remote.adapter.RemoteGenreAdapter
import jdr.tv.remote.adapter.RemoteSeasonListAdapter
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
object RemoteModule {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    private fun provideHttpLoggingInterceptor() = Interceptor { chain ->
        chain.proceed(chain.request().apply { Log.v("--> $method $url -->") })
    }

    @Singleton
    @Provides
    @JvmStatic
    fun providesInterceptor(@Named("TMDB_API_KEY") tmdbApiKey: String) = Interceptor { chain ->
        chain.request().run {
            val url = url.newBuilder().addQueryParameter("api_key", tmdbApiKey).build()
            chain.proceed(newBuilder().url(url).build())
        }
    }

    @Singleton
    @Provides
    @JvmStatic
    fun providesOkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .dispatcher(Dispatcher(IOExecutor))
            .addInterceptor(interceptor)
            .addInterceptor(provideHttpLoggingInterceptor())
            .build()
    }

    @Singleton
    @Provides
    @JvmStatic
    fun providesTmdbApi(client: OkHttpClient): TmdbApi {
        val moshi: Moshi = MOSHI_DEFAULT.newBuilder()
            .addAdapter(RemoteGenreAdapter())
            .addListAdapter(RemoteSeasonListAdapter())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create()
    }
}
