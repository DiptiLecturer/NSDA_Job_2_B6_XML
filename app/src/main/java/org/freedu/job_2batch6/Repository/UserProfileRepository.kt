package org.freedu.job_2batch6.Repository

import androidx.lifecycle.LiveData
import org.freedu.job_2batch6.dao.UserProfileDao
import org.freedu.job_2batch6.Model.UserProfile

class UserProfileRepository(private val dao: UserProfileDao) {

    val allProfiles: LiveData<List<UserProfile>> = dao.getAllProfiles()

    val profileCount: LiveData<Int> = dao.getProfileCount()

    fun getProfileById(profileId: Int): LiveData<UserProfile> {
        return dao.getProfileById(profileId)
    }

    suspend fun insertProfile(profile: UserProfile): Long {
        return dao.insertProfile(profile)
    }

    suspend fun updateProfile(profile: UserProfile) {
        dao.updateProfile(profile)
    }
    suspend fun deleteProfile(profile: UserProfile) {
        dao.deleteProfile(profile)
    }

    suspend fun deleteProfileById(profileId: Int) {
        dao.deleteProfileById(profileId)
    }
}