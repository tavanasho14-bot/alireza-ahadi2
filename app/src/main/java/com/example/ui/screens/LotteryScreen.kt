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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MemberEntity
import com.example.ui.components.LotteryWheel
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGoldLight
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
fun LotteryScreen(
    members: List<MemberEntity>,
    onPerformFullLottery: () -> Unit,
    onBack: () -> Unit
) {
    val candidateNames = remember(members) {
        val unassigned = members.filter { it.roundNumber == null || !it.hasWon }.map { "${it.name} ${it.familyName}".trim() }
        if (unassigned.isNotEmpty()) unassigned else members.map { "${it.name} ${it.familyName}".trim() }
    }

    var selectedWinnerName by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "گردونه قرعه‌کشی نوبت‌ها",
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Description
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MintLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(PrimaryGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Casino,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "قرعه‌کشی کاملاً تصادفی، شفاف و عادلانه بین اعضای صندوق",
                        fontSize = 12.sp,
                        color = PrimaryGreenDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Wheel Component
            LotteryWheel(
                candidates = candidateNames,
                onWinnerSelected = { winner ->
                    selectedWinnerName = winner
                },
                modifier = Modifier.weight(1f)
            )

            // Winner Announcement Card
            if (selectedWinnerName != null) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                    elevation = CardDefaults.cardElevation(3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = AccentGold,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "🎉 برنده نوبت قرعه‌کشی:",
                            fontSize = 13.sp,
                            color = SlateSecondary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = selectedWinnerName ?: "",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryGreen
                        )
                    }
                }
            }

            // Bottom Batch Lottery Button
            Button(
                onClick = onPerformFullLottery,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("full_lottery_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Icon(Icons.Default.Shuffle, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "قرعه‌کشی و تخصیص خودکار همه نوبت‌ها",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
