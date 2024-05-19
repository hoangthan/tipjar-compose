package com.example.tipjar.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipjar.R
import com.example.tipjar.ui.theme.Grey
import com.example.tipjar.ui.theme.Orange
import com.example.tipjar.ui.theme.TipJarTypography
import com.example.tipjar.ui.utils.asCurrencyString
import kotlin.math.max


@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    Scaffold(topBar = { HomeTopBar() }) { innerPadding ->
        var showDialog by remember { mutableStateOf(false) }
        var numberOfPeople by remember { mutableIntStateOf(1) }
        var amountString by remember { mutableStateOf("") }
        var tipPercentString by remember { mutableStateOf("10") }

        val totalTip by remember(amountString, tipPercentString, numberOfPeople) {
            val amount = amountString.toDoubleOrNull() ?: 0.0
            val percent = amountString.toDoubleOrNull() ?: 0.0
            val tipAmount = amount * percent / 100
            val costPerPerson = (amount + tipAmount) / max(numberOfPeople, 1)
            mutableStateOf(tipAmount to costPerPerson)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = stringResource(id = R.string.enter_amount))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    onValueChange = { amountString = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    placeholder = {
                        Text(
                            text = "100.00",
                            modifier = Modifier
                                .alpha(.3f)
                                .fillMaxWidth(),
                            style = TipJarTypography.h3.copy(textAlign = TextAlign.Center)
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    value = amountString,
                    textStyle = TipJarTypography.h3.copy(textAlign = TextAlign.Center),
                    leadingIcon = { Text(text = "$", style = TipJarTypography.h5) },
                    trailingIcon = { Box(modifier = Modifier.width(24.dp)) }
                )

                NumberOfPeople(numberOfPeople, modifier = Modifier.padding(vertical = 16.dp)) {
                    numberOfPeople = max(1, it)
                }

                Text(text = stringResource(id = R.string.tip_percentage))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    onValueChange = {
                        tipPercentString = it
                    },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    textStyle = TipJarTypography.h3.copy(textAlign = TextAlign.Center),
                    value = tipPercentString,
                    trailingIcon = { Text(text = "%", style = TipJarTypography.h5) },
                    leadingIcon = { Box(modifier = Modifier.width(24.dp)) }
                )

                CostView(
                    totalTip = totalTip.first,
                    perPerson = totalTip.second,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 16.dp),
                    ) {
                        Checkbox(
                            modifier = Modifier.size(20.dp),
                            checked = showDialog,
                            onCheckedChange = { showDialog = !showDialog },
                            colors = CheckboxDefaults.colors(
                                uncheckedColor = Grey,
                                checkedColor = White,
                                checkmarkColor = Orange,
                            ),
                        )

                        Text(
                            text = stringResource(id = R.string.take_receipt_photo),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(Orange),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(48.dp),
                    ) {
                        Text(text = stringResource(id = R.string.save_payment))
                    }
                }
            }
        }
    }
}

@Composable
fun CostView(
    totalTip: Double,
    perPerson: Double,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(id = R.string.total_tip))
            Text(text = totalTip.asCurrencyString())
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            Text(text = stringResource(id = R.string.person_cost), style = TipJarTypography.h5)
            Text(text = perPerson.asCurrencyString(), style = TipJarTypography.h5)
        }
    }
}

@Composable
fun NumberOfPeople(
    numberOfPeople: Int,
    modifier: Modifier = Modifier,
    onNumberOfPeopleChange: (Int) -> Unit,
) {
    Column(modifier = modifier) {

        Text(text = stringResource(id = R.string.how_many_people))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { onNumberOfPeopleChange(numberOfPeople + 1) }
                    .size(71.dp)
                    .clip(CircleShape)
                    .background(Grey)
                    .padding(1.dp)
                    .clip(CircleShape)
                    .background(White),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "+", color = Orange, fontSize = 42.sp
                )
            }

            Text(
                text = numberOfPeople.toString(),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                style = TipJarTypography.h3
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { onNumberOfPeopleChange(numberOfPeople - 1) }
                    .size(71.dp)
                    .clip(CircleShape)
                    .background(Grey)
                    .padding(1.dp)
                    .clip(CircleShape)
                    .background(White), contentAlignment = Alignment.Center) {
                Text(
                    text = "-", color = Orange, fontSize = 42.sp
                )
            }
        }
    }
}

@Composable
fun HomeTopBar(onHistoryClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_app_logo_landscape),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )

        Image(painter = painterResource(id = R.drawable.ic_history),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable { onHistoryClick() })
    }
}
