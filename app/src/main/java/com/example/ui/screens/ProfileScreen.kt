package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.SupportedCurrencies
import com.example.ui.theme.Emerald600
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.Neutral200
import com.example.ui.theme.Neutral400
import com.example.ui.theme.Neutral500
import com.example.ui.theme.Neutral900
import com.example.ui.viewmodel.MoneyFlowViewModel

@Composable
fun ProfileScreen(
    viewModel: MoneyFlowViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showAccountDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }

    var editName by remember { mutableStateOf(userProfile.name) }
    var editEmail by remember { mutableStateOf(userProfile.email) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Avatar and Info Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .testTag("user_profile_header_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar circle with initial "M"
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userProfile.name.take(1).uppercase(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userProfile.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = userProfile.email,
                            fontSize = 13.sp,
                            color = Neutral500
                        )
                    }

                    IconButton(
                        onClick = {
                            editName = userProfile.name
                            editEmail = userProfile.email
                            showAccountDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = Emerald600,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Settings Menu Items List
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    ProfileMenuItem(
                        icon = Icons.Outlined.Lock,
                        title = "Account & Security",
                        trailingText = null,
                        onClick = {
                            editName = userProfile.name
                            editEmail = userProfile.email
                            showAccountDialog = true
                        }
                    )
                    Divider(color = Neutral200.copy(alpha = 0.5f), thickness = 0.8.dp)

                    ProfileMenuItem(
                        icon = Icons.Outlined.Paid,
                        title = "Currency",
                        trailingText = "${userProfile.currencyCode} (${userProfile.currencySymbol})",
                        onClick = { showCurrencyDialog = true }
                    )
                    Divider(color = Neutral200.copy(alpha = 0.5f), thickness = 0.8.dp)

                    ProfileMenuItemWithSwitch(
                        icon = Icons.Outlined.Notifications,
                        title = "Notifications",
                        checked = userProfile.notificationsEnabled,
                        onCheckedChange = { viewModel.toggleNotifications(it) }
                    )
                    Divider(color = Neutral200.copy(alpha = 0.5f), thickness = 0.8.dp)

                    ProfileMenuItem(
                        icon = Icons.Outlined.Palette,
                        title = "Appearance",
                        trailingText = if (userProfile.isDarkMode) "Dark" else "Light",
                        onClick = { viewModel.toggleDarkMode(!userProfile.isDarkMode) }
                    )
                    Divider(color = Neutral200.copy(alpha = 0.5f), thickness = 0.8.dp)

                    ProfileMenuItem(
                        icon = Icons.Outlined.HelpOutline,
                        title = "Help & Support",
                        trailingText = null,
                        onClick = { /* Help info */ }
                    )
                    Divider(color = Neutral200.copy(alpha = 0.5f), thickness = 0.8.dp)

                    ProfileMenuItem(
                        icon = Icons.Outlined.Info,
                        title = "About App",
                        trailingText = "v1.0.0",
                        onClick = { showAboutDialog = true }
                    )
                    Divider(color = Neutral200.copy(alpha = 0.5f), thickness = 0.8.dp)

                    ProfileMenuItem(
                        icon = Icons.Outlined.RestartAlt,
                        title = "Reset Demo Data",
                        trailingText = "Restore",
                        onClick = { showResetConfirmDialog = true }
                    )
                    Divider(color = Neutral200.copy(alpha = 0.5f), thickness = 0.8.dp)

                    ProfileMenuItem(
                        icon = Icons.Outlined.School,
                        title = "View Onboarding",
                        trailingText = null,
                        onClick = { viewModel.restartOnboarding() }
                    )
                }
            }
        }

        // Log Out Button matching Screen 10
        item {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = { showResetConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_logout"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ExpenseRed),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(ExpenseRed.copy(alpha = 0.35f)))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Log Out",
                        tint = ExpenseRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Log Out",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ExpenseRed
                    )
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
        }
    }

    // Currency Selector Dialog
    if (showCurrencyDialog) {
        AlertDialog(
            onDismissRequest = { showCurrencyDialog = false },
            title = { Text("Choose Currency", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    SupportedCurrencies.forEach { curr ->
                        val isSel = curr.code == userProfile.currencyCode
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) Emerald600.copy(alpha = 0.1f) else Color.Transparent)
                                .clickable {
                                    viewModel.updateCurrency(curr)
                                    showCurrencyDialog = false
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${curr.name} (${curr.symbol})",
                                fontSize = 14.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) Emerald600 else MaterialTheme.colorScheme.onSurface
                            )
                            if (isSel) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Emerald600,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCurrencyDialog = false }) {
                    Text("Close", color = Emerald600)
                }
            }
        )
    }

    // Account & Security Edit Dialog
    if (showAccountDialog) {
        AlertDialog(
            onDismissRequest = { showAccountDialog = false },
            title = { Text("Edit Account", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Full Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email Address") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editName.isNotBlank() && editEmail.isNotBlank()) {
                            viewModel.updateProfile(editName.trim(), editEmail.trim())
                            showAccountDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald600)
                ) {
                    Text("Save", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAccountDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // About App Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About MoneyFlow", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("MoneyFlow — Track. Save. Grow.", fontWeight = FontWeight.SemiBold)
                    Text("Version 1.0.0", fontSize = 13.sp, color = Neutral500)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "MoneyFlow is an offline personal finance companion designed with local SQLite/Room persistence, intuitive budget management, and interactive spending insights.",
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("OK", color = Emerald600)
                }
            }
        )
    }

    // Reset Confirm Dialog
    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = { Text("Reset Demo Data", fontWeight = FontWeight.Bold) },
            text = {
                Text("This will restore the showcase demo transactions (Freelance, Groceries, Electricity Bill) and budgets into offline local storage.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetData()
                        showResetConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald600)
                ) {
                    Text("Reset", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    trailingText: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Neutral500,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (trailingText != null) {
                Text(
                    text = trailingText,
                    fontSize = 13.sp,
                    color = Neutral400,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Neutral400,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun ProfileMenuItemWithSwitch(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Neutral500,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Emerald600
            )
        )
    }
}
