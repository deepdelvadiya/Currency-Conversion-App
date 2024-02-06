package com.paypaytest.currencyconversionapp.presentation.feature.home.viewModel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paypaytest.currencyconversionapp.common.result.Result
import com.paypaytest.currencyconversionapp.data.utils.SyncManager
import com.paypaytest.currencyconversionapp.domain.model.ConvertedRateModel
import com.paypaytest.currencyconversionapp.domain.model.CountryModel
import com.paypaytest.currencyconversionapp.domain.repository.CurrencyAppRepository
import com.paypaytest.currencyconversionapp.domain.usecase.ConvertToCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currencyAppRepository: CurrencyAppRepository,
    private val syncManager: SyncManager,
    private val convertToCurrenciesUseCase: ConvertToCurrenciesUseCase
):ViewModel() {

    var selectedCountry by mutableStateOf<CountryModel>(CountryModel("USD"))

    var amount by mutableStateOf("1")

    private val _convertedRateUiState = MutableStateFlow<ConvertedRateUiState>(
        ConvertedRateUiState()
    )
    val convertedRatesState = _convertedRateUiState.asStateFlow()

    val isSyncing = syncManager.isSyncing.stateIn(
        scope = viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val countryCodeState =
        currencyAppRepository.getCurrencies().map(CountryCodeState::Success).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000), initialValue = CountryCodeState.Loading
        )

    fun convertToOtherCurrencies(){
        viewModelScope.launch {
            convertToCurrenciesUseCase(selectedCountry,amount).collectLatest {result ->
                when(result){
                    is Result.Error -> {
                        _convertedRateUiState.update {
                            ConvertedRateUiState(errorMassage = result.exception)
                        }
                    }
                    Result.Loading -> {
                        _convertedRateUiState.update {
                            ConvertedRateUiState(isLoading = true)
                        }
                    }
                    is Result.Success -> {
                        _convertedRateUiState.update {
                            ConvertedRateUiState(currenciesList = result.data)
                        }
                    }
                }

            }
        }
    }

}

@Immutable
data class ConvertedRateUiState(
    val currenciesList:List<ConvertedRateModel> = emptyList(),
    val isLoading:Boolean = false,
    val errorMassage:String? = null
)

sealed class CountryCodeState{
    data object Loading:CountryCodeState()

    data class Success(
        val countryCodes:List<CountryModel>
    ):CountryCodeState()
}
