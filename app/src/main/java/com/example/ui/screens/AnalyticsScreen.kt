package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.TrendingUp
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
import com.example.ui.components.DonutChartWithLegend
import com.example.ui.components.SpendingTrendBarChart
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Neutral500
import com.example.ui.viewmodel.AnalyticsTab
import com.example.ui.viewmodel.MoneyFlowViewModel

@Composable
fun AnalyticsScreen(
    viewModel: MoneyFlowViewModel,
    modifier: Modifier = Modifier
) {
    val analyticsTab by viewModel.analyticsTab.collectAsStateWithLifecycle()
    val monthLabel by viewModel.currentMonthLabel.collectAsStateWithLifecycle()
    val totalExpense by viewModel.totalExpense.collectAsStateWithLifecycle()
    val totalIncome by viewModel.totalIncome.collectAsStateWithLifecycle()
    val categorySpendList by viewModel.categorySpendList.collectAsStateWithLifecycle()
    val spendingTrends by viewModel.spendingTrends.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("analytics_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Screen Header
        item {
            Text(
                text = "Analytics",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Segmented Tabs: Spending, Income, Savings
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(
                    AnalyticsTab.SPENDING to "Spending",
                    AnalyticsTab.INCOME to "Income",
                    AnalyticsTab.SAVINGS to "Savings"
                ).forEach { (tab, label) ->
                    val isSelected = analyticsTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Emerald600 else Color.Transparent)
                            .clickable { viewModel.analyticsTab.value = tab }
                            .padding(vertical = 10.dp)
                            .testTag("analytics_tab_${tab.name.lowercase()}"),
                        contentAlignment = Alignment.Center
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
        }

        // Month Selector `< October 2026 >`
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* Previous month */ },
                    modifier = Modifier.size(36.dp)
                ) {
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

                IconButton(
                    onClick = { /* Next month */ },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Next Month",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Donut Chart with Category Legend
        item {
            val totalFormatted = when (analyticsTab) {
                AnalyticsTab.SPENDING -> viewModel.formatCurrency(totalExpense)
                AnalyticsTab.INCOME -> viewModel.formatCurrency(totalIncome)
                AnalyticsTab.SAVINGS -> viewModel.formatCurrency(totalIncome - totalExpense)
            }

            DonutChartWithLegend(
                items = categorySpendList,
                totalSpentFormatted = totalFormatted
            )
        }

        // Spending Trend Bar Chart
        item {
            SpendingTrendBarChart(trends = spendingTrends)
        }

        // This Month vs Last Month Comparison Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("comparison_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "This Month vs Last Month",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Neutral500
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "+12%",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Emerald600
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "₹2,100 more than last month",
                                fontSize = 13.sp,
                                color = Neutral500
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFECFDF5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = Emerald600,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
