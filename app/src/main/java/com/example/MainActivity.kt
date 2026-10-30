package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.MoneyFlowBottomNav
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppNavTab
import com.example.ui.viewmodel.MoneyFlowViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MoneyFlowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
            val isDark = userProfile.isDarkMode

            MyApplicationTheme(darkTheme = isDark) {
                MoneyFlowApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MoneyFlowApp(viewModel: MoneyFlowViewModel) {
    val showSplash by viewModel.showSplash.collectAsStateWithLifecycle()
    val showOnboarding by viewModel.showOnboarding.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val showAddSheet by viewModel.showAddSheet.collectAsStateWithLifecycle()

    when {
        showSplash -> {
            SplashScreen(
                onContinue = { viewModel.completeSplash() }
            )
        }
        showOnboarding -> {
            BackHandler {
                viewModel.completeOnboarding()
            }
            OnboardingScreen(
                onFinish = { viewModel.completeOnboarding() }
            )
        }
        else -> {
            // Handle Android back button to navigate back to Home if in another tab
            BackHandler(enabled = currentTab != AppNavTab.HOME) {
                viewModel.setTab(AppNavTab.HOME)
            }

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("moneyflow_main_scaffold"),
                bottomBar = {
                    MoneyFlowBottomNav(
                        currentTab = currentTab,
                        onTabSelected = { viewModel.setTab(it) },
                        onFabClick = { viewModel.openAddSheet() }
                    )
                },
                contentWindowInsets = WindowInsets.safeDrawing
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(bottom = innerPadding.calculateBottomPadding())
                ) {
                    Crossfade(
                        targetState = currentTab,
                        label = "tab_crossfade"
                    ) { tab ->
                        when (tab) {
                            AppNavTab.HOME -> HomeScreen(
                                viewModel = viewModel,
                                onSeeAllTransactions = { viewModel.setTab(AppNavTab.TRANSACTIONS) },
                                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                            )
                            AppNavTab.TRANSACTIONS -> TransactionsScreen(
                                viewModel = viewModel,
                                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                            )
                            AppNavTab.BUDGETS -> BudgetsScreen(
                                viewModel = viewModel,
                                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                            )
                            AppNavTab.PROFILE -> ProfileScreen(
                                viewModel = viewModel,
                                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                            )
                        }
                    }
                }
            }

            // Modal Add/Edit Transaction Bottom Sheet
            if (showAddSheet) {
                AddTransactionSheet(
                    viewModel = viewModel,
                    onDismiss = { viewModel.closeAddSheet() }
                )
            }
        }
    }
}
