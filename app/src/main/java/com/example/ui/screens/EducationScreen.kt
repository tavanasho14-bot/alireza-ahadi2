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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FairnessModel
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGoldDark
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.DarkSlate
import com.example.ui.theme.MintContainer
import com.example.ui.theme.MintLight
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenDark
import com.example.ui.theme.SlateSecondary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationScreen(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "آموزش و عدالت مالی صندوق",
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
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))

                // Hero Card: Why Anti-Inflation System is needed
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MintContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.School,
                                    contentDescription = null,
                                    tint = PrimaryGreen,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "چگونه همه افراد سود می‌کنند؟",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "در صندوق‌های سنتی، نفر اول برنده بزرگ است چون وام بدون بهره فوری می‌گیرد، اما نفر آخر ارزش پولش با تورم کاهش می‌یابد. اپلیکیشن صندوق همیار با ۵ روش هوشمند، این نابرابری را به صفر رسانده تا نفر آخر بیشترین پاداش نقدی را دریافت کند.",
                            color = Color.White.copy(alpha = 0.95f),
                            fontSize = 13.sp,
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "الگوهای ۵‌گانه سودآوری عادلانه در همیار:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DarkSlate
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            items(FairnessModel.entries) { model ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                    elevation = CardDefaults.cardElevation(1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Shield,
                                contentDescription = null,
                                tint = PrimaryGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = model.titleFa,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = PrimaryGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = model.fullExplanation,
                            fontSize = 12.sp,
                            color = DarkSlate,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Tips for Managers Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MintLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "نکات مهم مدیریت شفاف صندوق:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = PrimaryGreenDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• برای هر عضو حتماً معرف یا ضامن معتبر ثبت نمایید.\n• رسید واریز اقساط را پس از هر پرداخت بررسی فرمایید.\n• از حالت «دسترسی شرکت‌کننده» برای اشتراک‌گذاری شفاف گزارش‌ها بدون امکان دستکاری استفاده نمایید.",
                            fontSize = 12.sp,
                            color = DarkSlate,
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
