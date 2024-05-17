package com.example.tipjar.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tip_history")
data class TipHistory(
    @PrimaryKey @ColumnInfo(name = "timestamp") val timestamp: Long,

    @ColumnInfo(name = "bill_amount") val billAmount: Double,

    @ColumnInfo(name = "tip_amount") val tipAmount: Double,

    @ColumnInfo(name = "image_url") val imageUrl: String? = null
)
