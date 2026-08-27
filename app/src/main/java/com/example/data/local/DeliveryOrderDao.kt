package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.DeliveryOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface DeliveryOrderDao {
    @Query("SELECT * FROM delivery_orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<DeliveryOrder>>

    @Query("SELECT * FROM delivery_orders WHERE id = :id LIMIT 1")
    fun getOrderById(id: Long): Flow<DeliveryOrder?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: DeliveryOrder): Long

    @Delete
    suspend fun deleteOrder(order: DeliveryOrder)

    @Query("DELETE FROM delivery_orders")
    suspend fun clearAll()
}
