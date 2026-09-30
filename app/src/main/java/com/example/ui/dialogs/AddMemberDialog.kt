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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.components.MemberAvatar
import com.example.ui.theme.BorderColor
import com.example.ui.theme.DarkSlate
import com.example.ui.theme.MintContainer
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary
import com.example.ui.theme.SurfaceColor

@Composable
fun AddMemberDialog(
    onDismiss: () -> Unit,
    onConfirm: (
        name: String,
        familyName: String,
        phone: String,
        recommender: String,
        isManager: Boolean,
        roundNumber: Int?
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var familyName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var recommenderName by remember { mutableStateOf("") }
    var roundStr by remember { mutableStateOf("") }
    var isManager by remember { mutableStateOf(false) }
    var avatarId by remember { mutableStateOf(0) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = SurfaceColor,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with title and close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "افزودن عضو جدید",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkSlate
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "بستن", tint = SlateSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Avatar Selector (Matching screenshot 7)
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clickable { avatarId = (avatarId + 1) % 6 },
                    contentAlignment = Alignment.Center
                ) {
                    MemberAvatar(
                        name = if (name.isNotEmpty()) name else "عضو",
                        avatarId = avatarId,
                        size = 80.dp
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(PrimaryGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "تغییر آواتار",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Member Name Field (Required)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("نام عضو *", fontSize = 12.sp) },
                    placeholder = { Text("مثلاً: علیرضا", fontSize = 12.sp, color = SlateLight) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = textFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("member_name_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Family Name (Optional)
                OutlinedTextField(
                    value = familyName,
                    onValueChange = { familyName = it },
                    label = { Text("نام خانوادگی (اختیاری)", fontSize = 12.sp) },
                    placeholder = { Text("مثلاً: احدی", fontSize = 12.sp, color = SlateLight) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Phone number (Optional)
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("شماره تماس (اختیاری)", fontSize = 12.sp) },
                    placeholder = { Text("۰۹۱۲۰۰۰۰۰۰۰", fontSize = 12.sp, color = SlateLight) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Recommender / Guarantor (معرف / ضامن)
                OutlinedTextField(
                    value = recommenderName,
                    onValueChange = { recommenderName = it },
                    label = { Text("نام معرف یا ضامن (اختیاری)", fontSize = 12.sp) },
                    placeholder = { Text("مثلاً: حاج احمد", fontSize = 12.sp, color = SlateLight) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Round number (Optional)
                OutlinedTextField(
                    value = roundStr,
                    onValueChange = { roundStr = it },
                    label = { Text("نوبت پیشنهادی (اختیاری)", fontSize = 12.sp) },
                    placeholder = { Text("خالی بگذارید تا با قرعه‌کشی تعیین شود", fontSize = 11.sp, color = SlateLight) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Is Manager Checkbox (Matching screenshot 7)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MintContainer.copy(alpha = 0.5f))
                        .clickable { isManager = !isManager }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isManager,
                        onCheckedChange = { isManager = it },
                        colors = CheckboxDefaults.colors(checkedColor = PrimaryGreen)
                    )
                    Text(
                        text = "این عضو مدیر صندوق است",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkSlate
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            val parsedRound = roundStr.toIntOrNull()
                            onConfirm(name, familyName, phoneNumber, recommenderName, isManager, parsedRound)
                        }
                    },
                    enabled = name.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_member_button")
                ) {
                    Text(
                        text = "ثبت عضو",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = SurfaceColor,
    unfocusedContainerColor = SurfaceColor,
    focusedBorderColor = PrimaryGreen,
    unfocusedBorderColor = BorderColor
)
