package com.example.data.repository

import com.example.data.local.DeliveryOrderDao
import com.example.data.model.DeliveryOrder
import kotlinx.coroutines.flow.Flow

class DeliveryOrderRepository(private val deliveryOrderDao: DeliveryOrderDao) {
    val allOrders: Flow<List<DeliveryOrder>> = deliveryOrderDao.getAllOrders()

    fun getOrderById(id: Long): Flow<DeliveryOrder?> = deliveryOrderDao.getOrderById(id)

    suspend fun insertOrder(order: DeliveryOrder): Long = deliveryOrderDao.insertOrder(order)

    suspend fun deleteOrder(order: DeliveryOrder) = deliveryOrderDao.deleteOrder(order)

    suspend fun clearAll() = deliveryOrderDao.clearAll()
}
