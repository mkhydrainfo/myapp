package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class AppNavTab {
    HOME,
    TRANSACTIONS,
    BUDGETS,
    PROFILE
}

enum class AnalyticsTab {
    SPENDING,
    INCOME,
    SAVINGS
}

data class CategorySpendItem(
    val category: CategoryInfo,
    val amount: Double,
    val percentage: Float
)

data class BudgetStatusItem(
    val category: CategoryInfo,
    val spent: Double,
    val limit: Double,
    val percentage: Float
)

data class TrendBarItem(
    val label: String,
    val amount: Double,
    val heightRatio: Float
)

class MoneyFlowViewModel(application: Application) : AndroidViewModel(application) {

    private val database = MoneyFlowDatabase.getDatabase(application, viewModelScope)
    private val repository = MoneyFlowRepository(
        database.transactionDao(),
        database.budgetDao(),
        database.userProfileDao(),
        database
    )

    // Navigation and Sheets
    val currentTab = MutableStateFlow(AppNavTab.HOME)
    val showAddSheet = MutableStateFlow(false)
    val editingTransaction = MutableStateFlow<TransactionEntity?>(null)
    val isBalanceVisible = MutableStateFlow(true)
    val showSplash = MutableStateFlow(true)
    val showOnboarding = MutableStateFlow(false)

    // User Profile
    val userProfile: StateFlow<UserProfileEntity> = repository.userProfile
        .map { it ?: UserProfileEntity() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UserProfileEntity()
        )

    // Transactions
    val allTransactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Budgets
    val allBudgets: StateFlow<List<BudgetEntity>> = repository.allBudgets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filter & Search states
    val searchQuery = MutableStateFlow("")
    val transactionFilter = MutableStateFlow("ALL") // "ALL", "INCOME", "EXPENSE"
    val selectedCategoryFilter = MutableStateFlow<String?>(null)

    // Analytics state
    val analyticsTab = MutableStateFlow(AnalyticsTab.SPENDING)
    val currentMonthLabel = MutableStateFlow("October 2026")

    // Filtered Transactions
    val filteredTransactions = combine(
        allTransactions,
        searchQuery,
        transactionFilter,
        selectedCategoryFilter
    ) { list, query, filter, catFilter ->
        list.filter { item ->
            val matchesQuery = query.isBlank() ||
                    item.title.contains(query, ignoreCase = true) ||
                    item.note.contains(query, ignoreCase = true) ||
                    CategoryRegistry.getCategory(item.categoryId).name.contains(query, ignoreCase = true)

            val matchesType = when (filter) {
                "INCOME" -> item.type == "INCOME"
                "EXPENSE" -> item.type == "EXPENSE"
                else -> true
            }

            val matchesCategory = catFilter == null || item.categoryId.equals(catFilter, ignoreCase = true)

            matchesQuery && matchesType && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Totals
    val totalIncome: StateFlow<Double> = allTransactions.map { list ->
        list.filter { it.type == "INCOME" }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 65000.0)

    val totalExpense: StateFlow<Double> = allTransactions.map { list ->
        list.filter { it.type == "EXPENSE" }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 16750.0)

    val totalBalance: StateFlow<Double> = combine(totalIncome, totalExpense) { inc, exp ->
        inc - exp
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 48250.0)

    // Category Breakdown for Analytics
    val categorySpendList: StateFlow<List<CategorySpendItem>> = allTransactions.map { list ->
        val expenses = list.filter { it.type == "EXPENSE" }
        val total = expenses.sumOf { it.amount }.coerceAtLeast(1.0)
        val grouped = expenses.groupBy { it.categoryId }

        val items = grouped.map { (catId, txs) ->
            val sum = txs.sumOf { it.amount }
            CategorySpendItem(
                category = CategoryRegistry.getCategory(catId),
                amount = sum,
                percentage = ((sum / total) * 100).toFloat()
            )
        }.sortedByDescending { it.amount }

        if (items.isEmpty()) {
            listOf(
                CategorySpendItem(CategoryRegistry.FOOD_DINING, 5862.5, 35f),
                CategorySpendItem(CategoryRegistry.TRANSPORT, 3015.0, 18f),
                CategorySpendItem(CategoryRegistry.BILLS, 2512.5, 15f),
                CategorySpendItem(CategoryRegistry.SHOPPING, 2010.0, 12f),
                CategorySpendItem(CategoryRegistry.HEALTH, 1340.0, 8f),
                CategorySpendItem(CategoryRegistry.OTHER, 2010.0, 12f)
            )
        } else {
            items
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Budget Status List
    val budgetStatusList: StateFlow<List<BudgetStatusItem>> = combine(allBudgets, allTransactions) { budgets, txs ->
        val expensesByCat = txs.filter { it.type == "EXPENSE" }.groupBy { it.categoryId }
        
        budgets.map { budget ->
            val spent = expensesByCat[budget.categoryId]?.sumOf { it.amount } ?: 0.0
            val pct = if (budget.monthlyLimit > 0) ((spent / budget.monthlyLimit) * 100).toFloat() else 0f
            BudgetStatusItem(
                category = CategoryRegistry.getCategory(budget.categoryId),
                spent = spent,
                limit = budget.monthlyLimit,
                percentage = pct
            )
        }.sortedByDescending { it.percentage }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Spending Trend Bars (e.g. 1 Oct, 8 Oct, 15 Oct, 22 Oct, 29 Oct)
    val spendingTrends: StateFlow<List<TrendBarItem>> = allTransactions.map { txs ->
        val expenses = txs.filter { it.type == "EXPENSE" }
        val maxAmount = 12000.0

        listOf(
            TrendBarItem("1 Oct", 3200.0, (3200f / maxAmount.toFloat()).coerceIn(0.1f, 1f)),
            TrendBarItem("8 Oct", 5400.0, (5400f / maxAmount.toFloat()).coerceIn(0.1f, 1f)),
            TrendBarItem("15 Oct", 8200.0, (8200f / maxAmount.toFloat()).coerceIn(0.1f, 1f)),
            TrendBarItem("22 Oct", 4100.0, (4100f / maxAmount.toFloat()).coerceIn(0.1f, 1f)),
            TrendBarItem("29 Oct", 9650.0, (9650f / maxAmount.toFloat()).coerceIn(0.1f, 1f))
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleBalanceVisibility() {
        isBalanceVisible.value = !isBalanceVisible.value
    }

    fun setTab(tab: AppNavTab) {
        currentTab.value = tab
    }

    fun openAddSheet(transactionToEdit: TransactionEntity? = null) {
        editingTransaction.value = transactionToEdit
        showAddSheet.value = true
    }

    fun closeAddSheet() {
        editingTransaction.value = null
        showAddSheet.value = false
    }

    fun completeSplash() {
        showSplash.value = false
        // Check if onboarding completed
        viewModelScope.launch {
            val profile = repository.userProfile.firstOrNull()
            if (profile?.onboardingCompleted == false) {
                showOnboarding.value = true
            }
        }
    }

    fun completeOnboarding() {
        showOnboarding.value = false
        viewModelScope.launch {
            val current = userProfile.value
            repository.saveUserProfile(current.copy(onboardingCompleted = true))
        }
    }

    fun restartOnboarding() {
        showOnboarding.value = true
    }

    fun saveTransaction(
        title: String,
        amount: Double,
        type: TransactionType,
        categoryId: String,
        note: String,
        timestamp: Long = System.currentTimeMillis()
    ) {
        viewModelScope.launch {
            val editing = editingTransaction.value
            if (editing != null) {
                repository.updateTransaction(
                    editing.copy(
                        title = title,
                        amount = amount,
                        type = type.name,
                        categoryId = categoryId,
                        note = note,
                        timestamp = timestamp
                    )
                )
            } else {
                repository.insertTransaction(
                    TransactionEntity(
                        title = title,
                        amount = amount,
                        type = type.name,
                        categoryId = categoryId,
                        note = note,
                        timestamp = timestamp
                    )
                )
            }
            closeAddSheet()
        }
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            repository.deleteTransaction(id)
        }
    }

    fun saveBudget(categoryId: String, limit: Double) {
        viewModelScope.launch {
            repository.saveBudget(
                BudgetEntity(
                    categoryId = categoryId,
                    monthlyLimit = limit,
                    monthYear = "2026-10"
                )
            )
        }
    }

    fun updateCurrency(currencyItem: CurrencyItem) {
        viewModelScope.launch {
            val current = userProfile.value
            repository.saveUserProfile(
                current.copy(
                    currencyCode = currencyItem.code,
                    currencySymbol = currencyItem.symbol
                )
            )
        }
    }

    fun updateProfile(name: String, email: String) {
        viewModelScope.launch {
            val current = userProfile.value
            repository.saveUserProfile(current.copy(name = name, email = email))
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            val current = userProfile.value
            repository.saveUserProfile(current.copy(isDarkMode = enabled))
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            val current = userProfile.value
            repository.saveUserProfile(current.copy(notificationsEnabled = enabled))
        }
    }

    fun resetData() {
        viewModelScope.launch {
            repository.resetToDefaultSeed()
        }
    }

    fun formatCurrency(amount: Double): String {
        val symbol = userProfile.value.currencySymbol
        val formattedNumber = String.format(Locale.getDefault(), "%,.2f", amount)
        return "$symbol$formattedNumber"
    }

    fun formatRelativeDate(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diffDays = ((now - timestamp) / (24 * 60 * 60 * 1000)).toInt()

        return when {
            diffDays <= 0 -> "Today"
            diffDays == 1 -> "Yesterday"
            diffDays in 2..6 -> "$diffDays days ago"
            else -> {
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                sdf.format(Date(timestamp))
            }
        }
    }
}
