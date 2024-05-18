package com.example.tipjar.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipjar.R
import com.example.tipjar.ui.history.model.TipUiModel
import com.example.tipjar.ui.theme.TipJarTheme
import com.example.tipjar.ui.theme.TipJarTypography

@Preview
@Composable
fun HistoryScreen() {
    TipJarTheme {
        Scaffold(topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.saved_payments),
                        modifier = Modifier.align(Alignment.Center),
                        style = TipJarTypography.h6,
                    )

                    Image(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clickable { },
                        painter = painterResource(id = R.drawable.abc_vector_test),
                        contentDescription = "ImageBack",
                        colorFilter = ColorFilter.tint(Color.Black),
                    )
                }

                Divider()
            }
        }) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                LazyColumn {
                    items(20) {
                        TipViewItem(
                            TipUiModel(
                                id = 0L,
                                createAt = "createAt",
                                billAmount = "billAmount",
                                tipAmount = "tipAmount",
                                imageUrl = ""
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TipViewItem(
    tipModel: TipUiModel,
    modifier: Modifier = Modifier,
    onItemClicked: (TipUiModel) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .clickable { onItemClicked(tipModel) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Text(text = tipModel.createAt, modifier = Modifier.align(Alignment.TopStart))

            Row(modifier = Modifier.align(Alignment.BottomStart)) {
                Text(text = tipModel.billAmount)
                Text(text = tipModel.tipAmount, modifier = Modifier.padding(start = 24.dp))
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ic_history),
            contentDescription = "ImageTipThumbnail",
            modifier = Modifier
                .size(53.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}
