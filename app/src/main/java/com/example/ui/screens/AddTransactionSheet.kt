package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CategoryInfo
import com.example.data.CategoryRegistry
import com.example.data.TransactionType
import com.example.ui.theme.Emerald600
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.ExpenseRedLight
import com.example.ui.theme.Neutral200
import com.example.ui.theme.Neutral400
import com.example.ui.theme.Neutral500
import com.example.ui.theme.Neutral900
import com.example.ui.viewmodel.MoneyFlowViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionSheet(
    viewModel: MoneyFlowViewModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val editingTx = viewModel.editingTransaction.collectAsState().value
    val userProfile = viewModel.userProfile.collectAsState().value

    var transactionType by remember {
        mutableStateOf(
            if (editingTx?.type == "INCOME") TransactionType.INCOME else TransactionType.EXPENSE
        )
    }

    var title by remember { mutableStateOf(editingTx?.title ?: "") }
    var amountText by remember {
        mutableStateOf(
            if (editingTx != null && editingTx.amount > 0) {
                if (editingTx.amount % 1.0 == 0.0) editingTx.amount.toLong().toString()
                else editingTx.amount.toString()
            } else ""
        )
    }
    var selectedCategoryId by remember {
        mutableStateOf(editingTx?.categoryId ?: CategoryRegistry.FOOD_DINING.id)
    }
    var note by remember { mutableStateOf(editingTx?.note ?: "") }
    val timestamp by remember { mutableStateOf(editingTx?.timestamp ?: System.currentTimeMillis()) }

    val formattedDate = remember(timestamp) {
        val sdf = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())
        "Today, ${sdf.format(Date(timestamp))}"
    }

    val categories = if (transactionType == TransactionType.EXPENSE) {
        CategoryRegistry.expenseCategories
    } else {
        listOf(
            CategoryRegistry.WORK,
            CategoryRegistry.OTHER,
            CategoryRegistry.SHOPPING,
            CategoryRegistry.FOOD_DINING
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = modifier.testTag("add_transaction_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header: Close '✕' & Title & Optional Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp).testTag("btn_close_add_sheet")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = if (editingTx != null) "Edit Transaction" else "Add Transaction",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (editingTx != null) {
                    IconButton(
                        onClick = {
                            viewModel.deleteTransaction(editingTx.id)
                            onDismiss()
                        },
                        modifier = Modifier.size(36.dp).testTag("btn_delete_transaction")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Transaction",
                            tint = ExpenseRed
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(36.dp))
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Segmented Pill: Expense vs Income
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Expense Pill
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (transactionType == TransactionType.EXPENSE) ExpenseRedLight else Color.Transparent
                        )
                        .clickable { transactionType = TransactionType.EXPENSE }
                        .padding(vertical = 10.dp)
                        .testTag("tab_expense"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "– Expense",
                        fontSize = 14.sp,
                        fontWeight = if (transactionType == TransactionType.EXPENSE) FontWeight.Bold else FontWeight.Medium,
                        color = if (transactionType == TransactionType.EXPENSE) ExpenseRed else Neutral500
                    )
                }

                // Income Pill
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (transactionType == TransactionType.INCOME) Color(0xFFD1FAE5) else Color.Transparent
                        )
                        .clickable {
                            transactionType = TransactionType.INCOME
                            selectedCategoryId = CategoryRegistry.WORK.id
                        }
                        .padding(vertical = 10.dp)
                        .testTag("tab_income"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+ Income",
                        fontSize = 14.sp,
                        fontWeight = if (transactionType == TransactionType.INCOME) FontWeight.Bold else FontWeight.Medium,
                        color = if (transactionType == TransactionType.INCOME) Emerald600 else Neutral500
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Large Amount Display & Input
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${userProfile.currencySymbol} ",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (transactionType == TransactionType.EXPENSE) ExpenseRed else Emerald600
                        )

                        OutlinedTextField(
                            value = amountText,
                            onValueChange = { input ->
                                if (input.isEmpty() || input.matches(Regex("^\\d*(\\.\\d{0,2})?$"))) {
                                    amountText = input
                                }
                            },
                            placeholder = {
                                Text(
                                    text = "0.00",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Neutral400
                                )
                            },
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Start
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .widthIn(min = 120.dp, max = 220.dp)
                                .testTag("input_transaction_amount")
                        )
                    }

                    // Quick increment shortcuts
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(50, 100, 500, 1000).forEach { add ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .clickable {
                                        val current = amountText.toDoubleOrNull() ?: 0.0
                                        val updated = current + add
                                        amountText = if (updated % 1.0 == 0.0) updated.toLong().toString() else String.format(Locale.US, "%.2f", updated)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "+$add",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Neutral500
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Category Title
            Text(
                text = "Category",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Category Grid (2 columns or 4 columns)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                categories.chunked(3).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowItems.forEach { cat ->
                            val isSelected = cat.id.equals(selectedCategoryId, ignoreCase = true)
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable {
                                        selectedCategoryId = cat.id
                                        if (title.isBlank()) {
                                            title = cat.name
                                        }
                                    }
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) cat.color else Neutral200,
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .testTag("category_item_${cat.id}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) cat.bgLightColor else MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = cat.icon,
                                        contentDescription = cat.name,
                                        tint = cat.color,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = cat.name,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                        // Fill empty slots if any
                        repeat(3 - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Date selector indicator
            Text(
                text = "Date",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Neutral500,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = formattedDate,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Note (optional)
            Text(
                text = "Note (optional)",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                placeholder = { Text("Add a note...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_transaction_note"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Neutral200,
                    focusedBorderColor = Emerald600
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(26.dp))

            // Save Button
            Button(
                onClick = {
                    val parsedAmount = amountText.toDoubleOrNull() ?: 0.0
                    if (parsedAmount > 0) {
                        val finalTitle = if (title.isNotBlank()) title else CategoryRegistry.getCategory(selectedCategoryId).name
                        viewModel.saveTransaction(
                            title = finalTitle,
                            amount = parsedAmount,
                            type = transactionType,
                            categoryId = selectedCategoryId,
                            note = note,
                            timestamp = timestamp
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("btn_save_transaction"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
                enabled = (amountText.toDoubleOrNull() ?: 0.0) > 0
            ) {
                Text(
                    text = "Save",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
