package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGoldDark
import com.example.ui.theme.AccentGoldLight
import com.example.ui.theme.BorderColor
import com.example.ui.theme.DarkSlate
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.ErrorRedLight
import com.example.ui.theme.MintContainer
import com.example.ui.theme.MintLight
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenDark
import com.example.ui.theme.PrimaryGreenLight
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenLight
import com.example.ui.theme.SurfaceColor
import com.example.ui.viewmodel.AppScreen
import com.example.util.PersianUtils
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AppBottomNavigationBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    onOpenDrawer: () -> Unit
) {
    Surface(
        color = SurfaceColor,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBar(
            containerColor = SurfaceColor,
            tonalElevation = 0.dp
        ) {
            NavigationBarItem(
                selected = currentScreen == AppScreen.DASHBOARD,
                onClick = { onNavigate(AppScreen.DASHBOARD) },
                icon = { Icon(Icons.Default.Home, contentDescription = "خانه") },
                label = { Text("خانه", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                colors = navigationBarColors(),
                modifier = Modifier.testTag("nav_home")
            )
            NavigationBarItem(
                selected = currentScreen == AppScreen.MEMBERS,
                onClick = { onNavigate(AppScreen.MEMBERS) },
                icon = { Icon(Icons.Default.Group, contentDescription = "اعضا") },
                label = { Text("اعضا", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                colors = navigationBarColors(),
                modifier = Modifier.testTag("nav_members")
            )
            NavigationBarItem(
                selected = currentScreen == AppScreen.INSTALLMENTS,
                onClick = { onNavigate(AppScreen.INSTALLMENTS) },
                icon = { Icon(Icons.Default.DateRange, contentDescription = "اقساط") },
                label = { Text("اقساط", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                colors = navigationBarColors(),
                modifier = Modifier.testTag("nav_installments")
            )
            NavigationBarItem(
                selected = currentScreen == AppScreen.REPORTS,
                onClick = { onNavigate(AppScreen.REPORTS) },
                icon = { Icon(Icons.Default.Assessment, contentDescription = "گزارش‌ها") },
                label = { Text("گزارش‌ها", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                colors = navigationBarColors(),
                modifier = Modifier.testTag("nav_reports")
            )
            NavigationBarItem(
                selected = false,
                onClick = onOpenDrawer,
                icon = { Icon(Icons.Default.Menu, contentDescription = "بیشتر") },
                label = { Text("بیشتر", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                colors = navigationBarColors(),
                modifier = Modifier.testTag("nav_more")
            )
        }
    }
}

@Composable
private fun navigationBarColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = PrimaryGreen,
    selectedTextColor = PrimaryGreen,
    unselectedIconColor = SlateSecondary,
    unselectedTextColor = SlateSecondary,
    indicatorColor = MintContainer
)

private val avatarGradients = listOf(
    listOf(Color(0xFF0D9488), Color(0xFF042F2E)),
    listOf(Color(0xFFF59E0B), Color(0xFF78350F)),
    listOf(Color(0xFF6366F1), Color(0xFF312E81)),
    listOf(Color(0xFFEC4899), Color(0xFF831843)),
    listOf(Color(0xFF10B981), Color(0xFF064E3B)),
    listOf(Color(0xFF8B5CF6), Color(0xFF4C1D95))
)

@Composable
fun MemberAvatar(
    name: String,
    avatarId: Int = 0,
    photoUri: String? = null,
    size: Dp = 48.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (!photoUri.isNullOrEmpty()) {
            AsyncImage(
                model = photoUri,
                contentDescription = name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            val gradient = avatarGradients[avatarId % avatarGradients.size]
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.linearGradient(gradient)),
                contentAlignment = Alignment.Center
            ) {
                val initial = name.trim().firstOrNull()?.toString() ?: "ع"
                Text(
                    text = initial,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = (size.value * 0.42f).sp
                )
            }
        }
    }
}

@Composable
fun RoundBadge(
    roundNumber: Int?,
    modifier: Modifier = Modifier,
    isPaid: Boolean = false,
    size: Dp = 32.dp
) {
    val bgColor = if (isPaid) SuccessGreen else AccentGold
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (roundNumber != null) PersianUtils.toPersianDigits(roundNumber) else "-",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.45f).sp
        )
    }
}

@Composable
fun StatChip(
    count: String,
    label: String,
    countColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = PersianUtils.toPersianDigits(count),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = countColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                color = SlateSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun DonutChart(
    percentage: Float, // 0.0f to 1.0f
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 18.dp,
    paidColor: Color = SuccessGreen,
    unpaidColor: Color = ErrorRedLight
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            val diameter = size.minDimension - strokePx
            val topLeft = androidx.compose.ui.geometry.Offset(
                (size.width - diameter) / 2f,
                (size.height - diameter) / 2f
            )
            val arcSize = androidx.compose.ui.geometry.Size(diameter, diameter)

            // Background circle (unpaid portion)
            drawArc(
                color = unpaidColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Foreground arc (paid portion)
            drawArc(
                color = paidColor,
                startAngle = -90f,
                sweepAngle = 360f * percentage,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = PersianUtils.formatPercent((percentage * 100).toInt()),
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DarkSlate
            )
            Text(
                text = "پرداخت شده",
                fontSize = 11.sp,
                color = SlateSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun LotteryWheel(
    candidates: List<String>,
    onWinnerSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val isSpinning = remember { androidx.compose.runtime.mutableStateOf(false) }

    val wheelColors = listOf(
        Color(0xFF00594C),
        Color(0xFFF59E0B),
        Color(0xFF0F766E),
        Color(0xFFD97706),
        Color(0xFF10B981),
        Color(0xFF6366F1)
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(240.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val segmentCount = candidates.size.coerceAtLeast(1)
                val sweep = 360f / segmentCount
                val currentRot = rotation.value

                for (i in 0 until segmentCount) {
                    val color = wheelColors[i % wheelColors.size]
                    drawArc(
                        color = color,
                        startAngle = currentRot + (i * sweep),
                        sweepAngle = sweep,
                        useCenter = true
                    )
                }

                // Outer border
                drawCircle(
                    color = Color.White,
                    radius = size.minDimension / 2f,
                    style = Stroke(width = 6.dp.toPx())
                )
                // Center hub
                drawCircle(
                    color = Color.White,
                    radius = 24.dp.toPx()
                )
                drawCircle(
                    color = PrimaryGreen,
                    radius = 16.dp.toPx()
                )
            }

            // Indicator Needle at top
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(20.dp, 28.dp)
                    .background(Color.Red, RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        androidx.compose.material3.Button(
            onClick = {
                if (candidates.isEmpty() || isSpinning.value) return@Button
                isSpinning.value = true
                scope.launch {
                    val extraRounds = (4..8).random() * 360f
                    val randomOffset = (0..359).random().toFloat()
                    val targetRotation = rotation.value + extraRounds + randomOffset

                    rotation.animateTo(
                        targetValue = targetRotation,
                        animationSpec = tween(durationMillis = 3200, easing = FastOutSlowInEasing)
                    )

                    isSpinning.value = false
                    val segmentCount = candidates.size.coerceAtLeast(1)
                    val sweep = 360f / segmentCount
                    val finalAngle = (targetRotation % 360 + 360) % 360
                    // The needle is at 270 degrees (Top)
                    val normalizedAngle = (270f - finalAngle + 360f) % 360f
                    val index = ((normalizedAngle / sweep).toInt()) % segmentCount
                    onWinnerSelected(candidates[index])
                }
            },
            enabled = candidates.isNotEmpty() && !isSpinning.value,
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.testTag("spin_wheel_button")
        ) {
            Icon(Icons.Default.Star, contentDescription = null, tint = AccentGold)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                if (isSpinning.value) "در حال چرخش..." else "چرخاندن گردونه شانس",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}
