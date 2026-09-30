package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
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
import com.example.ui.theme.DarkSlate
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.MintContainer
import com.example.ui.theme.MintLight
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateSecondary
import com.example.ui.theme.SurfaceColor
import com.example.ui.viewmodel.AppScreen

@Composable
fun AppDrawerContent(
    currentScreen: AppScreen,
    managerName: String,
    isParticipantMode: Boolean,
    onNavigate: (AppScreen) -> Unit,
    onOpenFundsList: () -> Unit,
    onToggleParticipantMode: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = SurfaceColor,
        modifier = Modifier.width(310.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Header (Matching screenshot 10)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(MintContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = "صندوق همیار",
                            tint = PrimaryGreen,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "صندوق همیار",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryGreen
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Profile Card (Matching screenshot 10)
                CardProfile(
                    name = managerName,
                    role = if (isParticipantMode) "شرکت‌کننده (مشاهده‌گر)" else "مدیر صندوق",
                    avatarId = 0
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Menu items
                DrawerMenuItem(
                    icon = Icons.Default.Home,
                    label = "خانه",
                    selected = currentScreen == AppScreen.DASHBOARD,
                    onClick = {
                        onNavigate(AppScreen.DASHBOARD)
                        onCloseDrawer()
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.FolderShared,
                    label = "صندوق‌های من",
                    selected = false,
                    onClick = {
                        onCloseDrawer()
                        onOpenFundsList()
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.Casino,
                    label = "قرعه‌کشی نوبت‌ها",
                    selected = currentScreen == AppScreen.LOTTERY,
                    onClick = {
                        onNavigate(AppScreen.LOTTERY)
                        onCloseDrawer()
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.Assessment,
                    label = "گزارش‌ها",
                    selected = currentScreen == AppScreen.REPORTS,
                    onClick = {
                        onNavigate(AppScreen.REPORTS)
                        onCloseDrawer()
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.School,
                    label = "آموزش و سیستم ضدتورم",
                    selected = currentScreen == AppScreen.EDUCATION,
                    onClick = {
                        onNavigate(AppScreen.EDUCATION)
                        onCloseDrawer()
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.Settings,
                    label = "تنظیمات",
                    selected = currentScreen == AppScreen.SETTINGS,
                    onClick = {
                        onNavigate(AppScreen.SETTINGS)
                        onCloseDrawer()
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.Security,
                    label = if (isParticipantMode) "تغییر به حالت مدیر" else "تغییر به حالت شرکت‌کننده",
                    selected = false,
                    onClick = {
                        onToggleParticipantMode()
                        onCloseDrawer()
                    }
                )

                Divider(
                    color = MintContainer,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                DrawerMenuItem(
                    icon = Icons.Default.Info,
                    label = "درباره برنامه",
                    selected = false,
                    onClick = {
                        onNavigate(AppScreen.WELCOME)
                        onCloseDrawer()
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Default.HelpOutline,
                    label = "راهنما",
                    selected = false,
                    onClick = {
                        onNavigate(AppScreen.EDUCATION)
                        onCloseDrawer()
                    }
                )
            }

            // Exit / Close Drawer
            DrawerMenuItem(
                icon = Icons.Default.ExitToApp,
                label = "بستن منو",
                selected = false,
                textColor = ErrorRed,
                iconColor = ErrorRed,
                onClick = onCloseDrawer
            )
        }
    }
}

@Composable
private fun CardProfile(
    name: String,
    role: String,
    avatarId: Int
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MintLight,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSlate
                )
                Text(
                    text = role,
                    fontSize = 11.sp,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Medium
                )
            }

            MemberAvatar(
                name = name,
                avatarId = avatarId,
                size = 46.dp
            )
        }
    }
}

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    textColor: Color = DarkSlate,
    iconColor: Color = PrimaryGreen,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) PrimaryGreen else iconColor,
                modifier = Modifier.size(22.dp)
            )
        },
        label = {
            Text(
                text = label,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.sp,
                color = if (selected) PrimaryGreen else textColor
            )
        },
        selected = selected,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MintContainer,
            unselectedContainerColor = Color.Transparent
        ),
        modifier = Modifier.padding(vertical = 2.dp)
    )
}
