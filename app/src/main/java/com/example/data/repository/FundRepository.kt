package com.example.data.repository

import com.example.data.dao.AnnouncementDao
import com.example.data.dao.FundDao
import com.example.data.dao.InstallmentDao
import com.example.data.dao.MemberDao
import com.example.data.model.AnnouncementEntity
import com.example.data.model.FairnessModel
import com.example.data.model.FundEntity
import com.example.data.model.InstallmentEntity
import com.example.data.model.MemberEntity
import com.example.util.PersianUtils
import kotlinx.coroutines.flow.Flow
import kotlin.math.roundToLong

class FundRepository(
    private val fundDao: FundDao,
    private val memberDao: MemberDao,
    private val installmentDao: InstallmentDao,
    private val announcementDao: AnnouncementDao
) {
    val activeFunds: Flow<List<FundEntity>> = fundDao.getAllActiveFunds()
    val allFunds: Flow<List<FundEntity>> = fundDao.getAllFunds()

    fun getFundById(fundId: Long): Flow<FundEntity?> = fundDao.getFundById(fundId)
    fun getMembersByFund(fundId: Long): Flow<List<MemberEntity>> = memberDao.getMembersByFund(fundId)
    fun getInstallmentsByFund(fundId: Long): Flow<List<InstallmentEntity>> = installmentDao.getInstallmentsByFund(fundId)
    fun getAnnouncementsByFund(fundId: Long): Flow<List<AnnouncementEntity>> = announcementDao.getAnnouncementsByFund(fundId)

    suspend fun createFund(
        name: String,
        managerName: String,
        monthlyInstallment: Long,
        memberCount: Int,
        basePayoutAmount: Long,
        startDateJalali: String = "مهر ۱۴۰۵",
        fairnessModel: String = FairnessModel.STEPPED_INCREASING.name,
        fairnessRatePercent: Double = 1.0,
        description: String = ""
    ): Long {
        val fund = FundEntity(
            name = name,
            managerName = managerName,
            monthlyInstallment = monthlyInstallment,
            basePayoutAmount = basePayoutAmount,
            memberCount = memberCount,
            currentBalance = basePayoutAmount,
            startDateJalali = startDateJalali,
            activeRound = 1,
            fairnessModel = fairnessModel,
            fairnessRatePercent = fairnessRatePercent,
            description = description
        )
        val fundId = fundDao.insertFund(fund)

        // Add Manager as first member
        val managerMember = MemberEntity(
            fundId = fundId,
            name = managerName,
            familyName = "",
            phoneNumber = "۰۹۱۲۰۰۰۰۰۰۰",
            recommenderName = "مدیر صندوق",
            avatarId = 0,
            isManager = true,
            roundNumber = 1,
            hasWon = true,
            isPaidCurrentMonth = true
        )
        memberDao.insertMember(managerMember)

        // Generate initial schedule
        regenerateInstallmentSchedule(fundId)

        // Add welcome announcement
        announcementDao.insertAnnouncement(
            AnnouncementEntity(
                fundId = fundId,
                title = "افتتاح صندوق $name",
                message = "صندوق $name با مدیریت $managerName راه‌اندازی شد. اقساط ماهانه طبق تقویم قابل پرداخت است.",
                dateJalali = startDateJalali,
                author = managerName,
                isImportant = true
            )
        )

        return fundId
    }

    suspend fun updateFund(fund: FundEntity) {
        fundDao.updateFund(fund)
        regenerateInstallmentSchedule(fund.id)
    }

    suspend fun addMember(member: MemberEntity): Long {
        val id = memberDao.insertMember(member)
        regenerateInstallmentSchedule(member.fundId)
        return id
    }

    suspend fun updateMember(member: MemberEntity) {
        memberDao.updateMember(member)
        regenerateInstallmentSchedule(member.fundId)
    }

    suspend fun deleteMember(member: MemberEntity) {
        memberDao.deleteMember(member)
        regenerateInstallmentSchedule(member.fundId)
    }

    suspend fun toggleMemberPayment(member: MemberEntity) {
        val updated = member.copy(
            isPaidCurrentMonth = !member.isPaidCurrentMonth,
            paidRoundsCount = if (!member.isPaidCurrentMonth) member.paidRoundsCount + 1 else (member.paidRoundsCount - 1).coerceAtLeast(0)
        )
        memberDao.updateMember(updated)
    }

    suspend fun performLotteryDraw(fundId: Long) {
        val members = memberDao.getMembersByFundSync(fundId)
        val unassignedMembers = members.filter { it.roundNumber == null }.shuffled()
        val assignedRounds = members.mapNotNull { it.roundNumber }.toSet()

        var currentRound = 1
        var unassignedIdx = 0

        val updatedMembers = mutableListOf<MemberEntity>()
        while (unassignedIdx < unassignedMembers.size) {
            while (assignedRounds.contains(currentRound)) {
                currentRound++
            }
            val member = unassignedMembers[unassignedIdx]
            updatedMembers.add(member.copy(roundNumber = currentRound))
            currentRound++
            unassignedIdx++
        }

        memberDao.insertMembers(updatedMembers)
        regenerateInstallmentSchedule(fundId)

        val fund = fundDao.getFundByIdSync(fundId)
        announcementDao.insertAnnouncement(
            AnnouncementEntity(
                fundId = fundId,
                title = "انجام قرعه‌کشی نوبت‌ها",
                message = "قرعه‌کشی شفاف نوبت‌ها انجام شد. تمامی اعضا می‌توانند نوبت جدید خود را در بخش اقساط مشاهده کنند.",
                dateJalali = "امروز",
                author = fund?.managerName ?: "مدیر صندوق",
                isImportant = true
            )
        )
    }

    suspend fun regenerateInstallmentSchedule(fundId: Long) {
        val fund = fundDao.getFundByIdSync(fundId) ?: return
        val members = memberDao.getMembersByFundSync(fundId)
        val memberMap = members.filter { it.roundNumber != null }.associateBy { it.roundNumber!! }

        val count = fund.memberCount.coerceAtLeast(1)
        val baseAmount = fund.basePayoutAmount
        val fairnessModel = try {
            FairnessModel.valueOf(fund.fairnessModel)
        } catch (e: Exception) {
            FairnessModel.STEPPED_INCREASING
        }

        val rate = fund.fairnessRatePercent

        installmentDao.deleteInstallmentsByFund(fundId)

        val installments = mutableListOf<InstallmentEntity>()
        for (round in 1..count) {
            val recipient = memberMap[round]
            val recipientName = recipient?.let { "${it.name} ${it.familyName}".trim() } ?: "نامشخص (نوبت $round)"

            // Compute fairness adjustment
            val adjustment: Long = when (fairnessModel) {
                FairnessModel.STEPPED_INCREASING -> {
                    // Symmetrical adjustment around midpoint
                    val midpoint = (count + 1) / 2.0
                    val offset = (round - midpoint) * (rate / 100.0)
                    (baseAmount * offset).roundToLong()
                }
                FairnessModel.WINNER_COMPENSATION_FEE -> {
                    if (round <= count / 2) {
                        // early winners pay fee
                        val fee = (baseAmount * (rate / 100.0) * (count / 2 - round + 1) * 0.4).roundToLong()
                        -fee
                    } else {
                        // late winners receive bonus
                        val bonus = (baseAmount * (rate / 100.0) * (round - count / 2) * 0.4).roundToLong()
                        bonus
                    }
                }
                FairnessModel.AUCTION_BID_POOL -> {
                    if (round > count * 0.7) {
                        val bonus = (baseAmount * 0.08).roundToLong()
                        bonus
                    } else if (round <= 3) {
                        -(baseAmount * 0.05).roundToLong()
                    } else 0L
                }
                FairnessModel.SAVINGS_INVESTMENT_POOL -> {
                    // Compound safe yield on floating balance for latter rounds
                    val bonusFactor = (round - 1) * (rate / 100.0) * 0.5
                    (baseAmount * bonusFactor).roundToLong()
                }
                FairnessModel.TRADITIONAL_EQUAL -> 0L
            }

            val finalPayout = baseAmount + adjustment
            val monthIndex = (round - 1 + 6) % 12 + 1 // Start around Mehr
            val year = 1405 + (round - 1 + 6) / 12
            val dueDate = "${PersianUtils.toPersianDigits(round * 8 % 28 + 1)} ${PersianUtils.getMonthName(monthIndex)} ${PersianUtils.toPersianDigits(year)}"

            val isPaid = round < fund.activeRound || (round == fund.activeRound && recipient?.hasWon == true)
            val status = if (isPaid) "پرداخت شده" else "برنامه‌ریزی شده"

            installments.add(
                InstallmentEntity(
                    fundId = fundId,
                    roundNumber = round,
                    recipientMemberId = recipient?.id,
                    recipientName = recipientName,
                    dueDateJalali = dueDate,
                    baseAmount = baseAmount,
                    fairnessAdjustment = adjustment,
                    finalPayoutAmount = finalPayout,
                    isPaidToRecipient = isPaid,
                    collectedAmount = if (isPaid) finalPayout else 0L,
                    status = status
                )
            )
        }

        installmentDao.insertInstallments(installments)
    }

    suspend fun addAnnouncement(fundId: Long, title: String, message: String, author: String) {
        announcementDao.insertAnnouncement(
            AnnouncementEntity(
                fundId = fundId,
                title = title,
                message = message,
                dateJalali = "امروز",
                author = author,
                isImportant = true
            )
        )
    }

    suspend fun checkAndSeedInitialData() {
        if (fundDao.getFundCount() > 0) return

        // Create initial default fund matching screenshot: "صندوق تست ۲"
        val fund1Id = fundDao.insertFund(
            FundEntity(
                name = "صندوق تست ۲",
                managerName = "علیرضا احدی",
                monthlyInstallment = 9_000_000,
                basePayoutAmount = 100_000_000,
                memberCount = 20,
                currentBalance = 100_000_000,
                startDateJalali = "مهر ۱۴۰۵",
                activeRound = 1,
                fairnessModel = FairnessModel.STEPPED_INCREASING.name,
                fairnessRatePercent = 1.0,
                description = "صندوق قرض‌الحسنه خانوادگی فامیل احدی با سودآوری عادلانه و تضمین ارزش پول نفرات آخر"
            )
        )

        // Create 2nd and 3rd funds to demonstrate multiple funds capability (up to 20 funds)
        val fund2Id = fundDao.insertFund(
            FundEntity(
                name = "صندوق همکاران شرکت",
                managerName = "علیرضا احدی",
                monthlyInstallment = 5_000_000,
                basePayoutAmount = 60_000_000,
                memberCount = 12,
                currentBalance = 60_000_000,
                startDateJalali = "آبان ۱۴۰۵",
                activeRound = 1,
                fairnessModel = FairnessModel.WINNER_COMPENSATION_FEE.name,
                fairnessRatePercent = 1.2,
                description = "صندوق رفاهی همکاران دپارتمان فنی"
            )
        )

        val fund3Id = fundDao.insertFund(
            FundEntity(
                name = "صندوق پس‌انداز دوستانه",
                managerName = "امیرحسین رضایی",
                monthlyInstallment = 10_000_000,
                basePayoutAmount = 150_000_000,
                memberCount = 15,
                currentBalance = 150_000_000,
                startDateJalali = "شهریور ۱۴۰۵",
                activeRound = 2,
                fairnessModel = FairnessModel.SAVINGS_INVESTMENT_POOL.name,
                fairnessRatePercent = 1.5,
                description = "صندوق سرمایه‌گذاری دورهمی دوستان دانشگاه"
            )
        )

        // Seed members for "صندوق تست ۲" exactly matching screenshots:
        // علیرضا (نوبت ۱ | پرداخت شده), شیرین (نوبت ۲ | در انتظار), یکتا (نوبت ۳), امیر (نوبت ۴), مریم (نوبت ۵), etc.
        val sampleMembers = listOf(
            MemberEntity(fundId = fund1Id, name = "علیرضا", familyName = "احدی", phoneNumber = "۰۹۱۲۱۱۱۱۱۱۱", recommenderName = "مدیر", avatarId = 0, isManager = true, roundNumber = 1, hasWon = true, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "شیرین", familyName = "رحیمی", phoneNumber = "۰۹۱۲۲۲۲۲۲۲۲", recommenderName = "علیرضا احدی", avatarId = 1, isManager = false, roundNumber = 2, hasWon = false, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "یکتا", familyName = "صادقی", phoneNumber = "۰۹۱۲۳۳۳۳۳۳۳", recommenderName = "شیرین رحیمی", avatarId = 2, isManager = false, roundNumber = 3, hasWon = false, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "امیر", familyName = "کاظمی", phoneNumber = "۰۹۱۲۴۴۴۴۴۴۴", recommenderName = "علیرضا احدی", avatarId = 3, isManager = false, roundNumber = 4, hasWon = false, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "مریم", familyName = "تهرانی", phoneNumber = "۰۹۱۲۵۵۵۵۵۵۵", recommenderName = "حاج احمد", avatarId = 4, isManager = false, roundNumber = 5, hasWon = false, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "حسین", familyName = "کریمی", phoneNumber = "۰۹۱۲۶۶۶۶۶۶۶", recommenderName = "علیرضا احدی", avatarId = 5, isManager = false, roundNumber = 6, hasWon = false, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "زهرا", familyName = "موسوی", phoneNumber = "۰۹۱۲۷۷۷۷۷۷۷", recommenderName = "مریم تهرانی", avatarId = 1, isManager = false, roundNumber = 7, hasWon = false, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "رضا", familyName = "نجفی", phoneNumber = "۰۹۱۲۸۸۸۸۸۸۸", recommenderName = "امیر کاظمی", avatarId = 0, isManager = false, roundNumber = 8, hasWon = false, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "سارا", familyName = "فرهمند", phoneNumber = "۰۹۱۲۹۹۹۹۹۹۹", recommenderName = "یکتا صادقی", avatarId = 2, isManager = false, roundNumber = 9, hasWon = false, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "مهدی", familyName = "شریفی", phoneNumber = "۰۹۱۲۰۱۱۱۱۱۱", recommenderName = "علیرضا احدی", avatarId = 3, isManager = false, roundNumber = 10, hasWon = false, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "نیلوفر", familyName = "باقری", phoneNumber = "۰۹۱۲۰۲۲۲۲۲۲", recommenderName = "شیرین رحیمی", avatarId = 4, isManager = false, roundNumber = 11, hasWon = false, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "محمد", familyName = "قاسمی", phoneNumber = "۰۹۱۲۰۳۳۳۳۳۳", recommenderName = "حسین کریمی", avatarId = 5, isManager = false, roundNumber = 12, hasWon = false, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "الهام", familyName = "اسدی", phoneNumber = "۰۹۱۲۰۴۴۴۴۴۴", recommenderName = "زهرا موسوی", avatarId = 1, isManager = false, roundNumber = 13, hasWon = false, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "سعید", familyName = "رستمی", phoneNumber = "۰۹۱۲۰۵۵۵۵۵۵", recommenderName = "رضا نجفی", avatarId = 0, isManager = false, roundNumber = 14, hasWon = false, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "فاطمه", familyName = "یوسفی", phoneNumber = "۰۹۱۲۰۶۶۶۶۶۶", recommenderName = "سارا فرهمند", avatarId = 2, isManager = false, roundNumber = 15, hasWon = false, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "علی", familyName = "اکبری", phoneNumber = "۰۹۱۲۰۷۷۷۷۷۷", recommenderName = "مهدی شریفی", avatarId = 3, isManager = false, roundNumber = 16, hasWon = false, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "نرگس", familyName = "میرزایی", phoneNumber = "۰۹۱۲۰۸۸۸۸۸۸", recommenderName = "نیلوفر باقری", avatarId = 4, isManager = false, roundNumber = 17, hasWon = false, isPaidCurrentMonth = true),
            MemberEntity(fundId = fund1Id, name = "پیمان", familyName = "جعفری", phoneNumber = "۰۹۱۲۰۹۹۹۹۹۹", recommenderName = "محمد قاسمی", avatarId = 5, isManager = false, roundNumber = 18, hasWon = false, isPaidCurrentMonth = true),
            // 2 members haven't paid yet this month (matching the 18 paid, 2 remaining from screenshot!)
            MemberEntity(fundId = fund1Id, name = "کامران", familyName = "صالحی", phoneNumber = "۰۹۱۳۱۱۱۱۱۱۱", recommenderName = "علیرضا احدی", avatarId = 0, isManager = false, roundNumber = 19, hasWon = false, isPaidCurrentMonth = false),
            MemberEntity(fundId = fund1Id, name = "بهناز", familyName = "روشن", phoneNumber = "۰۹۱۳۲۲۲۲۲۲۲", recommenderName = "الهام اسدی", avatarId = 1, isManager = false, roundNumber = 20, hasWon = false, isPaidCurrentMonth = false)
        )

        memberDao.insertMembers(sampleMembers)
        regenerateInstallmentSchedule(fund1Id)

        // Add announcements
        announcementDao.insertAnnouncement(
            AnnouncementEntity(
                fundId = fund1Id,
                title = "یادآوری پرداخت قسط ماه مهر",
                message = "اعضای محترم لطفاً تا دهم مهرماه قسط ماهانه خود به مبلغ ۹ میلیون تومان را واریز فرمایند.",
                dateJalali = "۵ مهر ۱۴۰۵",
                author = "علیرضا احدی",
                isImportant = true
            )
        )
        announcementDao.insertAnnouncement(
            AnnouncementEntity(
                fundId = fund1Id,
                title = "محاسبه پاداش تورم برای نفرات پایانی",
                message = "طبق مصوبه صندوق و الگوریتم پلکانی همیار، مبلغ دریافتی نوبت‌های آخر تا ۱۱۰ میلیون تومان افزایش خواهد یافت.",
                dateJalali = "۱ مهر ۱۴۰۵",
                author = "مدیر صندوق",
                isImportant = false
            )
        )

        // Seed members for fund 2
        val fund2Members = listOf(
            MemberEntity(fundId = fund2Id, name = "علیرضا احدی", phoneNumber = "۰۹۱۲۱۱۱۱۱۱۱", isManager = true, roundNumber = 1, hasWon = true),
            MemberEntity(fundId = fund2Id, name = "آرش بهرامی", phoneNumber = "۰۹۱۲۲۲۲۳۳۳۳", roundNumber = 2),
            MemberEntity(fundId = fund2Id, name = "نگین مرادی", phoneNumber = "۰۹۱۲۴۴۴۵۵۵۵", roundNumber = 3)
        )
        memberDao.insertMembers(fund2Members)
        regenerateInstallmentSchedule(fund2Id)
    }
}
