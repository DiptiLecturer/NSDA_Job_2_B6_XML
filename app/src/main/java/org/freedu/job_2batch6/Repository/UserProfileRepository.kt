package org.freedu.job_2batch6.Repository

import androidx.lifecycle.LiveData
import org.freedu.job_2batch6.dao.UserProfileDao
import org.freedu.job_2batch6.Model.UserProfile

class UserProfileRepository(private val dao: UserProfileDao) {

    // Expose all profiles as LiveData (Room handles background thread)
    val allProfiles: LiveData<List<UserProfile>> = dao.getAllProfiles()

    // Expose profile count as LiveData
    val profileCount: LiveData<Int> = dao.getProfileCount()

    // Get a single profile by ID
    fun getProfileById(profileId: Int): LiveData<UserProfile> {
        return dao.getProfileById(profileId)
    }

    // Insert — returns the new row ID
    suspend fun insertProfile(profile: UserProfile): Long {
        return dao.insertProfile(profile)
    }

    // Update an existing profile
    suspend fun updateProfile(profile: UserProfile) {
        dao.updateProfile(profile)
    }

    // Delete by full object
    suspend fun deleteProfile(profile: UserProfile) {
        dao.deleteProfile(profile)
    }

    // Delete by ID (useful when you only have the ID, not the full object)
    suspend fun deleteProfileById(profileId: Int) {
        dao.deleteProfileById(profileId)
    }
}