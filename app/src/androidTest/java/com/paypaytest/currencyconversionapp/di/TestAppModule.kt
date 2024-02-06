package com.paypaytest.currencyconversionapp.di

import com.paypaytest.currencyconversionapp.common.DispatcherProvider
import com.paypaytest.currencyconversionapp.data.repository.FakeCurrencyAppRepository
import com.paypaytest.currencyconversionapp.domain.repository.CurrencyAppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class] // Replace the original module with this one for testing
)
object TestAppModule {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Singleton
    @Provides
    fun provideTestDispatcherProvider(): DispatcherProvider {
        // Provide a test implementation of DispatcherProvider
        return object : DispatcherProvider {
            override val io = UnconfinedTestDispatcher()
            override val default = UnconfinedTestDispatcher()
            override val main = UnconfinedTestDispatcher()
            override val unconfined = UnconfinedTestDispatcher()
        }
    }

    @Singleton
    @Provides
    fun provideTestCurrencyAppRepository(): CurrencyAppRepository {
        return FakeCurrencyAppRepository()
    }
}
