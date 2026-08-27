package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "delivery_orders")
data class DeliveryOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderCode: String = "",
    val photoPath: String,
    val photoUriString: String,
    val timestamp: Long = System.currentTimeMillis(),
    val formattedDate: String,
    val recipientEmail: String,
    val ccEmail: String = "",
    val subject: String,
    val notes: String = "",
    val isSent: Boolean = true
)
