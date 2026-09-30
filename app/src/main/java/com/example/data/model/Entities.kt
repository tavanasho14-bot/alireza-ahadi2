package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class FairnessModel(val titleFa: String, val shortDesc: String, val fullExplanation: String) {
    STEPPED_INCREASING(
        titleFa = "پلکانی افزایشی (تعدیل تورم)",
        shortDesc = "مبلغ وام برای نفرات آخر به تدریج افزایش می‌یابد.",
        fullExplanation = "در این مدل محبوب، نفرات ابتدایی که زودتر وام می‌گیرند مبلغی معادل پایه منهای تعدیل دریافت می‌کنند و در عوض، هر ماه بر مبلغ دریافتی افزوده می‌شود تا نفرات آخر با دریافت مبلغی بالاتر (مثلاً تا ۱۱۰٪ مبلغ پایه) افت ارزش پول و تورم را به صورت کامل جبران نمایند."
    ),
    WINNER_COMPENSATION_FEE(
        titleFa = "کارمزد زودرس برندگان",
        shortDesc = "برندگان اول کارمزد اندکی می‌دهند که پاداش نفرات آخر می‌شود.",
        fullExplanation = "افرادی که در ماه‌های ابتدایی برنده می‌شوند، به ازای هر ماه بهره‌مندی زودهنگام از سرمایه، کارمزد منصفانه‌ای (مثلاً ۱٪) به صندوق می‌پردازند. تمامی این کارمزدها در صندوق ذخیره شده و در ماه‌های پایانی به عنوان سود و پاداش ویژه به نفرات آخر تعلق می‌گیرد."
    ),
    AUCTION_BID_POOL(
        titleFa = "حراج امتیاز نوبت",
        shortDesc = "پیشنهاد حق‌تقدم افراد به استخر سود نفرات پایانی واریز می‌شود.",
        fullExplanation = "اعضایی که عجله دارند می‌توانند برای ماه‌های اول مبلغی را به عنوان حق تقدم پیشنهاد دهند. تمام عواید حراج به صورت شفاف در استخر پاداش ذخیره شده و منحصراً بین ۳۰٪ پایانی اعضا تقسیم می‌شود."
    ),
    SAVINGS_INVESTMENT_POOL(
        titleFa = "پس‌انداز و سرمایه‌گذاری جمعی",
        shortDesc = "درصد کمی در دارایی امن سرمایه‌گذاری شده و سود به نفرات آخر می‌رسد.",
        fullExplanation = "بخشی از گردش وجوه ماهانه صندوق در ابزارهای با درآمد ثابت یا طلا به صورت کوتاه‌مدت نگهداری می‌شود و سود تضمینی حاصل از آن به نوبت‌های ماه‌های پایانی افزوده می‌شود."
    ),
    TRADITIONAL_EQUAL(
        titleFa = "روش سنتی (مبالغ یکسان)",
        shortDesc = "تمام اعضا مبلغ پایه کاملاً یکسان بدون تغییر دریافت می‌کنند.",
        fullExplanation = "روش سنتی قرعه‌کشی خانوادگی که همه افراد مبالغ مساوی دریافت می‌کنند."
    )
}

@Entity(tableName = "funds")
data class FundEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val managerName: String,
    val monthlyInstallment: Long,
    val basePayoutAmount: Long,
    val memberCount: Int,
    val currentBalance: Long,
    val startDateJalali: String,
    val activeRound: Int,
    val fairnessModel: String = FairnessModel.STEPPED_INCREASING.name,
    val fairnessRatePercent: Double = 1.0, // 1% per round step
    val isArchived: Boolean = false,
    val accessCode: String = "HAMYAR-${System.currentTimeMillis() % 10000}",
    val description: String = ""
)

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fundId: Long,
    val name: String,
    val familyName: String = "",
    val phoneNumber: String = "",
    val recommenderName: String = "",
    val avatarId: Int = 0,
    val photoUri: String? = null,
    val isManager: Boolean = false,
    val roundNumber: Int? = null,
    val hasWon: Boolean = false,
    val paidRoundsCount: Int = 0,
    val totalPaid: Long = 0,
    val joinDateJalali: String = "مهر ۱۴۰۵",
    val isPaidCurrentMonth: Boolean = true
)

@Entity(tableName = "installments")
data class InstallmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fundId: Long,
    val roundNumber: Int,
    val recipientMemberId: Long? = null,
    val recipientName: String,
    val dueDateJalali: String,
    val baseAmount: Long,
    val fairnessAdjustment: Long = 0,
    val finalPayoutAmount: Long,
    val isPaidToRecipient: Boolean = false,
    val collectedAmount: Long = 0,
    val status: String = "برنامه‌ریزی شده" // "پرداخت شده", "در انتظار", "برنامه‌ریزی شده"
)

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fundId: Long,
    val title: String,
    val message: String,
    val dateJalali: String,
    val author: String = "مدیر صندوق",
    val isImportant: Boolean = false
)
