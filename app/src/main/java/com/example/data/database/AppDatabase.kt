package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.AnnouncementDao
import com.example.data.dao.FundDao
import com.example.data.dao.InstallmentDao
import com.example.data.dao.MemberDao
import com.example.data.model.AnnouncementEntity
import com.example.data.model.FundEntity
import com.example.data.model.InstallmentEntity
import com.example.data.model.MemberEntity

@Database(
    entities = [
        FundEntity::class,
        MemberEntity::class,
        InstallmentEntity::class,
        AnnouncementEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fundDao(): FundDao
    abstract fun memberDao(): MemberDao
    abstract fun installmentDao(): InstallmentDao
    abstract fun announcementDao(): AnnouncementDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sandogh_hamyar_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
