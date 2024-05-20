package com.example.tipjar.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.tipjar.R
import com.example.tipjar.ui.history.HistoryViewModel.HistoryViewState
import com.example.tipjar.ui.history.HistoryViewModel.ViewEvent
import com.example.tipjar.ui.history.model.TipUiModel
import com.example.tipjar.ui.theme.TipJarTypography

@Composable
fun HistoryScreen(
    viewState: HistoryViewState,
    onEventSent: (ViewEvent) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
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
                        .clickable { onBackPressed() },
                    painter = painterResource(id = R.drawable.abc_vector_test),
                    contentDescription = "ImageBack",
                    colorFilter = ColorFilter.tint(Color.Black),
                )
            }

            Divider()
        }
    }) { innerPadding ->
        var showBillDetails by remember { mutableStateOf<TipUiModel?>(null) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn {
                items(viewState.payments.size) { index ->
                    TipViewItem(
                        tipModel = viewState.payments[index],
                        onItemClicked = { showBillDetails = viewState.payments[index] }
                    )
                }
            }

            if (viewState.payments.isEmpty()) {
                Text(
                    style = TipJarTypography.body2,
                    text = stringResource(id = R.string.no_saved_payment),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(0.7f),
                )
            }
        }

        showBillDetails?.let {
            BillDetailDialog(
                tipModel = it,
                onDeleteClick = {
                    showBillDetails = null
                    onEventSent(ViewEvent.DeleteTipRecord(it.id))
                },
                onDismissRequest = {
                    showBillDetails = null
                }
            )
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
                .height(53.dp)
        ) {
            Text(
                text = tipModel.createAt,
                modifier = Modifier.align(Alignment.TopStart),
                style = TipJarTypography.body1
            )

            Row(
                modifier = Modifier.align(Alignment.BottomStart),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "$${tipModel.billAmount}",
                    style = TipJarTypography.h5,
                    textAlign = TextAlign.End,
                    modifier = Modifier.alignByBaseline()
                )

                Text(
                    text = "Tip: $${tipModel.tipAmount}",
                    modifier = Modifier
                        .padding(start = 36.dp)
                        .alpha(.6f)
                        .alignByBaseline(),
                    style = TipJarTypography.body1,
                )
            }
        }

        if (tipModel.imageUrl.isNullOrEmpty().not()) {
            Image(
                painter = rememberImagePainter(tipModel.imageUrl),
                contentDescription = "ImageTipThumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(53.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Preview
@Composable
fun TipViewItemPreview() {
    HistoryScreen(
        viewState = HistoryViewState(
            listOf()
        )
    )
}
