package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val categoryId: String,
    val type: String, // "EXPENSE" or "INCOME"
    val amount: Double,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey
    val categoryId: String,
    val monthlyLimit: Double,
    val monthYear: String // e.g. "2026-10"
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val name: String = "Mukesh",
    val email: String = "mukesh@example.com",
    val currencyCode: String = "INR",
    val currencySymbol: String = "₹",
    val notificationsEnabled: Boolean = true,
    val isDarkMode: Boolean = false,
    val onboardingCompleted: Boolean = true
)
