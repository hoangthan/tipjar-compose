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
import com.example.tipjar.ui.home.HomeViewModel.HomeViewEvent
import com.example.tipjar.ui.home.HomeViewModel.HomeViewState
import com.example.tipjar.ui.theme.Grey
import com.example.tipjar.ui.theme.Orange
import com.example.tipjar.ui.theme.TipJarTypography


@Composable
fun HomeScreen(
    viewState: HomeViewState,
    dispatchEvent: (HomeViewEvent) -> Unit = {},
) {
    Scaffold(topBar = { HomeTopBar() }) { innerPadding ->
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
                    onValueChange = { dispatchEvent(HomeViewEvent.UpdateAmount(it)) },
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
                    value = viewState.billAmount,
                    textStyle = TipJarTypography.h3.copy(textAlign = TextAlign.Center),
                    leadingIcon = { Text(text = "$", style = TipJarTypography.h5) },
                    trailingIcon = { Box(modifier = Modifier.width(24.dp)) }
                )

                NumberOfPeople(
                    viewState.numberOfPeople,
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    dispatchEvent(HomeViewEvent.UpdateNumberOfPeople(it))
                }

                Text(text = stringResource(id = R.string.tip_percentage))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    onValueChange = { dispatchEvent(HomeViewEvent.UpdateTipPercent(it)) },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    textStyle = TipJarTypography.h3.copy(textAlign = TextAlign.Center),
                    value = viewState.tipPercent,
                    trailingIcon = { Text(text = "%", style = TipJarTypography.h5) },
                    leadingIcon = { Box(modifier = Modifier.width(24.dp)) }
                )

                CostView(
                    totalTip = viewState.totalTip,
                    perPerson = viewState.perPerson,
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { dispatchEvent(HomeViewEvent.UpdateTakePhoto) }
                            .padding(vertical = 16.dp),
                    ) {
                        Checkbox(
                            modifier = Modifier.size(20.dp),
                            checked = viewState.takePhotoReceipt,
                            onCheckedChange = { dispatchEvent(HomeViewEvent.UpdateTakePhoto) },
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
                        onClick = { dispatchEvent(HomeViewEvent.OnSavePaymentClicked) },
                        colors = ButtonDefaults.buttonColors(Orange),
                        shape = RoundedCornerShape(16.dp),
                        enabled = viewState.enableSave,
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
    totalTip: String,
    perPerson: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(id = R.string.total_tip))
            Text(text = totalTip)
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            Text(text = stringResource(id = R.string.person_cost), style = TipJarTypography.h5)
            Text(text = perPerson, style = TipJarTypography.h5)
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
                Text(text = "+", color = Orange, fontSize = 42.sp)
            }

            Text(
                text = numberOfPeople.toString(),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                style = TipJarTypography.h3
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { onNumberOfPeopleChange(numberOfPeople - 1) }
                    .size(71.dp)
                    .clip(CircleShape)
                    .background(Grey)
                    .padding(1.dp)
                    .clip(CircleShape)
                    .background(White),
            ) {
                Text(text = "-", color = Orange, fontSize = 42.sp)
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

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(HomeViewState()) {}
}
