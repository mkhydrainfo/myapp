package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.CategoryRegistry
import com.example.ui.components.TransactionRowItem
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Neutral200
import com.example.ui.theme.Neutral400
import com.example.ui.theme.Neutral500
import com.example.ui.theme.Neutral900
import com.example.ui.viewmodel.MoneyFlowViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    viewModel: MoneyFlowViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val transactionFilter by viewModel.transactionFilter.collectAsStateWithLifecycle()
    val selectedCategoryFilter by viewModel.selectedCategoryFilter.collectAsStateWithLifecycle()
    val transactions by viewModel.filteredTransactions.collectAsStateWithLifecycle()

    var showCategoryFilterDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("transactions_screen")
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Screen Title
        Text(
            text = "Transactions",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.searchQuery.value = it },
            placeholder = { Text("Search transactions...", fontSize = 14.sp, color = Neutral400) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Neutral500,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = Neutral500,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_search_transactions"),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Neutral200,
                focusedBorderColor = Emerald600,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Filter Chips Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    "ALL" to "All",
                    "INCOME" to "Income",
                    "EXPENSE" to "Expense"
                ).forEach { (key, label) ->
                    val isSelected = transactionFilter == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isSelected) Emerald600 else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                            .clickable { viewModel.transactionFilter.value = key }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("filter_chip_$key")
                    ) {
                        Text(
                            text = label,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Filter button icon
            IconButton(
                onClick = { showCategoryFilterDialog = true },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (selectedCategoryFilter != null) Emerald600 else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                    .testTag("btn_category_filter")
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter by Category",
                    tint = if (selectedCategoryFilter != null) Color.White else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        if (selectedCategoryFilter != null) {
            val cat = CategoryRegistry.getCategory(selectedCategoryFilter!!)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(cat.bgLightColor)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Category: ${cat.name}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = cat.color
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear filter",
                    tint = cat.color,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { viewModel.selectedCategoryFilter.value = null }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Transaction List
        if (transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No transactions found",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Neutral500
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Try adjusting your search or filters.",
                        fontSize = 13.sp,
                        color = Neutral400
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(transactions, key = { it.id }) { tx ->
                    TransactionRowItem(
                        transaction = tx,
                        formattedAmount = viewModel.formatCurrency(tx.amount),
                        relativeDate = viewModel.formatRelativeDate(tx.timestamp),
                        onClick = { viewModel.openAddSheet(tx) }
                    )
                }
            }
        }
    }

    // Category Filter Selection Dialog
    if (showCategoryFilterDialog) {
        AlertDialog(
            onDismissRequest = { showCategoryFilterDialog = false },
            title = { Text("Filter by Category", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "All Categories",
                        fontWeight = if (selectedCategoryFilter == null) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedCategoryFilter == null) Emerald600 else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.selectedCategoryFilter.value = null
                                showCategoryFilterDialog = false
                            }
                            .padding(vertical = 8.dp)
                    )
                    CategoryRegistry.allCategories.forEach { cat ->
                        Text(
                            text = cat.name,
                            fontWeight = if (selectedCategoryFilter == cat.id) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedCategoryFilter == cat.id) Emerald600 else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.selectedCategoryFilter.value = cat.id
                                    showCategoryFilterDialog = false
                                }
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCategoryFilterDialog = false }) {
                    Text("Close", color = Emerald600)
                }
            }
        )
    }
}
