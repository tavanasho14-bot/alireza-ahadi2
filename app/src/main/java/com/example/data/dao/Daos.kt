package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AnnouncementEntity
import com.example.data.model.FundEntity
import com.example.data.model.InstallmentEntity
import com.example.data.model.MemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FundDao {
    @Query("SELECT * FROM funds WHERE isArchived = 0 ORDER BY id DESC")
    fun getAllActiveFunds(): Flow<List<FundEntity>>

    @Query("SELECT * FROM funds ORDER BY id DESC")
    fun getAllFunds(): Flow<List<FundEntity>>

    @Query("SELECT * FROM funds WHERE id = :id LIMIT 1")
    fun getFundById(id: Long): Flow<FundEntity?>

    @Query("SELECT * FROM funds WHERE id = :id LIMIT 1")
    suspend fun getFundByIdSync(id: Long): FundEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFund(fund: FundEntity): Long

    @Update
    suspend fun updateFund(fund: FundEntity)

    @Delete
    suspend fun deleteFund(fund: FundEntity)

    @Query("SELECT COUNT(*) FROM funds")
    suspend fun getFundCount(): Int
}

@Dao
interface MemberDao {
    @Query("SELECT * FROM members WHERE fundId = :fundId ORDER BY (roundNumber IS NULL), roundNumber ASC, id ASC")
    fun getMembersByFund(fundId: Long): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE fundId = :fundId")
    suspend fun getMembersByFundSync(fundId: Long): List<MemberEntity>

    @Query("SELECT * FROM members WHERE id = :id LIMIT 1")
    suspend fun getMemberById(id: Long): MemberEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: MemberEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(members: List<MemberEntity>)

    @Update
    suspend fun updateMember(member: MemberEntity)

    @Delete
    suspend fun deleteMember(member: MemberEntity)

    @Query("DELETE FROM members WHERE fundId = :fundId")
    suspend fun deleteMembersByFund(fundId: Long)
}

@Dao
interface InstallmentDao {
    @Query("SELECT * FROM installments WHERE fundId = :fundId ORDER BY roundNumber ASC")
    fun getInstallmentsByFund(fundId: Long): Flow<List<InstallmentEntity>>

    @Query("SELECT * FROM installments WHERE fundId = :fundId ORDER BY roundNumber ASC")
    suspend fun getInstallmentsByFundSync(fundId: Long): List<InstallmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstallments(installments: List<InstallmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstallment(installment: InstallmentEntity): Long

    @Update
    suspend fun updateInstallment(installment: InstallmentEntity)

    @Query("DELETE FROM installments WHERE fundId = :fundId")
    suspend fun deleteInstallmentsByFund(fundId: Long)
}

@Dao
interface AnnouncementDao {
    @Query("SELECT * FROM announcements WHERE fundId = :fundId ORDER BY id DESC")
    fun getAnnouncementsByFund(fundId: Long): Flow<List<AnnouncementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: AnnouncementEntity): Long

    @Delete
    suspend fun deleteAnnouncement(announcement: AnnouncementEntity)
}
