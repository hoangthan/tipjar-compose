package com.example.tipjar.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberImagePainter
import com.example.tipjar.R
import com.example.tipjar.ui.history.model.TipUiModel
import com.example.tipjar.ui.theme.TipJarTypography

@Composable
fun BillDetailDialog(
    tipModel: TipUiModel,
    onDeleteClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (tipModel.imageUrl.isNullOrEmpty().not()) {
                Image(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    painter = rememberImagePainter(tipModel.imageUrl),
                    contentDescription = "BillImage",
                )
            } else {
                Text(
                    style = TipJarTypography.body2,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .alpha(.5f),
                    text = stringResource(id = R.string.this_recod_have_no_image)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp),
            ) {
                Text(
                    text = tipModel.createAt,
                    style = TipJarTypography.body1,
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
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

            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .clickable { onDeleteClick() }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_trash),
                    contentDescription = "ImageDelete",
                    modifier = Modifier
                        .padding(16.dp)
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Preview
@Composable
fun BillDetailViewPreview() {
    BillDetailDialog(TipUiModel(1, "2021 January 14", "105.23", "20.52", ""))
}
