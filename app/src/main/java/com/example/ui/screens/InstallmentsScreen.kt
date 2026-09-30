package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InstallmentEntity
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGoldDark
import com.example.ui.theme.AccentGoldLight
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BorderColor
import com.example.ui.theme.DarkSlate
import com.example.ui.theme.MintContainer
import com.example.ui.theme.MintLight
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenDark
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenLight
import com.example.ui.theme.SurfaceColor
import com.example.util.PersianUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstallmentsScreen(
    installments: List<InstallmentEntity>,
    onBack: () -> Unit
) {
    var viewModeIndex by remember { mutableIntStateOf(0) }
    val viewOptions = listOf("نمای لیست", "نمای تقویم")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "اقساط",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DarkSlate
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "بازگشت",
                        tint = DarkSlate
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
        )

        // View Mode Switcher: "نمای لیست" / "نمای تقویم" (Matching screenshot 6)
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            viewOptions.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = viewOptions.size),
                    onClick = { viewModeIndex = index },
                    selected = index == viewModeIndex,
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MintContainer,
                        activeContentColor = PrimaryGreen,
                        inactiveContainerColor = SurfaceColor,
                        inactiveContentColor = SlateSecondary
                    )
                ) {
                    Text(
                        text = label,
                        fontSize = 13.sp,
                        fontWeight = if (index == viewModeIndex) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Fairness note
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MintLight),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "مبالغ پرداختی با الگوی ضدتورم محاسبه شده و برای نوبت‌های بعدی افزایش یافته است.",
                    fontSize = 11.sp,
                    color = PrimaryGreenDark,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Timeline of Installments (Matching screenshot 6)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            itemsIndexed(installments, key = { _, item -> item.id }) { index, item ->
                TimelineInstallmentItem(
                    installment = item,
                    isFirst = index == 0,
                    isLast = index == installments.lastIndex
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun TimelineInstallmentItem(
    installment: InstallmentEntity,
    isFirst: Boolean,
    isLast: Boolean
) {
    val roundBadgeColors = listOf(
        SuccessGreen,
        AccentGold,
        Color(0xFF6366F1),
        SlateSecondary,
        Color(0xFFEC4899),
        Color(0xFF0F766E)
    )
    val badgeColor = roundBadgeColors[(installment.roundNumber - 1) % roundBadgeColors.size]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
    ) {
        // Timeline Column (Badge & Connecting Line)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(44.dp)
        ) {
            // Top connecting line
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(10.dp)
                    .background(if (isFirst) Color.Transparent else BorderColor)
            )

            // Number Badge (1, 2, 3...)
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = PersianUtils.toPersianDigits(installment.roundNumber),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            // Bottom connecting line
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(if (isLast) Color.Transparent else BorderColor)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Installment Details Card (Matching screenshot 6)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceColor),
            elevation = CardDefaults.cardElevation(1.dp),
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status Pill (Left in RTL, Right in LTR)
                Column(horizontalAlignment = Alignment.Start) {
                    if (installment.isPaidToRecipient) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(SuccessGreenLight)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "پرداخت شده",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(BackgroundColor)
                                .border(1.dp, BorderColor, RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.RadioButtonUnchecked,
                                    contentDescription = null,
                                    tint = SlateSecondary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "برنامه‌ریزی شده",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = SlateSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    if (installment.fairnessAdjustment > 0) {
                        Text(
                            text = "+${PersianUtils.formatPrice(installment.fairnessAdjustment, includeToman = false)} پاداش",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }
                }

                // Month title, Recipient, Date and Amount
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = PersianUtils.formatPrice(installment.finalPayoutAmount),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkSlate
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "ماه ${PersianUtils.toPersianDigits(installment.roundNumber)} ${installment.recipientName}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkSlate
                    )
                    Text(
                        text = installment.dueDateJalali,
                        fontSize = 10.sp,
                        color = SlateLight
                    )
                }
            }
        }
    }
}
