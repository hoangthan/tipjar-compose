package com.example.tipjar.domain.model

data class TipModel(
    val id: Long,
    val billAmount: Double,
    val tipAmount: Double,
    val imageUrl: String? = null,
    val createAt: Long
)
