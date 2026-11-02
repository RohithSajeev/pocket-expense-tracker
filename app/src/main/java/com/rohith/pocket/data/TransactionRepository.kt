package com.rohith.pocket.data

class TransactionRepository(private val dao: TransactionDao) {

    fun getAll() = dao.getAll()

    fun getBetween(start: Long, end: Long) = dao.getBetween(start, end)

    fun getTotalBetween(start: Long, end: Long) = dao.getTotalBetween(start, end)

    fun getCategoryTotalsBetween(start: Long, end: Long) = dao.getCategoryTotalsBetween(start, end)

    suspend fun save(amount: Double, payee: String, category: Category, timestamp: Long) {
        dao.insert(
            Transaction(
                amount = amount,
                payee = payee,
                category = category,
                timestamp = timestamp
            )
        )
    }
}
