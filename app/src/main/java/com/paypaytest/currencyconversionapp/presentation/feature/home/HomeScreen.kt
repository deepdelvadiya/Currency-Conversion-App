@file:OptIn(ExperimentalAnimationApi::class)

package com.paypaytest.currencyconversionapp.presentation.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paypaytest.currencyconversionapp.R
import com.paypaytest.currencyconversionapp.domain.model.ConvertedRateModel
import com.paypaytest.currencyconversionapp.domain.model.CountryModel
import com.paypaytest.currencyconversionapp.presentation.feature.home.viewModel.ConvertedRateUiState
import com.paypaytest.currencyconversionapp.presentation.feature.home.viewModel.CountryCodeState
import com.paypaytest.currencyconversionapp.ui.theme.CurrencyConversionAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isSyncing: Boolean,
    countryCodeState: CountryCodeState,
    selectedCountryState: CountryModel,
    onCountryCodeSelected: (CountryModel) -> Unit = {},
    currencyTextFiledValue: String,
    onCurrencyTextFiledValueChange: (String) -> Unit,
    convertedRateUiState: ConvertedRateUiState,
    onConvertClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isCountryCodeExpanded by remember {
        mutableStateOf(false)
    }

    val isCountryCodeIsLoading = countryCodeState is CountryCodeState.Loading

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(2f)
                        .align(Alignment.CenterVertically),
                    value = currencyTextFiledValue,
                    onValueChange = { onCurrencyTextFiledValueChange(it) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = {
                        Text(text = stringResource(id = R.string.from))
                    }
                )
                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    expanded = isCountryCodeExpanded,
                    onExpandedChange = { isCountryCodeExpanded = it }
                ) {
                    TextField(
                        value = selectedCountryState.code,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCountryCodeExpanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = isCountryCodeExpanded,
                        onDismissRequest = { isCountryCodeExpanded = false }) {
                        if (countryCodeState is CountryCodeState.Success) {
                            countryCodeState.countryCodes.forEach { countryModel ->
                                DropdownMenuItem(
                                    text = { Text(text = countryModel.code) },
                                    onClick = {
                                        onCountryCodeSelected(countryModel)
                                        isCountryCodeExpanded = false
                                    })
                            }
                        }
                    }
                }
            }
            AnimatedContent(targetState = convertedRateUiState.isLoading ||isSyncing || isCountryCodeIsLoading, transitionSpec = {
                (fadeIn(tween(500)) + scaleIn()).togetherWith(fadeOut(tween(500)) + scaleOut(spring()))
            }, label = "convertLogInStateAnimation") { isLoading ->
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(onClick = onConvertClicked, modifier = Modifier.fillMaxWidth()) {
                        Text(text = stringResource(id = R.string.convert))
                    }
                }
            }
            ConvertedRateView(convertedList = convertedRateUiState.currenciesList)
        }
    }
}

@Composable
fun ConvertedRateView(
    convertedList: List<ConvertedRateModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(convertedList, key = { it.countryCode }) {
            Card(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {
                    Text(text = it.countryCode)
                    Text(text = it.rates.toString())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DummyCompose() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Row(
            modifier = Modifier
                .clickable {

                }
                .padding(10.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.CenterVertically),
                value = "userInputValue",
                onValueChange = {},
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Call, contentDescription = "callIcon")
                },
                label = {
                    Text(text = "Enter Value")
                }
            )
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                expanded = false,
                onExpandedChange = { }
            ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxHeight()
                )
                ExposedDropdownMenu(expanded = false,
                    onDismissRequest = { }) {
                    DropdownMenuItem(text = { Text(text = "countryModel.code") }, onClick = {
                    })
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    CurrencyConversionAppTheme {
        HomeScreen(
            isSyncing = false,
            countryCodeState = CountryCodeState.Success(emptyList()),
            selectedCountryState = CountryModel("USD"),
            onCurrencyTextFiledValueChange = {},
            currencyTextFiledValue = "",
            convertedRateUiState = ConvertedRateUiState(),
            onConvertClicked = {}
        )
    }
}

@Preview
@Composable
fun DummyComposeDemo() {
    DummyCompose()
}