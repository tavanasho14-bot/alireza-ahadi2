package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.AnnouncementEntity
import com.example.data.model.FundEntity
import com.example.data.model.InstallmentEntity
import com.example.data.model.MemberEntity
import com.example.data.repository.FundRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    WELCOME,
    DASHBOARD,
    FUND_DETAIL,
    MEMBERS,
    INSTALLMENTS,
    REPORTS,
    SETTINGS,
    LOTTERY,
    EDUCATION,
    FUNDS_LIST
}

class FundViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FundRepository

    private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _isParticipantMode = MutableStateFlow(false)
    val isParticipantMode: StateFlow<Boolean> = _isParticipantMode.asStateFlow()

    private val _activeFundId = MutableStateFlow<Long>(1L)
    val activeFundId: StateFlow<Long> = _activeFundId.asStateFlow()

    private val _memberSearchQuery = MutableStateFlow("")
    val memberSearchQuery: StateFlow<String> = _memberSearchQuery.asStateFlow()

    private val _selectedMemberFilter = MutableStateFlow("ALL") // ALL, PAID, UNPAID
    val selectedMemberFilter: StateFlow<String> = _selectedMemberFilter.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = FundRepository(
            fundDao = db.fundDao(),
            memberDao = db.memberDao(),
            installmentDao = db.installmentDao(),
            announcementDao = db.announcementDao()
        )

        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    val activeFunds: StateFlow<List<FundEntity>> = repository.activeFunds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentFund: StateFlow<FundEntity?> = _activeFundId
        .flatMapLatest { id -> repository.getFundById(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val members: StateFlow<List<MemberEntity>> = _activeFundId
        .flatMapLatest { id -> repository.getMembersByFund(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val installments: StateFlow<List<InstallmentEntity>> = _activeFundId
        .flatMapLatest { id -> repository.getInstallmentsByFund(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val announcements: StateFlow<List<AnnouncementEntity>> = _activeFundId
        .flatMapLatest { id -> repository.getAnnouncementsByFund(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun setParticipantMode(enabled: Boolean) {
        _isParticipantMode.value = enabled
    }

    fun selectFund(fundId: Long) {
        _activeFundId.value = fundId
    }

    fun setMemberSearchQuery(query: String) {
        _memberSearchQuery.value = query
    }

    fun setMemberFilter(filter: String) {
        _selectedMemberFilter.value = filter
    }

    fun createFund(
        name: String,
        managerName: String,
        monthlyInstallment: Long,
        memberCount: Int,
        basePayoutAmount: Long,
        fairnessModel: String,
        description: String
    ) {
        viewModelScope.launch {
            val newId = repository.createFund(
                name = name,
                managerName = managerName,
                monthlyInstallment = monthlyInstallment,
                memberCount = memberCount,
                basePayoutAmount = basePayoutAmount,
                fairnessModel = fairnessModel,
                description = description
            )
            _activeFundId.value = newId
            _currentScreen.value = AppScreen.DASHBOARD
        }
    }

    fun updateFund(fund: FundEntity) {
        viewModelScope.launch {
            repository.updateFund(fund)
        }
    }

    fun addMember(
        name: String,
        familyName: String,
        phoneNumber: String,
        recommenderName: String,
        isManager: Boolean,
        roundNumber: Int? = null,
        photoUri: String? = null
    ) {
        viewModelScope.launch {
            val member = MemberEntity(
                fundId = _activeFundId.value,
                name = name,
                familyName = familyName,
                phoneNumber = phoneNumber,
                recommenderName = recommenderName,
                avatarId = (0..5).random(),
                photoUri = photoUri,
                isManager = isManager,
                roundNumber = roundNumber,
                isPaidCurrentMonth = false
            )
            repository.addMember(member)
        }
    }

    fun toggleMemberPayment(member: MemberEntity) {
        viewModelScope.launch {
            repository.toggleMemberPayment(member)
        }
    }

    fun deleteMember(member: MemberEntity) {
        viewModelScope.launch {
            repository.deleteMember(member)
        }
    }

    fun performLotteryDraw() {
        viewModelScope.launch {
            repository.performLotteryDraw(_activeFundId.value)
        }
    }

    fun addAnnouncement(title: String, message: String) {
        viewModelScope.launch {
            val fund = currentFund.value
            repository.addAnnouncement(
                fundId = _activeFundId.value,
                title = title,
                message = message,
                author = fund?.managerName ?: "مدیر صندوق"
            )
        }
    }

    fun archiveCurrentFund() {
        viewModelScope.launch {
            val fund = currentFund.value ?: return@launch
            repository.updateFund(fund.copy(isArchived = true))
            _currentScreen.value = AppScreen.DASHBOARD
        }
    }
}
