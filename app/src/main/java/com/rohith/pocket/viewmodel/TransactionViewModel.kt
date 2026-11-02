package com.rohith.pocket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rohith.pocket.data.Category
import com.rohith.pocket.data.CategoryTotal
import com.rohith.pocket.data.Transaction
import com.rohith.pocket.data.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val monthRange = currentMonthRange()

    val transactions: StateFlow<List<Transaction>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val monthlyTotal: StateFlow<Double> = repository.getTotalBetween(monthRange.first, monthRange.second)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val categoryTotals: StateFlow<List<CategoryTotal>> =
        repository.getCategoryTotalsBetween(monthRange.first, monthRange.second)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun save(amount: Double, payee: String, category: Category, timestamp: Long) {
        viewModelScope.launch {
            repository.save(amount, payee, category, timestamp)
        }
    }

    private fun currentMonthRange(): Pair<Long, Long> {
        val start = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0)
        }
        val end = (start.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
        return start.timeInMillis to end.timeInMillis
    }

    class Factory(private val repository: TransactionRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(repository) as T
        }
    }
}
