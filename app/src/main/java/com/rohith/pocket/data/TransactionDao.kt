package com.rohith.pocket.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class CategoryTotal(val category: Category, val total: Double)

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(transaction: Transaction): Long

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC")
    fun getBetween(start: Long, end: Long): Flow<List<Transaction>>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE timestamp BETWEEN :start AND :end")
    fun getTotalBetween(start: Long, end: Long): Flow<Double>

    @Query("SELECT category, COALESCE(SUM(amount), 0) as total FROM transactions WHERE timestamp BETWEEN :start AND :end GROUP BY category")
    fun getCategoryTotalsBetween(start: Long, end: Long): Flow<List<CategoryTotal>>

    @Query("SELECT * FROM transactions WHERE category = :category ORDER BY timestamp DESC")
    fun getByCategory(category: Category): Flow<List<Transaction>>
}
