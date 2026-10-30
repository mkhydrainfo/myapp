package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [TransactionEntity::class, BudgetEntity::class, UserProfileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MoneyFlowDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: MoneyFlowDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MoneyFlowDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoneyFlowDatabase::class.java,
                    "moneyflow_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        seedInitialData(database)
                    }
                }
            }
        }

        suspend fun seedInitialData(database: MoneyFlowDatabase) {
            val now = System.currentTimeMillis()
            val dayMs = 24L * 60 * 60 * 1000

            // Initial User Profile
            database.userProfileDao().saveUserProfile(
                UserProfileEntity(
                    id = 1,
                    name = "Mukesh",
                    email = "mukesh@example.com",
                    currencyCode = "INR",
                    currencySymbol = "₹",
                    notificationsEnabled = true,
                    isDarkMode = false,
                    onboardingCompleted = true
                )
            )

            // Initial Budgets matching showcase screenshot
            val budgets = listOf(
                BudgetEntity("food_dining", 6000.0, "2026-10"),
                BudgetEntity("transport", 3000.0, "2026-10"),
                BudgetEntity("bills", 5000.0, "2026-10"),
                BudgetEntity("shopping", 4000.0, "2026-10"),
                BudgetEntity("health", 2000.0, "2026-10"),
                BudgetEntity("entertainment", 3000.0, "2026-10")
            )
            database.budgetDao().insertAll(budgets)

            // Initial Transactions matching showcase screenshot
            // Total Income = 65,000 (Freelance 8,000 + Salary 57,000)
            // Total Expenses = 16,750
            // Total Balance = 48,250.00
            val transactions = listOf(
                TransactionEntity(
                    title = "Groceries",
                    categoryId = "food_dining",
                    type = "EXPENSE",
                    amount = 1200.0,
                    note = "Weekly vegetables & staples",
                    timestamp = now - (2 * 60 * 60 * 1000) // Today
                ),
                TransactionEntity(
                    title = "Freelance Payment",
                    categoryId = "work",
                    type = "INCOME",
                    amount = 8000.0,
                    note = "UI Design project milestone",
                    timestamp = now - (1 * dayMs) // Yesterday
                ),
                TransactionEntity(
                    title = "Electricity Bill",
                    categoryId = "bills",
                    type = "EXPENSE",
                    amount = 950.0,
                    note = "Monthly power consumption",
                    timestamp = now - (2 * dayMs)
                ),
                TransactionEntity(
                    title = "Transport",
                    categoryId = "transport",
                    type = "EXPENSE",
                    amount = 450.0,
                    note = "Metro card recharge & cab",
                    timestamp = now - (3 * dayMs)
                ),
                TransactionEntity(
                    title = "Mobile Recharge",
                    categoryId = "bills",
                    type = "EXPENSE",
                    amount = 299.0,
                    note = "Unlimited data plan",
                    timestamp = now - (4 * dayMs)
                ),
                TransactionEntity(
                    title = "Online Shopping",
                    categoryId = "shopping",
                    type = "EXPENSE",
                    amount = 1750.0,
                    note = "Noise-cancelling earphones",
                    timestamp = now - (5 * dayMs)
                ),
                TransactionEntity(
                    title = "Monthly Salary",
                    categoryId = "work",
                    type = "INCOME",
                    amount = 57000.0,
                    note = "Full-time tech lead payout",
                    timestamp = now - (8 * dayMs)
                ),
                TransactionEntity(
                    title = "Restaurant & Dining",
                    categoryId = "food_dining",
                    type = "EXPENSE",
                    amount = 3000.0,
                    note = "Dinner with friends",
                    timestamp = now - (10 * dayMs)
                ),
                TransactionEntity(
                    title = "Fuel & Commute",
                    categoryId = "transport",
                    type = "EXPENSE",
                    amount = 1350.0,
                    note = "Petrol refill",
                    timestamp = now - (12 * dayMs)
                ),
                TransactionEntity(
                    title = "Wifi Broadband",
                    categoryId = "bills",
                    type = "EXPENSE",
                    amount = 851.0,
                    note = "Fibre internet renewal",
                    timestamp = now - (14 * dayMs)
                ),
                TransactionEntity(
                    title = "Clothing & Accessories",
                    categoryId = "shopping",
                    type = "EXPENSE",
                    amount = 2250.0,
                    note = "Autumn shirts",
                    timestamp = now - (17 * dayMs)
                ),
                TransactionEntity(
                    title = "Pharmacy & Vitamins",
                    categoryId = "health",
                    type = "EXPENSE",
                    amount = 600.0,
                    note = "Health supplements",
                    timestamp = now - (19 * dayMs)
                ),
                TransactionEntity(
                    title = "Cinema & Streaming",
                    categoryId = "entertainment",
                    type = "EXPENSE",
                    amount = 2100.0,
                    note = "Weekend movie and subscriptions",
                    timestamp = now - (22 * dayMs)
                ),
                TransactionEntity(
                    title = "Book & Tutorial",
                    categoryId = "education",
                    type = "EXPENSE",
                    amount = 2000.0,
                    note = "Kotlin Architecture book",
                    timestamp = now - (25 * dayMs)
                )
            )

            database.transactionDao().insertAll(transactions)
        }
    }
}
