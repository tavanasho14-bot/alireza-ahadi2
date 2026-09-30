package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.FairnessModel
import com.example.data.model.FundEntity
import com.example.ui.theme.AccentGold
import com.example.ui.theme.DarkSlate
import com.example.ui.theme.MintContainer
import com.example.ui.theme.MintLight
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceColor
import com.example.util.PersianUtils

@Composable
fun FundsListDialog(
    funds: List<FundEntity>,
    activeFundId: Long,
    onSelectFund: (Long) -> Unit,
    onCreateFund: (
        name: String,
        managerName: String,
        monthly: Long,
        memberCount: Int,
        basePayout: Long,
        fairnessModel: String,
        description: String
    ) -> Unit,
    onDismiss: () -> Unit
) {
    var isCreatingNew by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = SurfaceColor,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 20.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isCreatingNew) "ایجاد صندوق جدید" else "مدیریت صندوق‌ها (${PersianUtils.toPersianDigits(funds.size)} صندوق)",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkSlate
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "بستن", tint = SlateSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (!isCreatingNew) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .height(350.dp)
                    ) {
                        items(funds, key = { it.id }) { fund ->
                            val isSelected = fund.id == activeFundId
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) MintContainer else MintLight
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                                    .clickable {
                                        onSelectFund(fund.id)
                                        onDismiss()
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(PrimaryGreen),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.width(28.dp))
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = fund.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = DarkSlate
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "مدیر: ${fund.managerName} | ${PersianUtils.toPersianDigits(fund.memberCount)} عضو",
                                            fontSize = 12.sp,
                                            color = SlateSecondary
                                        )
                                        Text(
                                            text = "قسط ماهانه: ${PersianUtils.formatPrice(fund.monthlyInstallment)}",
                                            fontSize = 11.sp,
                                            color = PrimaryGreen
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { isCreatingNew = true },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("create_new_fund_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ایجاد صندوق جدید (تا ۲۰+ صندوق)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    // Create New Fund Form
                    CreateFundForm(
                        onCancel = { isCreatingNew = false },
                        onSubmit = { name, manager, monthly, count, base, model, desc ->
                            onCreateFund(name, manager, monthly, count, base, model, desc)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateFundForm(
    onCancel: () -> Unit,
    onSubmit: (
        name: String,
        managerName: String,
        monthly: Long,
        memberCount: Int,
        basePayout: Long,
        fairnessModel: String,
        description: String
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var managerName by remember { mutableStateOf("علیرضا احدی") }
    var monthlyStr by remember { mutableStateOf("10000000") }
    var memberCountStr by remember { mutableStateOf("20") }
    var basePayoutStr by remember { mutableStateOf("200000000") }
    var selectedModel by remember { mutableStateOf(FairnessModel.STEPPED_INCREASING.name) }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("نام صندوق *", fontSize = 12.sp) },
            placeholder = { Text("مثلاً: صندوق فامیلی احدی", fontSize = 11.sp) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = managerName,
            onValueChange = { managerName = it },
            label = { Text("نام مدیر صندوق", fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = memberCountStr,
                onValueChange = { memberCountStr = it },
                label = { Text("تعداد اعضا", fontSize = 11.sp) },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = monthlyStr,
                onValueChange = { monthlyStr = it },
                label = { Text("قسط ماهانه (تومان)", fontSize = 11.sp) },
                modifier = Modifier.weight(1.5f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = basePayoutStr,
            onValueChange = { basePayoutStr = it },
            label = { Text("مبلغ وام هر نوبت (تومان)", fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "الگوی ضدتورم و سودآوری:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = DarkSlate
        )
        Text(
            text = "پلکانی افزایشی (مبالغ نوبت‌های آخر بیشتر محاسبه می‌شود)",
            fontSize = 11.sp,
            color = PrimaryGreen
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) {
                Text("بازگشت")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val monthly = monthlyStr.toLongOrNull() ?: 10_000_000
                        val count = memberCountStr.toIntOrNull() ?: 20
                        val base = basePayoutStr.toLongOrNull() ?: (monthly * count)
                        onSubmit(name, managerName, monthly, count, base, selectedModel, description)
                    }
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("تأیید و ساخت صندوق")
            }
        }
    }
}
