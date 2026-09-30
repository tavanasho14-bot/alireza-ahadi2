package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
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
import com.example.data.model.FundEntity
import com.example.data.model.InstallmentEntity
import com.example.data.model.MemberEntity
import com.example.ui.components.DonutChart
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BlueInfo
import com.example.ui.theme.BlueInfoLight
import com.example.ui.theme.DarkSlate
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.ErrorRedLight
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
fun ReportsScreen(
    fund: FundEntity?,
    members: List<MemberEntity>,
    installments: List<InstallmentEntity>,
    onBack: () -> Unit
) {
    if (fund == null) return

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("خلاصه", "پرداخت‌ها", "نمودارها")

    val totalMembers = members.size.coerceAtLeast(1)
    val paidMembersCount = members.count { it.isPaidCurrentMonth }
    val unpaidMembersCount = totalMembers - paidMembersCount
    val paidPercentage = paidMembersCount.toFloat() / totalMembers.toFloat()

    val totalPaidAmount = paidMembersCount * fund.monthlyInstallment
    val totalRemainingAmount = unpaidMembersCount * fund.monthlyInstallment

    // Anti-inflation stats
    val highestPayout = installments.maxOfOrNull { it.finalPayoutAmount } ?: fund.basePayoutAmount
    val bonusForLastWinner = (highestPayout - fund.basePayoutAmount).coerceAtLeast(0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "گزارش‌ها",
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

        // 3 Tabs: "خلاصه", "پرداخت‌ها", "نمودارها" (Matching screenshot 8)
        SecondaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = SurfaceColor,
            contentColor = PrimaryGreen,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Summary Cards (Matching screenshot 8)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Paid Card (Green - matching screenshot 8)
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = PrimaryGreen),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "پرداخت شده",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = SuccessGreenLight,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = PersianUtils.formatPrice(totalPaidAmount, includeToman = false),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "تومان",
                                fontSize = 11.sp,
                                color = MintContainer
                            )
                        }
                    }

                    // Remaining Card (Pink/Red - matching screenshot 8)
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = ErrorRedLight),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "باقیمانده",
                                    fontSize = 12.sp,
                                    color = ErrorRed,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    Icons.Default.HourglassBottom,
                                    contentDescription = null,
                                    tint = ErrorRed,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = PersianUtils.formatPrice(totalRemainingAmount, includeToman = false),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ErrorRed
                            )
                            Text(
                                text = "تومان",
                                fontSize = 11.sp,
                                color = ErrorRed.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            // Current Fund Balance Card (Blue tint / surface - matching screenshot 8)
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                    elevation = CardDefaults.cardElevation(1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(BlueInfoLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = BlueInfo,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "موجودی فعلی صندوق",
                                fontSize = 12.sp,
                                color = SlateSecondary,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = PersianUtils.formatPrice(fund.currentBalance),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkSlate
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Member Payment Status Donut Card (Matching screenshot 8)
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                    elevation = CardDefaults.cardElevation(1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "وضعیت پرداخت اعضا",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DarkSlate,
                            modifier = Modifier.align(Alignment.End)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Donut Chart
                            DonutChart(
                                percentage = paidPercentage,
                                modifier = Modifier.size(130.dp),
                                paidColor = SuccessGreen,
                                unpaidColor = ErrorRedLight
                            )

                            // Legend / breakdown
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(SuccessGreen)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "${PersianUtils.toPersianDigits(paidMembersCount)} نفر",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = DarkSlate
                                        )
                                        Text(
                                            text = "پرداخت شده",
                                            fontSize = 11.sp,
                                            color = SlateSecondary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(ErrorRed)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "${PersianUtils.toPersianDigits(unpaidMembersCount)} نفر",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = DarkSlate
                                        )
                                        Text(
                                            text = "باقیمانده",
                                            fontSize = 11.sp,
                                            color = SlateSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Fairness Profit & Protection Report (Proof that last members do not lose!)
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MintLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Shield,
                                contentDescription = null,
                                tint = PrimaryGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "گزارش سودآوری و عدالت مالی اعضا",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = PrimaryGreenDark
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "برای جبران تورم و حفظ ارزش پول، مبلغ پرداختی به نوبت بیستم به جای ${PersianUtils.formatPrice(fund.basePayoutAmount)} به میزان ${PersianUtils.formatPrice(highestPayout)} تعیین شده است (${PersianUtils.formatPrice(bonusForLastWinner)} سود مازاد).",
                            fontSize = 12.sp,
                            color = DarkSlate,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "ضریب رضایت اعضا:",
                                fontSize = 12.sp,
                                color = SlateSecondary
                            )
                            Text(
                                text = "۱۰۰٪ بدون ضرر مالی",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
