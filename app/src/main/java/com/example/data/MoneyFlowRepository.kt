package com.example.data

import kotlinx.coroutines.flow.Flow

class MoneyFlowRepository(
    private val transactionDao: TransactionDao,
    private val budgetDao: BudgetDao,
    private val userProfileDao: UserProfileDao,
    private val database: MoneyFlowDatabase
) {
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val allBudgets: Flow<List<BudgetEntity>> = budgetDao.getAllBudgets()
    val userProfile: Flow<UserProfileEntity?> = userProfileDao.getUserProfile()

    fun getRecentTransactions(limit: Int): Flow<List<TransactionEntity>> =
        transactionDao.getRecentTransactions(limit)

    suspend fun insertTransaction(transaction: TransactionEntity): Long =
        transactionDao.insertTransaction(transaction)

    suspend fun updateTransaction(transaction: TransactionEntity) =
        transactionDao.updateTransaction(transaction)

    suspend fun deleteTransaction(id: Long) =
        transactionDao.deleteById(id)

    suspend fun saveBudget(budget: BudgetEntity) =
        budgetDao.insertBudget(budget)

    suspend fun saveUserProfile(profile: UserProfileEntity) =
        userProfileDao.saveUserProfile(profile)

    suspend fun resetToDefaultSeed() {
        transactionDao.deleteAll()
        budgetDao.deleteAll()
        MoneyFlowDatabase.seedInitialData(database)
    }

    suspend fun clearAllData() {
        transactionDao.deleteAll()
        budgetDao.deleteAll()
    }
}
