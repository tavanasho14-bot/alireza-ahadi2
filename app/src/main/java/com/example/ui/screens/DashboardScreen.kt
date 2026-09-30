package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FairnessModel
import com.example.data.model.FundEntity
import com.example.data.model.InstallmentEntity
import com.example.data.model.MemberEntity
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGoldDark
import com.example.ui.theme.AccentGoldLight
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BlueInfo
import com.example.ui.theme.BlueInfoLight
import com.example.ui.theme.BorderColor
import com.example.ui.theme.DarkSlate
import com.example.ui.theme.ErrorRed
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

@Composable
fun DashboardScreen(
    currentFund: FundEntity?,
    members: List<MemberEntity>,
    installments: List<InstallmentEntity>,
    isParticipantMode: Boolean,
    onNavigate: (AppScreen) -> Unit,
    onOpenFundSwitcher: () -> Unit,
    onOpenLottery: () -> Unit
) {
    val fund = currentFund ?: return

    val nextInstallment = installments.firstOrNull { !it.isPaidToRecipient }
    val nextRecipientName = nextInstallment?.recipientName ?: "مشخص نشده"
    val nextDueDate = nextInstallment?.dueDateJalali ?: fund.startDateJalali
    val activeRoundNum = nextInstallment?.roundNumber ?: fund.activeRound

    val fairnessModel = try {
        FairnessModel.valueOf(fund.fairnessModel)
    } catch (e: Exception) {
        FairnessModel.STEPPED_INCREASING
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(14.dp))

            // Top Header: Greeting & Notifications
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isParticipantMode) "سلام عضو گرامی 👋" else "سلام ${fund.managerName.split(" ").firstOrNull() ?: "علیرضا"} 👋",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkSlate
                    )
                    if (isParticipantMode) {
                        Text(
                            text = "حالت مشاهده شرکت‌کننده (دسترسی محدود)",
                            fontSize = 12.sp,
                            color = PrimaryGreenLight,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Switcher button for multi-funds
                    IconButton(
                        onClick = onOpenFundSwitcher,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MintContainer)
                            .testTag("fund_switcher_button")
                    ) {
                        Icon(
                            Icons.Default.SwapHoriz,
                            contentDescription = "تعویض صندوق",
                            tint = PrimaryGreen
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    BadgedBox(
                        badge = { Badge(containerColor = AccentGold) }
                    ) {
                        IconButton(
                            onClick = { onNavigate(AppScreen.FUND_DETAIL) },
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(SurfaceColor)
                        ) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "پیام‌ها",
                                tint = SlateSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Active Fund Hero Card (Matching screenshot 3)
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryGreen),
                elevation = CardDefaults.cardElevation(3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(AppScreen.FUND_DETAIL) }
                    .testTag("active_fund_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Badge "صندوق فعال"
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(SuccessGreen.copy(alpha = 0.25f))
                                .border(1.dp, SuccessGreen.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "صندوق فعال",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Fund Name & Manager
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = fund.name,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "مدیر: ${fund.managerName}",
                                color = MintContainer,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "کد دسترسی: ${fund.accessCode}",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )

                        Text(
                            text = "مشاهده جزئیات و قوانین ➔",
                            color = AccentGoldLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Stats Grid: 4 Cards (تعداد اعضا، مبلغ قسط ماهانه، موجودی صندوق، نوبت بعدی)
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Number of Members
                DashStatCard(
                    title = "تعداد اعضا",
                    value = PersianUtils.toPersianDigits(fund.memberCount),
                    subText = "نفر",
                    icon = Icons.Default.Group,
                    iconTint = PrimaryGreen,
                    iconBg = MintContainer,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Monthly installment
                DashStatCard(
                    title = "مبلغ قسط ماهانه",
                    value = PersianUtils.formatPrice(fund.monthlyInstallment, includeToman = false),
                    subText = "تومان",
                    icon = Icons.Default.CalendarMonth,
                    iconTint = AccentGoldDark,
                    iconBg = AccentGoldLight,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                // Next turn
                DashStatCard(
                    title = "نوبت بعدی",
                    value = "ماه ${PersianUtils.toPersianDigits(activeRoundNum)} $nextRecipientName",
                    subText = nextDueDate,
                    icon = Icons.Default.DateRange,
                    iconTint = ErrorRed,
                    iconBg = ErrorRed.copy(alpha = 0.1f),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Fund Balance
                DashStatCard(
                    title = "موجودی صندوق",
                    value = PersianUtils.formatPrice(fund.currentBalance, includeToman = false),
                    subText = "تومان",
                    icon = Icons.Default.TrendingUp,
                    iconTint = SuccessGreen,
                    iconBg = SuccessGreenLight,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Anti-Inflation Fairness Highlight Card (Key user requirement)
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MintLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(AppScreen.EDUCATION) }
                    .testTag("fairness_info_card")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(PrimaryGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Shield,
                            contentDescription = null,
                            tint = AccentGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "سیستم سودآوری عادلانه و جبران تورم",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = PrimaryGreenDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "الگوی ${fairnessModel.titleFa}: هیچ عضوی در نوبت‌های پایانی متضرر نمی‌شود.",
                            fontSize = 11.sp,
                            color = SlateSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // 4 Action Buttons (Grid from screenshot 3: اعضا، اقساط، گزارش‌ها، تنظیمات)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionCircleButton(
                    title = "اعضا",
                    icon = Icons.Default.Group,
                    color = PrimaryGreen,
                    bgColor = MintContainer,
                    onClick = { onNavigate(AppScreen.MEMBERS) },
                    tag = "action_members"
                )

                ActionCircleButton(
                    title = "اقساط",
                    icon = Icons.Default.DateRange,
                    color = AccentGoldDark,
                    bgColor = AccentGoldLight,
                    onClick = { onNavigate(AppScreen.INSTALLMENTS) },
                    tag = "action_installments"
                )

                ActionCircleButton(
                    title = "گزارش‌ها",
                    icon = Icons.Default.Assessment,
                    color = BlueInfo,
                    bgColor = BlueInfoLight,
                    onClick = { onNavigate(AppScreen.REPORTS) },
                    tag = "action_reports"
                )

                ActionCircleButton(
                    title = "تنظیمات",
                    icon = Icons.Default.Settings,
                    color = Color(0xFF7C3AED),
                    bgColor = Color(0xFFEDE9FE),
                    onClick = { onNavigate(AppScreen.SETTINGS) },
                    tag = "action_settings"
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        // Quick Lottery Button
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onOpenLottery)
                    .testTag("lottery_banner_card")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(AccentGoldLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Casino,
                                contentDescription = null,
                                tint = AccentGoldDark,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "گردونه قرعه‌کشی نوبت‌ها",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = DarkSlate
                            )
                            Text(
                                text = "تعیین شفاف و هیجان‌انگیز برندگان",
                                fontSize = 11.sp,
                                color = SlateSecondary
                            )
                        }
                    }

                    Button(
                        onClick = onOpenLottery,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("اجرا", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DashStatCard(
    title: String,
    value: String,
    subText: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = SlateSecondary,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DarkSlate,
                maxLines = 1
            )

            Text(
                text = subText,
                fontSize = 10.sp,
                color = SlateLight,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun ActionCircleButton(
    title: String,
    icon: ImageVector,
    color: Color,
    bgColor: Color,
    onClick: () -> Unit,
    tag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(tag)
    ) {
        Box(
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = DarkSlate
        )
    }
}
