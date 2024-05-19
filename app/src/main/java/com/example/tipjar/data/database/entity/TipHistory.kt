package com.example.tipjar.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tipjar.domain.model.TipModel

@Entity(tableName = "tip_history")
data class TipHistory(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "bill_amount") val billAmount: Double,

    @ColumnInfo(name = "tip_amount") val tipAmount: Double,

    @ColumnInfo(name = "image_url") val imageUrl: String? = null
)

fun TipHistory.toTipModel() = TipModel(
    id = id,
    billAmount = billAmount,
    tipAmount = tipAmount,
    imageUrl = imageUrl,
    createAt = id //Since currently, we are using the timestamp as the id of record
)
