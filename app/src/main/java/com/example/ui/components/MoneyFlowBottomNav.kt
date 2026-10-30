package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.viewmodel.AppNavTab

data class NavItem(
    val tab: AppNavTab,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun MoneyFlowBottomNav(
    currentTab: AppNavTab,
    onTabSelected: (AppNavTab) -> Unit,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(AppNavTab.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
        NavItem(AppNavTab.TRANSACTIONS, "Transactions", Icons.Filled.ReceiptLong, Icons.Outlined.ReceiptLong),
        NavItem(AppNavTab.BUDGETS, "Budgets", Icons.Filled.Savings, Icons.Outlined.Savings),
        NavItem(AppNavTab.PROFILE, "Profile", Icons.Filled.Person, Icons.Outlined.Person)
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // First 2 items
            items.take(2).forEach { item ->
                BottomNavTabItem(
                    item = item,
                    isSelected = currentTab == item.tab,
                    onClick = { onTabSelected(item.tab) }
                )
            }

            // Center FAB for Add Transaction (+)
            Box(
                modifier = Modifier
                    .offset(y = (-8).dp)
                    .size(54.dp)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Emerald600)
                    .clickable(onClick = onFabClick)
                    .testTag("fab_add_transaction"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Transaction",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Last 2 items
            items.takeLast(2).forEach { item ->
                BottomNavTabItem(
                    item = item,
                    isSelected = currentTab == item.tab,
                    onClick = { onTabSelected(item.tab) }
                )
            }
        }
    }
}

@Composable
private fun BottomNavTabItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val tint = if (isSelected) Emerald600 else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("nav_tab_${item.label.lowercase()}"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.label,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = item.label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = tint
        )
    }
}
