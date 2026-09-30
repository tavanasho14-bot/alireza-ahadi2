package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGoldDark
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

@Composable
fun WelcomeScreen(
    onEnterApp: () -> Unit,
    onLearnMore: () -> Unit
) {
    var step by remember { mutableStateOf(1) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundColor
    ) {
        if (step == 1) {
            // Screen 1: Splash / Onboarding Intro
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Brand Emblem
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(MintContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = "صندوق همیار",
                            modifier = Modifier.size(52.dp),
                            tint = PrimaryGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "صندوق همیار",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryGreen
                    )
                    Text(
                        text = "با هم برای فردای بهتر",
                        fontSize = 15.sp,
                        color = SlateSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Decorative Center Card with Plant / Prosperity Graphic
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(MintLight, MintContainer)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = null,
                                modifier = Modifier.size(72.dp),
                                tint = PrimaryGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "مدیریت ساده و شفاف\nصندوق‌های جمعی خانوادگی و دوستانه",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkSlate,
                            textAlign = TextAlign.Center,
                            lineHeight = 26.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "با سیستم هوشمند ضدتورم تا همه اعضا سودمند باشند",
                            fontSize = 12.sp,
                            color = PrimaryGreen,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Bottom Pagination & Button
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Dot indicators
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(PrimaryGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        repeat(4) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(SlateLight.copy(alpha = 0.4f))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                    }

                    Button(
                        onClick = { step = 2 },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("start_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        Text(
                            text = "شروع کنید",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        } else {
            // Screen 2: Features Checklist Screen (matches screenshot 2)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Illustration Banner
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MintContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                modifier = Modifier.size(54.dp),
                                tint = PrimaryGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "صندوق همیار",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "مدیریت صندوق‌های جمعی\nساده، شفاف و مطمئن",
                            fontSize = 15.sp,
                            color = SlateSecondary,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Features Checklist
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        FeatureCheckItem(text = "ثبت اعضا و مدیریت اقساط")
                        Spacer(modifier = Modifier.height(12.dp))
                        FeatureCheckItem(text = "پیگیری پرداخت‌ها و نوبت‌ها")
                        Spacer(modifier = Modifier.height(12.dp))
                        FeatureCheckItem(text = "گزارش‌های کامل و شفاف")
                        Spacer(modifier = Modifier.height(12.dp))
                        FeatureCheckItem(text = "مناسب خانواده، دوستان و همکاران")
                        Spacer(modifier = Modifier.height(12.dp))
                        FeatureCheckItem(
                            text = "مدل‌های عادلانه سودآوری (بدون ضرر نفر آخر)",
                            isHighlight = true
                        )
                    }
                }

                // Action Buttons
                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onEnterApp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("enter_app_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        Text(
                            text = "ورود به برنامه",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = onLearnMore,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("learn_more_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryGreen)
                    ) {
                        Text(
                            text = "آشنایی بیشتر با سیستم ضدتورم",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun FeatureCheckItem(text: String, isHighlight: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = if (isHighlight) AccentGoldDark else SuccessGreen,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlight) PrimaryGreenDark else DarkSlate
        )
    }
}
