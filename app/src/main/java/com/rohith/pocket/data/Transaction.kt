package com.rohith.pocket.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Category(val label: String) {
    FOOD("Food"),
    GROCERIES("Groceries"),
    PETROL("Petrol"),
    TRAVEL("Travel"),
    SHOPPING("Shopping"),
    OTHERS("Others")
}

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val payee: String,
    val category: Category,
    val timestamp: Long,      // epoch millis
    val sourceApp: String = "Google Pay"
)
