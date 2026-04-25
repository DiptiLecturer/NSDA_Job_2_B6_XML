package org.freedu.job_2batch6.UserProfileDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.freedu.job_2batch6.Model.UserProfile
import org.freedu.job_2batch6.dao.UserProfileDao


@Database(entities = [UserProfile::class], version = 1, exportSchema = false)
abstract class UserProfileDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao

    companion object {

        @Volatile
        private var INSTANCE: UserProfileDatabase? = null

        fun getInstance(context: Context): UserProfileDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserProfileDatabase::class.java,
                    "user_profile_database"
                )
                    .fallbackToDestructiveMigration(false) // handles version upgrades gracefully
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
