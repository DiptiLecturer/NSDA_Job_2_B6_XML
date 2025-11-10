package org.freedu.job_2batch6.Repository

import androidx.lifecycle.LiveData
import org.freedu.job_2batch6.Model.UserProfile
import org.freedu.job_2batch6.dao.UserProfileDao

class UserProfileRepository(private val userProfileDao: UserProfileDao) {

    val allProfiles: LiveData<List<UserProfile>> = userProfileDao.getAllProfiles()
    val profileCount: LiveData<Int> = userProfileDao.getProfileCount()

    suspend fun insert(profile: UserProfile): Long {
        return userProfileDao.insertProfile(profile)
    }

    suspend fun update(profile: UserProfile) {
        userProfileDao.updateProfile(profile)
    }

    suspend fun delete(profile: UserProfile) {
        userProfileDao.deleteProfile(profile)
    }

    suspend fun deleteById(profileId: Int) {
        userProfileDao.deleteProfileById(profileId)
    }

    fun getProfileById(profileId: Int): LiveData<UserProfile> {
        return userProfileDao.getProfileById(profileId)
    }
}