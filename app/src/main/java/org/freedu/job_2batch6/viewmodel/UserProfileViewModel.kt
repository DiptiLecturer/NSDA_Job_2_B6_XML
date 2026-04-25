package org.freedu.job_2batch6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.freedu.job_2batch6.Model.UserProfile
import org.freedu.job_2batch6.Repository.UserProfileRepository
import org.freedu.job_2batch6.UserProfileDatabase.UserProfileDatabase



class UserProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserProfileRepository

    // Observed directly by the UI
    val allProfiles: LiveData<List<UserProfile>>
    val profileCount: LiveData<Int>

    init {
        val dao = UserProfileDatabase.getInstance(application).userProfileDao()
        repository = UserProfileRepository(dao)
        allProfiles = repository.allProfiles
        profileCount = repository.profileCount
    }

    // Get single profile by ID — returns LiveData for the UI to observe
    fun getProfileById(profileId: Int): LiveData<UserProfile> {
        return repository.getProfileById(profileId)
    }

    // Insert a new profile
    fun insertProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.insertProfile(profile)
        }
    }

    // Update an existing profile
    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.updateProfile(profile)
        }
    }

    // Delete by full object
    fun deleteProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.deleteProfile(profile)
        }
    }

    // Delete by ID only
    fun deleteProfileById(profileId: Int) {
        viewModelScope.launch {
            repository.deleteProfileById(profileId)
        }
    }
}