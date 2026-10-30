package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Neutral400
import com.example.ui.theme.Neutral500
import com.example.ui.theme.Neutral900

data class OnboardingStep(
    val title: String,
    val description: String,
    val type: Int // 1 = Wallet, 2 = Charts, 3 = Piggy Bank
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        OnboardingStep(
            title = "Take control\nof your money",
            description = "Track your income, expenses\nand build better money habits.",
            type = 1
        ),
        OnboardingStep(
            title = "See your\nspending patterns",
            description = "Get clear insights with simple\ncharts and smart categories.",
            type = 2
        ),
        OnboardingStep(
            title = "Save for\nyour goals",
            description = "Set budgets, track progress\nand reach your goals faster.",
            type = 3
        )
    )

    var currentStepIndex by remember { mutableStateOf(0) }
    val step = steps[currentStepIndex]

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("onboarding_screen"),
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar with "Skip"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (currentStepIndex < steps.size - 1) {
                    Text(
                        text = "Skip",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Neutral500,
                        modifier = Modifier
                            .clickable { onFinish() }
                            .padding(8.dp)
                            .testTag("onboarding_skip")
                    )
                } else {
                    Spacer(modifier = Modifier.height(36.dp))
                }
            }

            Spacer(modifier = Modifier.weight(0.6f))

            // Graphic Illustration Area
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFECFDF5)),
                contentAlignment = Alignment.Center
            ) {
                when (step.type) {
                    1 -> OnboardingIllustrationWallet()
                    2 -> OnboardingIllustrationChart()
                    3 -> OnboardingIllustrationPiggy()
                }
            }

            Spacer(modifier = Modifier.weight(0.8f))

            // Headline
            Text(
                text = step.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Neutral900,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Subtitle
            Text(
                text = step.description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Neutral500,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.weight(0.6f))

            // Pagination Dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.indices.forEach { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == currentStepIndex) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == currentStepIndex) Emerald600 else Neutral400.copy(alpha = 0.4f)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Button ("Next" or "Get Started")
            Button(
                onClick = {
                    if (currentStepIndex < steps.size - 1) {
                        currentStepIndex++
                    } else {
                        onFinish()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("onboarding_action_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Emerald600)
            ) {
                Text(
                    text = if (currentStepIndex == steps.size - 1) "Get Started" else "Next",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun OnboardingIllustrationWallet() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Receipt back
        Card(
            modifier = Modifier
                .offset(x = (-16).dp, y = (-24).dp)
                .size(90.dp, 110.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Box(modifier = Modifier.size(30.dp, 6.dp).background(Color(0xFFE2E8F0), CircleShape))
                Spacer(modifier = Modifier.height(6.dp))
                Box(modifier = Modifier.size(50.dp, 4.dp).background(Color(0xFFF1F5F9), CircleShape))
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.size(40.dp, 4.dp).background(Color(0xFFF1F5F9), CircleShape))
            }
        }

        // Green Wallet
        Card(
            modifier = Modifier
                .offset(x = 12.dp, y = 14.dp)
                .size(110.dp, 80.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Emerald600),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(24.dp, 16.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF047857)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFFCD34D)))
                }
            }
        }

        // Floating Gold Coins
        Icon(
            imageVector = Icons.Default.MonetizationOn,
            contentDescription = null,
            tint = Color(0xFFF59E0B),
            modifier = Modifier
                .offset(x = 52.dp, y = (-30).dp)
                .size(28.dp)
        )
    }
}

@Composable
private fun OnboardingIllustrationChart() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Trend arrow
            Icon(
                imageVector = Icons.Default.TrendingUp,
                contentDescription = null,
                tint = Emerald600,
                modifier = Modifier
                    .size(46.dp)
                    .offset(x = 18.dp, y = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bars
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.size(18.dp, 40.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFFA7F3D0)))
                Box(modifier = Modifier.size(18.dp, 60.dp).clip(RoundedCornerShape(4.dp)).background(Emerald500))
                Box(modifier = Modifier.size(18.dp, 82.dp).clip(RoundedCornerShape(4.dp)).background(Emerald600))
            }
        }
    }
}

@Composable
private fun OnboardingIllustrationPiggy() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Coin dropping in
            Icon(
                imageVector = Icons.Default.MonetizationOn,
                contentDescription = null,
                tint = Color(0xFFF59E0B),
                modifier = Modifier
                    .size(32.dp)
                    .offset(y = (-4).dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Piggy Bank
            Box(
                modifier = Modifier
                    .size(100.dp, 80.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color(0xFFA7F3D0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Savings,
                    contentDescription = null,
                    tint = Emerald600,
                    modifier = Modifier.size(54.dp)
                )
            }
        }
    }
}
