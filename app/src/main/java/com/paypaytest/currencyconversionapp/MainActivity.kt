package com.paypaytest.currencyconversionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.paypaytest.currencyconversionapp.presentation.feature.home.HomeScreen
import com.paypaytest.currencyconversionapp.presentation.feature.home.viewModel.HomeViewModel
import com.paypaytest.currencyconversionapp.ui.theme.CurrencyConversionAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel: HomeViewModel by viewModels()
        setContent {
            val countryCodeState by mainViewModel.countryCodeState.collectAsState()
            val isSyncingState by mainViewModel.isSyncing.collectAsState()
            val convertedRateUiState by mainViewModel.convertedRatesState.collectAsState()
            CurrencyConversionAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.tertiaryContainer
                ) {
                    HomeScreen(
                        isSyncing = isSyncingState,
                        countryCodeState = countryCodeState,
                        mainViewModel.selectedCountry,
                        onCountryCodeSelected = { mainViewModel.selectedCountry = it },
                        onCurrencyTextFiledValueChange = {mainViewModel.amount = it},
                        currencyTextFiledValue = mainViewModel.amount,
                        convertedRateUiState = convertedRateUiState,
                        onConvertClicked = { mainViewModel.convertToOtherCurrencies() })
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CurrencyConversionAppTheme {
        Greeting("Android")
    }
}