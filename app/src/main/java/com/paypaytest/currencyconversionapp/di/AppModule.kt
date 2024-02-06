package com.paypaytest.currencyconversionapp.di

import android.content.Context
import androidx.room.Room
import com.paypaytest.currencyconversionapp.BuildConfig
import com.paypaytest.currencyconversionapp.common.DispatcherProvider
import com.paypaytest.currencyconversionapp.common.constant.currencyAppDatabaseName
import com.paypaytest.currencyconversionapp.data.database.CurrencyAppDatabase
import com.paypaytest.currencyconversionapp.data.database.dao.CountryDao
import com.paypaytest.currencyconversionapp.data.database.dao.RateDao
import com.paypaytest.currencyconversionapp.data.remote.moshi_adapters.CountryCodeEntityAdapter
import com.paypaytest.currencyconversionapp.data.remote.OpenExchangeRatesApis
import com.paypaytest.currencyconversionapp.data.remote.moshi_adapters.RatesAdapter
import com.paypaytest.currencyconversionapp.data.repository.CurrencyAppRepositoryImp
import com.paypaytest.currencyconversionapp.domain.repository.CurrencyAppRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesMoshi():Moshi{
        // Create a Moshi instance with the custom adapter
        return Moshi.Builder()
            .add(RatesAdapter())
            .add(CountryCodeEntityAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                },
        )
        .connectTimeout(100,TimeUnit.SECONDS)
        .readTimeout(100,TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun getOpenExchangeRateApi(moshi: Moshi): OpenExchangeRatesApis {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(moshi),
            )
            .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
            .build()
            .create(OpenExchangeRatesApis::class.java)
    }

    @Provides
    @Singleton
    fun provideFrmDatabse(@ApplicationContext context: Context): CurrencyAppDatabase {
        return Room.databaseBuilder(
            context,
            CurrencyAppDatabase::class.java,
            currencyAppDatabaseName
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideCurrencyAppRepository(
        openExchangeRatesApis: OpenExchangeRatesApis,
        countryDao: CountryDao,
        rateDao: RateDao,
        dispatcherProvider: DispatcherProvider
    ): CurrencyAppRepository {
        return CurrencyAppRepositoryImp(openExchangeRatesApis, countryDao,rateDao,dispatcherProvider)
    }

    @Singleton
    @Provides
    fun provideDispatcher() = object :DispatcherProvider{
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }

}