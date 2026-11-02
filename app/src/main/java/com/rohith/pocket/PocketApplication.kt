package com.rohith.pocket

import android.app.Application
import com.rohith.pocket.data.AppDatabase
import com.rohith.pocket.data.TransactionRepository

class PocketApplication : Application() {
    val repository: TransactionRepository by lazy {
        TransactionRepository(AppDatabase.getInstance(this).transactionDao())
    }
}
