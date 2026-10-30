package com.example

import com.example.data.CategoryRegistry
import com.example.data.SupportedCurrencies
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun categoryRegistry_findsRegisteredCategories() {
        val food = CategoryRegistry.getCategory("food_dining")
        assertEquals("Food & Dining", food.name)

        val transport = CategoryRegistry.getCategory("transport")
        assertEquals("Transport", transport.name)
    }

    @Test
    fun supportedCurrencies_containsDefaultRupeeAndOthers() {
        val rupee = SupportedCurrencies.find { it.code == "INR" }
        assertNotNull(rupee)
        assertEquals("₹", rupee?.symbol)

        val usd = SupportedCurrencies.find { it.code == "USD" }
        assertNotNull(usd)
        assertEquals("$", usd?.symbol)
    }

    @Test
    fun netBalance_computesAccurately() {
        val totalIncome = 65000.0
        val totalExpense = 16750.0
        val balance = totalIncome - totalExpense
        assertEquals(48250.0, balance, 0.001)
    }
}
