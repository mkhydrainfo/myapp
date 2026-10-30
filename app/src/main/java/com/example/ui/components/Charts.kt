package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Neutral300
import com.example.ui.theme.Neutral400
import com.example.ui.theme.Neutral500
import com.example.ui.viewmodel.CategorySpendItem
import com.example.ui.viewmodel.TrendBarItem

@Composable
fun DonutChartWithLegend(
    items: List<CategorySpendItem>,
    totalSpentFormatted: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("donut_chart_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Donut Chart Canvas with Center Text
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 24.dp.toPx()
                    val diameter = size.minDimension - strokeWidth
                    val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
                    val arcSize = Size(diameter, diameter)

                    var startAngle = -90f
                    val totalPct = items.sumOf { it.percentage.toDouble() }.toFloat().coerceAtLeast(1f)

                    items.forEach { item ->
                        val sweepAngle = (item.percentage / totalPct) * 360f
                        drawArc(
                            color = item.category.color,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle.coerceAtLeast(2f),
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                        )
                        startAngle += sweepAngle
                    }
                }

                // Center Label
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Total Spent",
                        fontSize = 11.sp,
                        color = Neutral500,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = totalSpentFormatted,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Legend items
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items.take(6).forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(item.category.color)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.category.name,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text(
                            text = "${item.percentage.toInt()}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Neutral500
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpendingTrendBarChart(
    trends: List<TrendBarItem>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("trend_bar_chart_card"),
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
                text = "Spending Trend",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // Y-Axis labels
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    Text("12k", fontSize = 10.sp, color = Neutral400)
                    Text("8k", fontSize = 10.sp, color = Neutral400)
                    Text("4k", fontSize = 10.sp, color = Neutral400)
                    Text("0", fontSize = 10.sp, color = Neutral400)
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Chart Bars Area
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Bottom
                ) {
                    trends.forEach { bar ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            // Bar
                            Box(
                                modifier = Modifier
                                    .width(18.dp)
                                    .fillMaxHeight(bar.heightRatio.coerceIn(0.12f, 0.95f))
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(Emerald600)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // X-axis label
                            Text(
                                text = bar.label,
                                fontSize = 10.sp,
                                color = Neutral500,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BudgetProgressBar(
    percentage: Float,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    val clampedRatio = (percentage / 100f).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Neutral300.copy(alpha = 0.35f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(clampedRatio)
                .fillMaxHeight()
                .clip(RoundedCornerShape(4.dp))
                .background(barColor)
        )
    }
}
