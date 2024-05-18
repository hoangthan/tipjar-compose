package com.example.tipjar.domain.model

data class TipModel(
    val id: Long = System.currentTimeMillis(),
    val billAmount: Double,
    val tipAmount: Double,
    val imageUrl: String? = null,
)
