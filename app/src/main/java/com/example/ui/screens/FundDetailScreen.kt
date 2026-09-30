package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
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
import com.example.ui.theme.DarkSlate
import com.example.ui.theme.MintContainer
import com.example.ui.theme.MintLight
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenDark
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceColor
import com.example.ui.viewmodel.AppScreen
import com.example.util.PersianUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundDetailScreen(
    fund: FundEntity?,
    members: List<MemberEntity>,
    installments: List<InstallmentEntity>,
    onBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToMembers: () -> Unit,
    onNavigateToInstallments: () -> Unit
) {
    if (fund == null) return
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("جزئیات", "اعضا", "اقساط", "قوانین")

    val fairnessModel = try {
        FairnessModel.valueOf(fund.fairnessModel)
    } catch (e: Exception) {
        FairnessModel.STEPPED_INCREASING
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = fund.name,
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

        // Fund Header Banner (Matching screenshot 4)
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceColor),
            elevation = CardDefaults.cardElevation(1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
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
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MintContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Savings,
                        contentDescription = null,
                        modifier = Modifier.size(34.dp),
                        tint = PrimaryGreen
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryGreen)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = fund.name,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "صندوق فعال",
                        color = SuccessGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tabs Row: جزئیات، اعضا، اقساط، قوانین
        SecondaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = SurfaceColor,
            contentColor = PrimaryGreen,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                        if (index == 1) onNavigateToMembers()
                        if (index == 2) onNavigateToInstallments()
                    },
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

        Spacer(modifier = Modifier.height(12.dp))

        // Content
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            if (selectedTab == 0) {
                // Details items (Screen 4)
                item {
                    DetailRowItem(
                        icon = Icons.Default.Person,
                        label = "نام صندوق",
                        value = fund.name
                    )
                    DetailRowItem(
                        icon = Icons.Default.Person,
                        label = "مدیر صندوق",
                        value = fund.managerName
                    )
                    DetailRowItem(
                        icon = Icons.Default.Group,
                        label = "تعداد اعضا",
                        value = "${PersianUtils.toPersianDigits(fund.memberCount)} نفر"
                    )
                    DetailRowItem(
                        icon = Icons.Default.Paid,
                        label = "مبلغ قسط ماهانه",
                        value = PersianUtils.formatPrice(fund.monthlyInstallment)
                    )
                    DetailRowItem(
                        icon = Icons.Default.AccountBalanceWallet,
                        label = "مبلغ پایه هر نوبت",
                        value = PersianUtils.formatPrice(fund.basePayoutAmount)
                    )
                    DetailRowItem(
                        icon = Icons.Default.Savings,
                        label = "موجودی فعلی صندوق",
                        value = PersianUtils.formatPrice(fund.currentBalance)
                    )
                    DetailRowItem(
                        icon = Icons.Default.CalendarMonth,
                        label = "شروع صندوق",
                        value = fund.startDateJalali
                    )
                    DetailRowItem(
                        icon = Icons.Default.Shield,
                        label = "الگوی ضدتورم",
                        value = fairnessModel.titleFa,
                        isHighlight = true
                    )
                }
            } else if (selectedTab == 3) {
                // Rules tab
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "الگوریتم سودآوری: ${fairnessModel.titleFa}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = PrimaryGreen
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = fairnessModel.fullExplanation,
                                fontSize = 13.sp,
                                color = DarkSlate,
                                lineHeight = 22.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "قوانین پرداخت و جریمه تأخیر:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = DarkSlate
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "• موعد واریز اقساط تا دهم هر ماه شمسی می‌باشد.\n• در صورت تأخیر، طبق توافق اعضا نوبت برنده به ماه بعد موکول می‌گردد.\n• معرفی معرف یا ضامن برای عضویت افراد الزامی است.",
                                fontSize = 12.sp,
                                color = SlateSecondary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Bottom Action Button: ویرایش تنظیمات (Matching screen 4)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                onClick = onNavigateToSettings,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("edit_settings_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ویرایش تنظیمات",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun DetailRowItem(
    icon: ImageVector,
    label: String,
    value: String,
    isHighlight: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = if (isHighlight) MintLight else SurfaceColor),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(if (isHighlight) PrimaryGreen else MintContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isHighlight) Color.White else PrimaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = label,
                    fontSize = 13.sp,
                    color = SlateSecondary,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isHighlight) PrimaryGreenDark else DarkSlate
            )
        }
    }
}
