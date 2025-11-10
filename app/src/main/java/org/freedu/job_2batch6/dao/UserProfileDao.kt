package org.freedu.job_2batch6.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.freedu.job_2batch6.Model.UserProfile

@Dao
interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile): Long

    @Update
    suspend fun updateProfile(profile: UserProfile)

    @Delete
    suspend fun deleteProfile(profile: UserProfile)

    @Query("SELECT * FROM user_profiles ORDER BY createdAt DESC")
    fun getAllProfiles(): LiveData<List<UserProfile>>

    @Query("SELECT * FROM user_profiles WHERE id = :profileId")
    fun getProfileById(profileId: Int): LiveData<UserProfile>

    @Query("SELECT COUNT(*) FROM user_profiles")
    fun getProfileCount(): LiveData<Int>

    @Query("DELETE FROM user_profiles WHERE id = :profileId")
    suspend fun deleteProfileById(profileId: Int)
}