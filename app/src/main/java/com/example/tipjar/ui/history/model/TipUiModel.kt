package com.example.tipjar.ui.history.model

import com.example.tipjar.domain.model.TipModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TipUiModel(
    val id: Long,
    val createAt: String,
    val billAmount: String,
    val tipAmount: String,
    val imageUrl: String?,
)

fun TipModel.toUiModel(): TipUiModel {
    return TipUiModel(
        id = id,
        createAt = id.toDateFormat(),
        billAmount = billAmount.toString(),
        tipAmount = tipAmount.toString(),
        imageUrl = imageUrl
    )
}

private fun Long.toDateFormat(): String {
    val sdf = SimpleDateFormat("yyyy MMMM dd", Locale.getDefault())
    val date = Date(this)
    return sdf.format(date)
}
