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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGoldLight
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BlueInfo
import com.example.ui.theme.BlueInfoLight
import com.example.ui.theme.BorderColor
import com.example.ui.theme.DarkSlate
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.ErrorRedLight
import com.example.ui.theme.MintContainer
import com.example.ui.theme.MintLight
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenDark
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary
import com.example.ui.theme.SurfaceColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    fund: FundEntity?,
    isParticipantMode: Boolean,
    onToggleParticipantMode: (Boolean) -> Unit,
    onUpdateFund: (FundEntity) -> Unit,
    onArchiveFund: () -> Unit,
    onNavigateToMembers: () -> Unit,
    onNavigateToLottery: () -> Unit,
    onNavigateToEducation: () -> Unit,
    onBack: () -> Unit
) {
    if (fund == null) return

    var showEditInfoDialog by remember { mutableStateOf(false) }
    var showFairnessModelDialog by remember { mutableStateOf(false) }
    var showArchiveConfirmDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "تنظیمات صندوق",
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

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))

                // Setting Item 1: اطلاعات صندوق (Matching screenshot 9)
                SettingCardItem(
                    title = "اطلاعات صندوق",
                    subtitle = "ویرایش نام، مبلغ قسط و ...",
                    icon = Icons.Default.Edit,
                    iconBg = BlueInfoLight,
                    iconTint = BlueInfo,
                    onClick = { showEditInfoDialog = true },
                    tag = "setting_edit_info"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Setting Item 2: قوانین نوبت‌دهی (Matching screenshot 9)
                SettingCardItem(
                    title = "قوانین نوبت‌دهی و قرعه‌کشی",
                    subtitle = "اجرای گردونه شانس و قرعه‌کشی دستی نوبت‌ها",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    iconBg = MintContainer,
                    iconTint = PrimaryGreen,
                    onClick = onNavigateToLottery,
                    tag = "setting_lottery_rules"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Setting Item 3: مدل سودآوری و عدالت مالی (Key feature)
                SettingCardItem(
                    title = "مدل سودآوری و عدالت مالی",
                    subtitle = "انتخاب سازوکار جبران تورم برای نفرات آخر",
                    icon = Icons.Default.Shield,
                    iconBg = AccentGoldLight,
                    iconTint = AccentGold,
                    onClick = { showFairnessModelDialog = true },
                    tag = "setting_fairness_model"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Setting Item 4: مدیریت اعضا (Matching screenshot 9)
                SettingCardItem(
                    title = "مدیریت اعضا",
                    subtitle = "افزودن، ویرایش و حذف اعضا",
                    icon = Icons.Default.Group,
                    iconBg = Color(0xFFFEF3C7),
                    iconTint = Color(0xFFD97706),
                    onClick = onNavigateToMembers,
                    tag = "setting_members_management"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Setting Item 5: دسترسی محدود شرکت‌کنندگان (User requirement)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                    elevation = CardDefaults.cardElevation(0.5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = isParticipantMode,
                            onCheckedChange = onToggleParticipantMode,
                            colors = SwitchDefaults.colors(checkedThumbColor = PrimaryGreen, checkedTrackColor = MintContainer)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "حالت دسترسی شرکت‌کنندگان",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = DarkSlate
                                )
                                Text(
                                    text = "محدود کردن دسترسی به فقط مشاهده گزارش و نوبت‌ها",
                                    fontSize = 11.sp,
                                    color = SlateSecondary
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEDE9FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Security,
                                    contentDescription = null,
                                    tint = Color(0xFF7C3AED),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Archive Button: بایگانی صندوق (Matching screenshot 9)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ErrorRedLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showArchiveConfirmDialog = true }
                        .testTag("archive_fund_button")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = ErrorRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "بایگانی صندوق",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = ErrorRed
                        )
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Dialog: Edit Fund Info
    if (showEditInfoDialog) {
        var name by remember { mutableStateOf(fund.name) }
        var manager by remember { mutableStateOf(fund.managerName) }
        var monthlyStr by remember { mutableStateOf(fund.monthlyInstallment.toString()) }
        var baseStr by remember { mutableStateOf(fund.basePayoutAmount.toString()) }

        AlertDialog(
            onDismissRequest = { showEditInfoDialog = false },
            title = { Text("ویرایش اطلاعات صندوق", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("نام صندوق") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = manager,
                        onValueChange = { manager = it },
                        label = { Text("نام مدیر") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = monthlyStr,
                        onValueChange = { monthlyStr = it },
                        label = { Text("مبلغ قسط ماهانه (تومان)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = baseStr,
                        onValueChange = { baseStr = it },
                        label = { Text("مبلغ پایه هر نوبت (تومان)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val monthly = monthlyStr.toLongOrNull() ?: fund.monthlyInstallment
                        val base = baseStr.toLongOrNull() ?: fund.basePayoutAmount
                        onUpdateFund(
                            fund.copy(
                                name = name,
                                managerName = manager,
                                monthlyInstallment = monthly,
                                basePayoutAmount = base
                            )
                        )
                        showEditInfoDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("ذخیره")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditInfoDialog = false }) {
                    Text("انصراف")
                }
            }
        )
    }

    // Dialog: Choose Fairness Model
    if (showFairnessModelDialog) {
        var selectedModel by remember { mutableStateOf(fund.fairnessModel) }

        AlertDialog(
            onDismissRequest = { showFairnessModelDialog = false },
            title = { Text("انتخاب مدل سودآوری و عدالت مالی", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    FairnessModel.entries.forEach { model ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedModel = model.name }
                                .padding(vertical = 6.dp)
                        ) {
                            RadioButton(
                                selected = selectedModel == model.name,
                                onClick = { selectedModel = model.name },
                                colors = RadioButtonDefaults.colors(selectedColor = PrimaryGreen)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = model.titleFa,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = DarkSlate
                                )
                                Text(
                                    text = model.shortDesc,
                                    fontSize = 11.sp,
                                    color = SlateSecondary
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateFund(fund.copy(fairnessModel = selectedModel))
                        showFairnessModelDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("اعمال و محاسبه مجدد")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFairnessModelDialog = false }) {
                    Text("انصراف")
                }
            }
        )
    }

    // Dialog: Confirm Archive
    if (showArchiveConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showArchiveConfirmDialog = false },
            title = { Text("آیا مطمئن هستید؟", color = ErrorRed, fontWeight = FontWeight.Bold) },
            text = {
                Text("صندوق ${fund.name} بایگانی خواهد شد و از لیست صندوق‌های فعال حذف می‌گردد.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showArchiveConfirmDialog = false
                        onArchiveFund()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) {
                    Text("بایگانی شود")
                }
            },
            dismissButton = {
                TextButton(onClick = { showArchiveConfirmDialog = false }) {
                    Text("انصراف")
                }
            }
        )
    }
}

@Composable
private fun SettingCardItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    onClick: () -> Unit,
    tag: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        elevation = CardDefaults.cardElevation(0.5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(tag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = SlateLight,
                modifier = Modifier.size(18.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = DarkSlate
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = SlateSecondary
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
