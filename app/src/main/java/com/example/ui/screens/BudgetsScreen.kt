package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.CategoryRegistry
import com.example.ui.components.BudgetProgressBar
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Neutral200
import com.example.ui.theme.Neutral400
import com.example.ui.theme.Neutral500
import com.example.ui.theme.Neutral900
import com.example.ui.viewmodel.BudgetStatusItem
import com.example.ui.viewmodel.MoneyFlowViewModel

@Composable
fun BudgetsScreen(
    viewModel: MoneyFlowViewModel,
    modifier: Modifier = Modifier
) {
    val monthLabel by viewModel.currentMonthLabel.collectAsStateWithLifecycle()
    val budgetStatusList by viewModel.budgetStatusList.collectAsStateWithLifecycle()

    var showAddBudgetDialog by remember { mutableStateOf(false) }
    var selectedCategoryForBudget by remember { mutableStateOf(CategoryRegistry.FOOD_DINING.id) }
    var budgetLimitInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("budgets_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header with Title and `+` action
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Budgets",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                IconButton(
                    onClick = { showAddBudgetDialog = true },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .testTag("btn_add_budget")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Set Budget",
                        tint = Emerald600,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Month Selector `< October 2026 >`
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Previous Month",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = monthLabel,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(onClick = {}, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Next Month",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Budget Items
        items(budgetStatusList, key = { it.category.id }) { item ->
            BudgetCardItem(
                item = item,
                viewModel = viewModel,
                onEdit = {
                    selectedCategoryForBudget = item.category.id
                    budgetLimitInput = item.limit.toLong().toString()
                    showAddBudgetDialog = true
                }
            )
        }

        // Smart Status Advice Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("budget_status_advice_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD1FAE5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Adjust,
                            contentDescription = null,
                            tint = Emerald600,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "You're on track!",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF047857)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "You've spent 12% less than your budget this month.",
                            fontSize = 13.sp,
                            color = Color(0xFF065F46),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }

    // Set / Edit Budget Dialog
    if (showAddBudgetDialog) {
        AlertDialog(
            onDismissRequest = { showAddBudgetDialog = false },
            title = { Text("Set Monthly Budget", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Select Category:", fontSize = 13.sp, color = Neutral500)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CategoryRegistry.expenseCategories.take(4).forEach { cat ->
                            val isSel = cat.id == selectedCategoryForBudget
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) cat.bgLightColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .clickable { selectedCategoryForBudget = cat.id }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cat.name.split(" ").first(),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) cat.color else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = budgetLimitInput,
                        onValueChange = { budgetLimitInput = it },
                        label = { Text("Monthly Limit (${viewModel.userProfile.value.currencySymbol})") },
                        placeholder = { Text("e.g. 5000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_budget_limit")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val limit = budgetLimitInput.toDoubleOrNull() ?: 0.0
                        if (limit > 0) {
                            viewModel.saveBudget(selectedCategoryForBudget, limit)
                            showAddBudgetDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald600)
                ) {
                    Text("Save Budget", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddBudgetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun BudgetCardItem(
    item: BudgetStatusItem,
    viewModel: MoneyFlowViewModel,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onEdit)
            .testTag("budget_card_${item.category.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(item.category.bgLightColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.category.icon,
                            contentDescription = item.category.name,
                            tint = item.category.color,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = item.category.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${viewModel.formatCurrency(item.spent)} / ${viewModel.formatCurrency(item.limit)}",
                            fontSize = 13.sp,
                            color = Neutral500,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Percentage Tag
                Text(
                    text = "${item.percentage.toInt()}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (item.percentage > 90f) Color(0xFFEF4444) else Neutral500
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Color-coded progress bar
            val barColor = when {
                item.percentage > 90f -> Color(0xFFEF4444)
                item.percentage > 70f -> Color(0xFFF59E0B)
                else -> item.category.color
            }

            BudgetProgressBar(
                percentage = item.percentage,
                barColor = barColor
            )
        }
    }
}
