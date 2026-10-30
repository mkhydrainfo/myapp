package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CategoryRegistry
import com.example.data.TransactionEntity
import com.example.ui.theme.*

@Composable
fun TotalBalanceCard(
    balanceFormatted: String,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp))
            .testTag("total_balance_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF059669),
                            Color(0xFF047857)
                        )
                    )
                )
                .padding(horizontal = 22.dp, vertical = 20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Balance",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    IconButton(
                        onClick = onToggleVisibility,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("toggle_balance_visibility")
                    ) {
                        Icon(
                            imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Balance Visibility",
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isVisible) balanceFormatted else "••••••••",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Monthly indicator pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "↗ + ₹5,200 this month",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun IncomeExpenseSummary(
    incomeFormatted: String,
    expenseFormatted: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Income Card
        Card(
            modifier = Modifier
                .weight(1f)
                .testTag("income_summary_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5))
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD1FAE5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "Income",
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Income",
                        fontSize = 13.sp,
                        color = Neutral600,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = incomeFormatted,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF047857)
                )
            }
        }

        // Expense Card
        Card(
            modifier = Modifier
                .weight(1f)
                .testTag("expense_summary_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2))
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEE2E2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = "Expenses",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Expenses",
                        fontSize = 13.sp,
                        color = Neutral600,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = expenseFormatted,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB91C1C)
                )
            }
        }
    }
}

@Composable
fun TransactionRowItem(
    transaction: TransactionEntity,
    formattedAmount: String,
    relativeDate: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val category = CategoryRegistry.getCategory(transaction.categoryId)
    val isIncome = transaction.type == "INCOME"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("transaction_item_${transaction.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon with soft background
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(category.bgLightColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    tint = category.color,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Title and category / note
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = transaction.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (transaction.note.isNotBlank()) "${category.name} • ${transaction.note}" else category.name,
                    fontSize = 12.sp,
                    color = Neutral500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Amount and Date
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = if (isIncome) "+ $formattedAmount" else "- $formattedAmount",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isIncome) Color(0xFF10B981) else Color(0xFFEF4444)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = relativeDate,
                    fontSize = 12.sp,
                    color = Neutral400
                )
            }
        }
    }
}
