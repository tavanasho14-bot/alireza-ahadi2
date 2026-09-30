package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AppBottomNavigationBar
import com.example.ui.components.AppDrawerContent
import com.example.ui.dialogs.AddMemberDialog
import com.example.ui.dialogs.FundsListDialog
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EducationScreen
import com.example.ui.screens.FundDetailScreen
import com.example.ui.screens.InstallmentsScreen
import com.example.ui.screens.LotteryScreen
import com.example.ui.screens.MembersScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.SandoghTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.FundViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: FundViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SandoghTheme {
                // Ensure Persian RTL layout for authentic Iranian app experience
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    SandoghApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun SandoghApp(viewModel: FundViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val activeFundId by viewModel.activeFundId.collectAsStateWithLifecycle()
    val currentFund by viewModel.currentFund.collectAsStateWithLifecycle()
    val activeFunds by viewModel.activeFunds.collectAsStateWithLifecycle()
    val members by viewModel.members.collectAsStateWithLifecycle()
    val installments by viewModel.installments.collectAsStateWithLifecycle()
    val isParticipantMode by viewModel.isParticipantMode.collectAsStateWithLifecycle()
    val memberSearchQuery by viewModel.memberSearchQuery.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showAddMemberDialog by remember { mutableStateOf(false) }
    var showFundsListDialog by remember { mutableStateOf(false) }

    // Handle Back Press
    BackHandler(enabled = currentScreen != AppScreen.DASHBOARD && currentScreen != AppScreen.WELCOME) {
        viewModel.navigateTo(AppScreen.DASHBOARD)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentScreen != AppScreen.WELCOME,
        drawerContent = {
            AppDrawerContent(
                currentScreen = currentScreen,
                managerName = currentFund?.managerName ?: "علیرضا احدی",
                isParticipantMode = isParticipantMode,
                onNavigate = { screen -> viewModel.navigateTo(screen) },
                onOpenFundsList = { showFundsListDialog = true },
                onToggleParticipantMode = {
                    viewModel.setParticipantMode(!isParticipantMode)
                },
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        val showBottomNav = currentScreen in listOf(
            AppScreen.DASHBOARD,
            AppScreen.MEMBERS,
            AppScreen.INSTALLMENTS,
            AppScreen.REPORTS,
            AppScreen.SETTINGS
        )

        Scaffold(
            bottomBar = {
                if (showBottomNav) {
                    AppBottomNavigationBar(
                        currentScreen = currentScreen,
                        onNavigate = { screen -> viewModel.navigateTo(screen) },
                        onOpenDrawer = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentScreen) {
                    AppScreen.WELCOME -> {
                        WelcomeScreen(
                            onEnterApp = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                            onLearnMore = { viewModel.navigateTo(AppScreen.EDUCATION) }
                        )
                    }

                    AppScreen.DASHBOARD -> {
                        DashboardScreen(
                            currentFund = currentFund,
                            members = members,
                            installments = installments,
                            isParticipantMode = isParticipantMode,
                            onNavigate = { screen -> viewModel.navigateTo(screen) },
                            onOpenFundSwitcher = { showFundsListDialog = true },
                            onOpenLottery = { viewModel.navigateTo(AppScreen.LOTTERY) }
                        )
                    }

                    AppScreen.FUND_DETAIL -> {
                        FundDetailScreen(
                            fund = currentFund,
                            members = members,
                            installments = installments,
                            onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                            onNavigateToSettings = { viewModel.navigateTo(AppScreen.SETTINGS) },
                            onNavigateToMembers = { viewModel.navigateTo(AppScreen.MEMBERS) },
                            onNavigateToInstallments = { viewModel.navigateTo(AppScreen.INSTALLMENTS) }
                        )
                    }

                    AppScreen.MEMBERS -> {
                        MembersScreen(
                            members = members,
                            searchQuery = memberSearchQuery,
                            onSearchChange = { viewModel.setMemberSearchQuery(it) },
                            onTogglePayment = { member -> viewModel.toggleMemberPayment(member) },
                            onDeleteMember = { member -> viewModel.deleteMember(member) },
                            onOpenAddMember = { showAddMemberDialog = true },
                            onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                            isParticipantMode = isParticipantMode
                        )
                    }

                    AppScreen.INSTALLMENTS -> {
                        InstallmentsScreen(
                            installments = installments,
                            onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) }
                        )
                    }

                    AppScreen.REPORTS -> {
                        ReportsScreen(
                            fund = currentFund,
                            members = members,
                            installments = installments,
                            onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) }
                        )
                    }

                    AppScreen.SETTINGS -> {
                        SettingsScreen(
                            fund = currentFund,
                            isParticipantMode = isParticipantMode,
                            onToggleParticipantMode = { viewModel.setParticipantMode(it) },
                            onUpdateFund = { updated -> viewModel.updateFund(updated) },
                            onArchiveFund = { viewModel.archiveCurrentFund() },
                            onNavigateToMembers = { viewModel.navigateTo(AppScreen.MEMBERS) },
                            onNavigateToLottery = { viewModel.navigateTo(AppScreen.LOTTERY) },
                            onNavigateToEducation = { viewModel.navigateTo(AppScreen.EDUCATION) },
                            onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) }
                        )
                    }

                    AppScreen.LOTTERY -> {
                        LotteryScreen(
                            members = members,
                            onPerformFullLottery = { viewModel.performLotteryDraw() },
                            onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) }
                        )
                    }

                    AppScreen.EDUCATION -> {
                        EducationScreen(
                            onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) }
                        )
                    }

                    AppScreen.FUNDS_LIST -> {
                        // Managed via dialog
                        DashboardScreen(
                            currentFund = currentFund,
                            members = members,
                            installments = installments,
                            isParticipantMode = isParticipantMode,
                            onNavigate = { screen -> viewModel.navigateTo(screen) },
                            onOpenFundSwitcher = { showFundsListDialog = true },
                            onOpenLottery = { viewModel.navigateTo(AppScreen.LOTTERY) }
                        )
                    }
                }
            }
        }
    }

    // Add Member Dialog
    if (showAddMemberDialog) {
        AddMemberDialog(
            onDismiss = { showAddMemberDialog = false },
            onConfirm = { name, familyName, phone, recommender, isManager, roundNumber ->
                viewModel.addMember(
                    name = name,
                    familyName = familyName,
                    phoneNumber = phone,
                    recommenderName = recommender,
                    isManager = isManager,
                    roundNumber = roundNumber
                )
                showAddMemberDialog = false
            }
        )
    }

    // Funds Switcher / Multi-fund Management Dialog
    if (showFundsListDialog) {
        FundsListDialog(
            funds = activeFunds,
            activeFundId = activeFundId,
            onSelectFund = { id -> viewModel.selectFund(id) },
            onCreateFund = { name, manager, monthly, count, base, model, desc ->
                viewModel.createFund(name, manager, monthly, count, base, model, desc)
            },
            onDismiss = { showFundsListDialog = false }
        )
    }
}
