package com.paypaytest.currencyconversionapp.data.presenation.feature.home.viewmodel

import androidx.lifecycle.Observer
import com.paypaytest.currencyconversionapp.common.result.Result
import com.paypaytest.currencyconversionapp.data.entitys.CodeEntity
import com.paypaytest.currencyconversionapp.data.repository.FakeCurrencyAppRepository
import com.paypaytest.currencyconversionapp.data.utils.SyncManager
import com.paypaytest.currencyconversionapp.domain.model.ConvertedRateModel
import com.paypaytest.currencyconversionapp.domain.model.CountryModel
import com.paypaytest.currencyconversionapp.domain.repository.CurrencyAppRepository
import com.paypaytest.currencyconversionapp.domain.usecase.ConvertToCurrenciesUseCase
import com.paypaytest.currencyconversionapp.presentation.feature.home.viewModel.ConvertedRateUiState
import com.paypaytest.currencyconversionapp.presentation.feature.home.viewModel.CountryCodeState
import com.paypaytest.currencyconversionapp.presentation.feature.home.viewModel.HomeViewModel
import com.paypaytest.currencyconversionapp.util.MainDispatcherRule
import com.paypaytest.currencyconversionapp.util.TestSyncManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    // Set the main coroutine dispatcher for testing
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    // Mock dependencies
    private lateinit var currencyAppRepository: CurrencyAppRepository
    private lateinit var convertToCurrenciesUseCase: ConvertToCurrenciesUseCase
    private lateinit var syncManager:TestSyncManager

    // Subject under test
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        currencyAppRepository = FakeCurrencyAppRepository()
        convertToCurrenciesUseCase = ConvertToCurrenciesUseCase(currencyAppRepository)
        syncManager = TestSyncManager()

        viewModel = HomeViewModel(
            currencyAppRepository = currencyAppRepository,
            convertToCurrenciesUseCase = convertToCurrenciesUseCase,
            syncManager = syncManager
        )
    }

    @Test
    fun stateIsLoadingWhenAppIsSyncing() = runTest {
        syncManager.setSyncing(true)

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.isSyncing.collect() }

        assertEquals(
            true,
            viewModel.isSyncing.value,
        )

        collectJob.cancel()
    }

    @Test
    fun `countryCodeState inital and update state on success of fatch`() = runTest {
        assert(viewModel.countryCodeState.value is CountryCodeState.Loading)

        val collectJob =
            launch(UnconfinedTestDispatcher()) {
                viewModel.countryCodeState.collect()
            }

        assert(viewModel.countryCodeState.value is CountryCodeState.Success)

        collectJob.cancel()
    }

    @Test
    fun `convertToOtherCurrencies updates convertedRatesState on success`()= runTest {
        // Given
        val selectedCountry = CountryModel("USD")
        val amount = "1.0"
        val fakeConvertedRates = listOf(
            ConvertedRateModel("USD", 1.0),
            ConvertedRateModel("EUR", 0.916721)
            // Add more fake rates as needed
        )


        // Set the selected country and amount
        viewModel.selectedCountry = selectedCountry
        viewModel.amount = amount


        // Create an observer for the convertedRatesState
        val collectJob =
            launch(UnconfinedTestDispatcher()) {
            viewModel.convertedRatesState.collect()
        }

        //assert initial state of convertedRateState
        assertEquals(
            ConvertedRateUiState(),
            viewModel.convertedRatesState.value
        )

        collectJob.cancel()

        // call convert currencies
        viewModel.convertToOtherCurrencies()


        assert(!viewModel.convertedRatesState.value.isLoading)
        assert(viewModel.convertedRatesState.value.errorMassage == null)
        assert(viewModel.convertedRatesState.value.currenciesList == fakeConvertedRates)
    }

    // Add more test cases for different scenarios, e.g., loading, error, etc.
}
