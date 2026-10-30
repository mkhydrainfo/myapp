package com.example.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.*

enum class TransactionType {
    EXPENSE,
    INCOME
}

data class CategoryInfo(
    val id: String,
    val name: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color,
    val bgLightColor: Color
)

object CategoryRegistry {
    val FOOD_DINING = CategoryInfo(
        id = "food_dining",
        name = "Food & Dining",
        subtitle = "Groceries & Food",
        icon = Icons.Default.Fastfood,
        color = CategoryFood,
        bgLightColor = CategoryFoodBg
    )
    val TRANSPORT = CategoryInfo(
        id = "transport",
        name = "Transport",
        subtitle = "Travel & Commute",
        icon = Icons.Default.DirectionsBus,
        color = CategoryTransport,
        bgLightColor = CategoryTransportBg
    )
    val SHOPPING = CategoryInfo(
        id = "shopping",
        name = "Shopping",
        subtitle = "Retail & Online",
        icon = Icons.Default.LocalMall,
        color = CategoryShopping,
        bgLightColor = CategoryShoppingBg
    )
    val BILLS = CategoryInfo(
        id = "bills",
        name = "Bills",
        subtitle = "Utilities & Power",
        icon = Icons.Default.ElectricBolt,
        color = CategoryBills,
        bgLightColor = CategoryBillsBg
    )
    val HEALTH = CategoryInfo(
        id = "health",
        name = "Health",
        subtitle = "Medical & Care",
        icon = Icons.Default.Favorite,
        color = CategoryHealth,
        bgLightColor = CategoryHealthBg
    )
    val ENTERTAINMENT = CategoryInfo(
        id = "entertainment",
        name = "Entertainment",
        subtitle = "Movies & Games",
        icon = Icons.Default.SportsEsports,
        color = CategoryEntertainment,
        bgLightColor = CategoryEntertainmentBg
    )
    val EDUCATION = CategoryInfo(
        id = "education",
        name = "Education",
        subtitle = "Courses & Books",
        icon = Icons.Default.School,
        color = CategoryEducation,
        bgLightColor = CategoryEducationBg
    )
    val WORK = CategoryInfo(
        id = "work",
        name = "Work",
        subtitle = "Salary & Freelance",
        icon = Icons.Default.Work,
        color = CategoryWork,
        bgLightColor = CategoryWorkBg
    )
    val OTHER = CategoryInfo(
        id = "other",
        name = "Other",
        subtitle = "General",
        icon = Icons.Default.MoreHoriz,
        color = CategoryOther,
        bgLightColor = CategoryOtherBg
    )

    val expenseCategories = listOf(
        FOOD_DINING,
        TRANSPORT,
        SHOPPING,
        BILLS,
        HEALTH,
        ENTERTAINMENT,
        EDUCATION,
        OTHER
    )

    val allCategories = listOf(
        FOOD_DINING,
        TRANSPORT,
        SHOPPING,
        BILLS,
        HEALTH,
        ENTERTAINMENT,
        EDUCATION,
        WORK,
        OTHER
    )

    fun getCategory(id: String): CategoryInfo {
        return allCategories.find { it.id.equals(id, ignoreCase = true) || it.name.equals(id, ignoreCase = true) } ?: OTHER
    }
}

data class CurrencyItem(
    val code: String,
    val symbol: String,
    val name: String
)

val SupportedCurrencies = listOf(
    CurrencyItem("INR", "₹", "Indian Rupee"),
    CurrencyItem("USD", "$", "US Dollar"),
    CurrencyItem("EUR", "€", "Euro"),
    CurrencyItem("GBP", "£", "British Pound"),
    CurrencyItem("JPY", "¥", "Japanese Yen"),
    CurrencyItem("AUD", "A$", "Australian Dollar"),
    CurrencyItem("CAD", "C$", "Canadian Dollar")
)
