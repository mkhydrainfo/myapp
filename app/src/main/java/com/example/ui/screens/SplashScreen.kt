package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Neutral500
import com.example.ui.theme.Neutral900
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Gentle pulse animation for the leaf logo
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "leaf_scale"
    )

    // Automatically transition after brief display, or tap to enter
    LaunchedEffect(Unit) {
        delay(1800)
        onContinue()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable(onClick = onContinue)
            .testTag("splash_screen")
    ) {
        // Center Brand Identity
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-40).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Leaf Icon matching Showcase Screen 1
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .scale(scale),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Main leaf shape
                    val leafPath = Path().apply {
                        moveTo(w * 0.5f, h * 0.15f)
                        cubicTo(w * 0.85f, h * 0.15f, w * 0.95f, h * 0.5f, w * 0.85f, h * 0.75f)
                        cubicTo(w * 0.75f, h * 0.9f, w * 0.5f, h * 0.95f, w * 0.35f, h * 0.9f)
                        cubicTo(w * 0.3f, h * 0.85f, w * 0.32f, h * 0.7f, w * 0.42f, h * 0.6f)
                        cubicTo(w * 0.55f, h * 0.45f, w * 0.35f, h * 0.35f, w * 0.35f, h * 0.35f)
                        cubicTo(w * 0.35f, h * 0.35f, w * 0.15f, h * 0.45f, w * 0.18f, h * 0.65f)
                        cubicTo(w * 0.2f, h * 0.75f, w * 0.12f, h * 0.85f, w * 0.12f, h * 0.85f)
                        cubicTo(w * 0.05f, h * 0.75f, w * 0.08f, h * 0.5f, w * 0.18f, h * 0.35f)
                        cubicTo(w * 0.3f, h * 0.15f, w * 0.5f, h * 0.15f, w * 0.5f, h * 0.15f)
                        close()
                    }

                    drawPath(
                        path = leafPath,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF34D399), Color(0xFF059669)),
                            start = Offset(0f, 0f),
                            end = Offset(w, h)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "MoneyFlow",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Neutral900,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Track. Save. Grow.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Neutral500,
                letterSpacing = 0.5.sp
            )
        }

        // Bottom Layered Mint Waves & Sprouting Plant
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .align(Alignment.BottomCenter)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Back wave (lighter mint)
                val backWave = Path().apply {
                    moveTo(0f, h * 0.55f)
                    cubicTo(w * 0.3f, h * 0.4f, w * 0.6f, h * 0.7f, w, h * 0.45f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(path = backWave, color = Color(0xFFECFDF5))

                // Front wave (subtle soft green)
                val frontWave = Path().apply {
                    moveTo(0f, h * 0.7f)
                    cubicTo(w * 0.4f, h * 0.85f, w * 0.7f, h * 0.55f, w, h * 0.65f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(path = frontWave, color = Color(0xFFD1FAE5).copy(alpha = 0.7f))
            }

            // Small sprouting plant in center bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = null,
                    tint = Emerald600,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
