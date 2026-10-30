package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.TransactionEntity
import com.example.ui.components.IncomeExpenseSummary
import com.example.ui.components.TotalBalanceCard
import com.example.ui.components.TransactionRowItem
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Neutral500
import com.example.ui.theme.Neutral900
import com.example.ui.viewmodel.AppNavTab
import com.example.ui.viewmodel.MoneyFlowViewModel

@Composable
fun HomeScreen(
    viewModel: MoneyFlowViewModel,
    onSeeAllTransactions: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val isBalanceVisible by viewModel.isBalanceVisible.collectAsStateWithLifecycle()
    val totalBalance by viewModel.totalBalance.collectAsStateWithLifecycle()
    val totalIncome by viewModel.totalIncome.collectAsStateWithLifecycle()
    val totalExpense by viewModel.totalExpense.collectAsStateWithLifecycle()
    val transactions by viewModel.allTransactions.collectAsStateWithLifecycle()

    val recentTransactions = transactions.take(5)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Top Greeting Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Good morning,",
                        fontSize = 13.sp,
                        color = Neutral500,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${userProfile.name} ☀️",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Notification Bell
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .clickable { /* Notifications */ }
                        .testTag("btn_notifications"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(22.dp)
                    )
                    // Notification badge dot
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 10.dp, end = 10.dp)
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444))
                    )
                }
            }
        }

        // Total Balance Gradient Card
        item {
            TotalBalanceCard(
                balanceFormatted = viewModel.formatCurrency(totalBalance),
                isVisible = isBalanceVisible,
                onToggleVisibility = { viewModel.toggleBalanceVisibility() }
            )
        }

        // Income & Expenses Summary Cards
        item {
            IncomeExpenseSummary(
                incomeFormatted = viewModel.formatCurrency(totalIncome),
                expenseFormatted = viewModel.formatCurrency(totalExpense)
            )
        }

        // Recent Transactions Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Transactions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "See all",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Emerald600,
                    modifier = Modifier
                        .clickable(onClick = onSeeAllTransactions)
                        .padding(4.dp)
                        .testTag("btn_see_all_transactions")
                )
            }
        }

        // Recent Transactions List
        if (recentTransactions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No transactions yet",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Neutral500
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tap the '+' button below to add your first expense or income.",
                            fontSize = 13.sp,
                            color = Neutral500,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        } else {
            items(recentTransactions, key = { it.id }) { tx ->
                TransactionRowItem(
                    transaction = tx,
                    formattedAmount = viewModel.formatCurrency(tx.amount),
                    relativeDate = viewModel.formatRelativeDate(tx.timestamp),
                    onClick = { viewModel.openAddSheet(tx) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
