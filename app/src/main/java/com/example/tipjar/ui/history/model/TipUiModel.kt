package com.example.tipjar.ui.history.model

data class TipUiModel(
    val id: Long,
    val createAt: String,
    val billAmount: String,
    val tipAmount: String,
    val imageUrl: String,
)
