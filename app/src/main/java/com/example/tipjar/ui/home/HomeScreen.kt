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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipjar.R
import com.example.tipjar.ui.theme.Grey
import com.example.tipjar.ui.theme.Orange
import com.example.tipjar.ui.theme.TipJarTheme
import com.example.tipjar.ui.theme.TipJarTypography
import kotlin.math.max


@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    TipJarTheme {
        Scaffold(topBar = { HomeTopBar() }) { innerPadding ->
            var showDialog by remember { mutableStateOf(false) }
            var numberOfPeople by remember { mutableIntStateOf(0) }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = stringResource(id = R.string.enter_amount))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        onValueChange = {

                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done
                        ),
                        value = ""
                    )

                    NumberOfPeople(numberOfPeople) {
                        numberOfPeople = max(0, it)
                    }

                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(id = R.string.tip_percentage)
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        onValueChange = {

                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done
                        ),
                        value = ""
                    )

                    CostView(
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
                                .height(48.dp),
                        ) {
                            Text(
                                text = stringResource(id = R.string.save_payment),
                                color = White,
                                style = TipJarTypography.subtitle1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CostView(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(id = R.string.total_tip))
            Text(text = stringResource(id = R.string.total_tip))
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            Text(text = stringResource(id = R.string.person_cost))
            Text(text = stringResource(id = R.string.person_cost))
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
                    .wrapContentWidth(Alignment.CenterHorizontally)
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
