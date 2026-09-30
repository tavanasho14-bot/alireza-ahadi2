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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MemberEntity
import com.example.ui.components.MemberAvatar
import com.example.ui.components.RoundBadge
import com.example.ui.components.StatChip
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGoldDark
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BorderColor
import com.example.ui.theme.DarkSlate
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.ErrorRedLight
import com.example.ui.theme.MintContainer
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenLight
import com.example.ui.theme.SurfaceColor
import com.example.util.PersianUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembersScreen(
    members: List<MemberEntity>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onTogglePayment: (MemberEntity) -> Unit,
    onDeleteMember: (MemberEntity) -> Unit,
    onOpenAddMember: () -> Unit,
    onBack: () -> Unit,
    isParticipantMode: Boolean
) {
    val totalCount = members.size
    val paidCount = members.count { it.isPaidCurrentMonth }
    val unpaidCount = totalCount - paidCount

    var filterType by remember { mutableStateOf("ALL") }

    val filteredMembers = members.filter {
        val matchesSearch = it.name.contains(searchQuery, ignoreCase = true) ||
                it.familyName.contains(searchQuery, ignoreCase = true) ||
                it.phoneNumber.contains(searchQuery)
        val matchesFilter = when (filterType) {
            "PAID" -> it.isPaidCurrentMonth
            "UNPAID" -> !it.isPaidCurrentMonth
            else -> true
        }
        matchesSearch && matchesFilter
    }

    Box(modifier = Modifier.fillMaxSize().background(BackgroundColor)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top App Bar
            TopAppBar(
                title = {
                    Text(
                        text = "اعضا",
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

            // Search Bar (Matching screenshot 5)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = {
                    Text("جستجوی نام عضو ...", fontSize = 13.sp, color = SlateLight)
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = SlateSecondary)
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceColor,
                    unfocusedContainerColor = SurfaceColor,
                    focusedBorderColor = PrimaryGreen,
                    unfocusedBorderColor = BorderColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("members_search_input")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 3 Stat Chips: کل اعضا، پرداخت کرده‌اند، باقیمانده (Matching screenshot 5)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatChip(
                    count = totalCount.toString(),
                    label = "کل اعضا",
                    countColor = DarkSlate,
                    bgColor = SurfaceColor,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { filterType = "ALL" }
                )

                StatChip(
                    count = paidCount.toString(),
                    label = "پرداخت کرده‌اند",
                    countColor = SuccessGreen,
                    bgColor = SuccessGreenLight,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { filterType = "PAID" }
                )

                StatChip(
                    count = unpaidCount.toString(),
                    label = "باقیمانده",
                    countColor = ErrorRed,
                    bgColor = ErrorRedLight,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { filterType = "UNPAID" }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Members List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                items(filteredMembers, key = { it.id }) { member ->
                    MemberRowItem(
                        member = member,
                        isParticipantMode = isParticipantMode,
                        onTogglePayment = { onTogglePayment(member) },
                        onDelete = { onDeleteMember(member) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        // Floating Action Button: + افزودن عضو (Matching screenshot 5)
        if (!isParticipantMode) {
            Button(
                onClick = onOpenAddMember,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 18.dp)
                    .height(52.dp)
                    .testTag("add_member_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "افزودن عضو",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun MemberRowItem(
    member: MemberEntity,
    isParticipantMode: Boolean,
    onTogglePayment: () -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        elevation = CardDefaults.cardElevation(0.5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Options Menu (Left in RTL, Right in LTR)
            Box {
                if (!isParticipantMode) {
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "گزینه‌ها",
                            tint = SlateLight
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    if (member.isPaidCurrentMonth) "علامت‌گذاری به عنوان پرداخت‌نشده" else "ثبت پرداخت قسط این ماه"
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                onTogglePayment()
                            },
                            leadingIcon = {
                                Icon(
                                    if (member.isPaidCurrentMonth) Icons.Default.Close else Icons.Default.Check,
                                    contentDescription = null
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("حذف عضو", color = ErrorRed) },
                            onClick = {
                                menuExpanded = false
                                onDelete()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = ErrorRed)
                            }
                        )
                    }
                }
            }

            // Member Info & Avatar & Round Badge (Matching screenshot 5)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${member.name} ${member.familyName}".trim(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = DarkSlate
                    )
                    Spacer(modifier = Modifier.height(2.dp))

                    val roundText = if (member.roundNumber != null) "نوبت ماه ${PersianUtils.toPersianDigits(member.roundNumber)}" else "نوبت تعیین‌نشده"
                    val statusText = if (member.isPaidCurrentMonth) "پرداخت شده" else "در انتظار"
                    val statusColor = if (member.isPaidCurrentMonth) SuccessGreen else AccentGoldDark

                    Text(
                        text = "$roundText | $statusText",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = statusColor
                    )

                    if (member.recommenderName.isNotEmpty()) {
                        Text(
                            text = "معرف: ${member.recommenderName}",
                            fontSize = 10.sp,
                            color = SlateLight
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Avatar
                MemberAvatar(
                    name = member.name,
                    avatarId = member.avatarId,
                    photoUri = member.photoUri,
                    size = 42.dp
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Round Number Badge
                RoundBadge(
                    roundNumber = member.roundNumber,
                    isPaid = member.isPaidCurrentMonth,
                    size = 28.dp
                )
            }
        }
    }
}
